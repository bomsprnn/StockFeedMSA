package com.example.cloudgateway.Config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//@Configuration
public class FilterConfig {
    //@Bean
    public RouteLocator gatewayRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(r -> r.path("/activity/**")
                        .filters(f->f.addRequestHeader("req","req header")
                                .addResponseHeader("res","res header"))
                        .uri("http://localhost:8082"))
                .route(r -> r.path("/user/**")
                        .filters(f->f.addRequestHeader("req2","req2 header")
                                .addResponseHeader("res2","res2 header"))
                        .uri("http://localhost:8081"))
                .build();
    }
}
