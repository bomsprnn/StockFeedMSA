package com.example.cloudgateway.Config;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class TokenValidationFilter implements GlobalFilter, Ordered {
    private final RedisTemplate<String, String> redisTemplate;

    public TokenValidationFilter(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        if (exchange.getRequest().getPath().toString().equals("/user/signup") || exchange.getRequest().getPath().toString().equals("/user/signup/confirm")) {
            // 회원가입 요청은 바로 통과시킨다.
            return chain.filter(exchange);
        }
        if (exchange.getRequest().getPath().toString().equals("/user/login")) {
            // 회원가입 요청은 바로 통과시킨다.
            return chain.filter(exchange);
        }
        String authorizationHeader = exchange.getRequest().getHeaders().getFirst("Authorization");
        String token = null;
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.substring(7); // "Bearer " 이후의 토큰 추출
        }
//        if (token == null || redisTemplate.hasKey(token)) {
//            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
//
//            return exchange.getResponse().setComplete();
//        }

        // 로그아웃 요청 처리
        if (token != null || exchange.getRequest().getMethod() == HttpMethod.POST && exchange.getRequest().getPath().toString().equals("/user/logout")) {
            redisTemplate.opsForValue().set(token, "blacklisted");
        }

        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return -100;
    }
}


