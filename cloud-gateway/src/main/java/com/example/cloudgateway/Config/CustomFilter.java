package com.example.cloudgateway.Config;

import lombok.extern.slf4j.*;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class CustomFilter extends AbstractGatewayFilterFactory<CustomFilter.Config> {
    public static class Config {
        // Put configuration properties here
    }

    public CustomFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        // Custom Pre Filter
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            ServerHttpResponse response = exchange.getResponse();

            //log.info("Custom Pre Filter Logging: request id -> {}",exchange.getRequest().getId());

            // Custom Post Filter
            return chain.filter(exchange).then(Mono.fromRunnable(() -> {
                //필터가 진행된 다음 mono 객체를 반환(비동기 방식에서 추가된 mono)
                //log.info("Custom Post Filter Logging: response code -> {}",response.getStatusCode());
            }));
        };
    }
}
