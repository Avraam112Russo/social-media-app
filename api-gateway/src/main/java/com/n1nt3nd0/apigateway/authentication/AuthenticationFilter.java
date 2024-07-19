package com.n1nt3nd0.apigateway.authentication;

import com.n1nt3nd0.apigateway.validationTokenDto.ValidTokenDto;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.concurrent.ExecutionException;

//@RefreshScope
@Component
@RequiredArgsConstructor
public class AuthenticationFilter implements GatewayFilter {
    private final RouterValidator routerValidator;
    private final JwtUtil jwtUtil;

    @SneakyThrows
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        if (routerValidator.isSecured.test(request)){
            if (this.isAuthMissing(request)){
                return this.onError(exchange, HttpStatus.UNAUTHORIZED);
            }
            final String token = this.getAuthHeader(request);
            this.updateRequest(exchange, token);
        }
        return chain.filter(exchange);
    }



    private Mono<Void> onError(ServerWebExchange exchange, HttpStatus httpStatus) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);
        return response.setComplete();
    }



    private boolean isAuthMissing(ServerHttpRequest request) {
        return !request.getHeaders().containsKey("Authorization");
    }



    private String getAuthHeader(ServerHttpRequest request) {
        return request.getHeaders().getOrEmpty("Authorization").get(0);
    }

    private void updateRequest(ServerWebExchange exchange, String token) throws ExecutionException, InterruptedException {
//        Claims claims = jwtUtil.getAllClaimsFromToken(token);
       String email = jwtUtil.getEmailFromClaims(exchange.getRequest());
        if (email != null){
            exchange.getRequest().mutate()
                .header("email", email)
                .build();
        }
    }
}
