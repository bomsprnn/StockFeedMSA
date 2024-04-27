package com.example.cloudgateway.Config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.security.Key;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class TokenValidationFilter implements GlobalFilter, Ordered {
    private final RedisTemplate<String, String> redisTemplate;
    private final Key key;


    public TokenValidationFilter(RedisTemplate<String, String> redisTemplate, @Value("${jwt.secret}") String secretKey) {
        this.redisTemplate = redisTemplate;
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        if (exchange.getRequest().getPath().toString().equals("/user/signup") ||
                exchange.getRequest().getPath().toString().equals("/user/signup/confirm") ||
                exchange.getRequest().getPath().toString().equals("/user/login")) {
            // 회원가입 요청은 바로 통과시킨다.
            return chain.filter(exchange);
        }

        String authorizationHeader = exchange.getRequest().getHeaders().getFirst("Authorization");
        String token = null;
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.substring(7); // "Bearer " 이후의 토큰 추출
        }
        if (token == null || redisTemplate.hasKey(token)) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        String username = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().getSubject();
        String lastLogout = redisTemplate.opsForValue().get("lastLogout:" + username);

        //모든 기기에서 로그아웃
        if (exchange.getRequest().getPath().toString().equals("/user/logoutall")) {
            //redisTemplate.opsForValue().set(token, "blacklisted");
            redisTemplate.opsForValue().set("lastLogout:" + username, String.valueOf(System.currentTimeMillis()));
            return chain.filter(exchange);
        }

        // 마지막 로그아웃 시간과 토큰 생성 시간 비교
        if (lastLogout != null) {
            long lastLogoutTime = Long.parseLong(lastLogout);
            Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
            Date issuedAt = claims.getIssuedAt();
            if (issuedAt.getTime() < lastLogoutTime) {
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                log.info("lastLogoutTime: " + lastLogoutTime);
                log.info("issuedAt: " + issuedAt.getTime());
                return exchange.getResponse().setComplete();
            }
        }

        //로그아웃
        if (exchange.getRequest().getMethod() == HttpMethod.POST && exchange.getRequest().getPath().toString().equals("/user/logout")) {
            Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
            Date expiration = claims.getExpiration();
            log.info("expiration: " + expiration);
            long remainingTime = expiration.getTime() - System.currentTimeMillis(); // 남은 유효기간 계산
            log.info("remainingTime: " + remainingTime);
            // Redis에 토큰 저장 시, 남은 유효기간을 설정
            if (remainingTime > 0) {
                redisTemplate.opsForValue().set(token, "blacklisted", remainingTime, TimeUnit.MILLISECONDS);
            }
        }

        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return -100;
    }
}


