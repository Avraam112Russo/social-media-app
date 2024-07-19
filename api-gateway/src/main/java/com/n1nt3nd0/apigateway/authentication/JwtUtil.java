package com.n1nt3nd0.apigateway.authentication;//package com.n1nt3nd0.apigateway.authentication;

import io.jsonwebtoken.Claims;//
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.List;
////import com.n1nt3nd0.apigateway.authentication.http.JwtHttpClient;
//import com.n1nt3nd0.apigateway.validationTokenDto.ValidTokenDto;
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Component;
//import org.springframework.web.reactive.function.BodyInserters;
//import org.springframework.web.reactive.function.client.WebClient;
//
//import javax.crypto.SecretKey;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.UUID;
//
@Component
public class JwtUtil {
//    @Value("${jwt.secret}")
    private final String secret_key = "b5f59337a612a2a7dc07328f3e7d1a04722967c7f06df20a499a7d3f91ff2a7e";
//    @Value("${jwt.issuer}")
//    private String issuer;
//    @Value("${jwt.expiration}")
//    private Integer expirationInSeconds;
    private final String TOKEN_HEADER = "Authorization";
    private final String TOKEN_PREFIX = "Bearer ";
//
//
//
//
//    public SecretKey getSignInKey(){
//        byte[] keyBytes = Decoders.BASE64.decode(secret_key);
//        return Keys.hmacShaKeyFor(keyBytes);
//    }
//
//    public ValidTokenDto validateToken(String bearerToken) {
//        String tokenValue = retrieveToken(bearerToken);
//        Claims claimsFromToken = getClaimsFromToken(tokenValue);
//        String emailFromClaims = getEmailFromClaims(claimsFromToken);
//        boolean userExist = userDetailsService.userExists(emailFromClaims);
//        boolean validateExpirationDate = validateClaimsExpirationDate(claimsFromToken);
//        if (userExist && validateExpirationDate) {
//            return ValidTokenDto.builder()
//                    .userExist(true)
//                    .email(emailFromClaims)
//                    .build();
//        }
//        throw new RuntimeException("Invalid token");
//    }
//
//    public String createToken(UserEntity user) {
//        Map<String, String> claims = new HashMap<>(){{
//            put("email", user.getEmail()); // claims body
//        }};
//        Long expirationDateInMillis = expirationInSeconds * 1000L;
//        Date expirationDate = new Date(new Date().getTime() + expirationDateInMillis);
//        Date createdAt = new Date();
//
//        return Jwts.builder()
//                .claims(claims)
//                .issuer(issuer) // avraam112russo
//                .subject(user.getFirstName()) // userID
//                .issuedAt(createdAt)
//                .id(UUID.randomUUID().toString())
//                .expiration(expirationDate)
//                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
//                .compact();
//
//    }


    private Claims retrieveClaimsFromToken(ServerHttpRequest req) {
        try {
            String token = retrieveTokenFromHeader(req);
            if (token != null) {
                return getClaimsFromToken(token);
            }
            return null;
        } catch (ExpiredJwtException ex) {
            throw new RuntimeException("Token expired: " + ex.getMessage());
        } catch (Exception ex) {
            throw new RuntimeException("Error while retrieveClaimsFromToken IN JwtUtil: " + ex.getMessage());
        }
    }
    private String retrieveTokenFromHeader(ServerHttpRequest request) {


        String bearerToken = request.getHeaders().getOrEmpty(TOKEN_HEADER).get(0);
        if (bearerToken != null && bearerToken.startsWith(TOKEN_PREFIX)) {
            return bearerToken.substring(TOKEN_PREFIX.length());
        }
        return null;
    }
//    public String retrieveToken(String bearerToken) {
//        if (bearerToken != null && bearerToken.startsWith(TOKEN_PREFIX)) {
//            return bearerToken.substring(TOKEN_PREFIX.length());
//        }
//        throw new RuntimeException("Error while retrieving token value IN retrieveToken");
//    }
//
//    public boolean validateClaimsExpirationDate(Claims claims) throws AuthenticationException {
//        try {
//            return claims.getExpiration().after(new Date());
//        } catch (Exception e) {
//            throw e;
//        }
//    }
//
    private Claims getClaimsFromToken(String token){
        byte[] keyBytes = Decoders.BASE64.decode(secret_key);
        SecretKey secretKey = Keys.hmacShaKeyFor(keyBytes);
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getBody();
    }
    public String getEmailFromClaims(ServerHttpRequest serverHttpRequest){
        Claims claims = retrieveClaimsFromToken(serverHttpRequest);
        String email = claims.get("email", String.class);
        return email;
    }
}
