package com.n1nt3nd0.apigateway.config;

import com.n1nt3nd0.apigateway.authentication.AuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableHystrix
@RequiredArgsConstructor
public class GateWayConfig {
    private final AuthenticationFilter filter;

    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("user-serviced", r -> r.path("/api/user-service/**")
                        .filters(f -> f.filter(filter))
                        .uri("lb://user-serviced"))

                .route("video-service", r -> r.path("/api/video-service/**")
                        .filters(f -> f.filter(filter))
                        .uri("lb://video-service"))
                .build();
    }
}
