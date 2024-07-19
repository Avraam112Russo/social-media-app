package com.n1nt3nd0.apigateway.authentication;

import org.springframework.stereotype.Component;
import org.springframework.http.server.reactive.ServerHttpRequest;
import java.util.List;
import java.util.function.Predicate;

@Component
public class RouterValidator {

    public static final List<String> openApiEndpoints = List.of(
            "/api/auth-service/register",
            "/api/auth-service/login",
            "/api/auth-service/confirmationCode"
    );
    public Predicate<ServerHttpRequest> isSecured = request ->
        openApiEndpoints.stream()
                .noneMatch(uri -> request.getURI().getPath().contains(uri));

}
