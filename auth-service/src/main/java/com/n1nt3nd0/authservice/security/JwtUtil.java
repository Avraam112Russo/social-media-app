package com.n1nt3nd0.authservice.security;

import com.n1nt3nd0.authservice.dto.validationTokenDto.ValidTokenDto;
import com.n1nt3nd0.authservice.model.UserEntity;
import com.n1nt3nd0.authservice.service.CustomUserDetailsService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
public class JwtUtil {
    @Value("${jwt.secret}")
    private String secret_key;
    @Value("${jwt.issuer}")
    private String issuer;
    @Value("${jwt.expiration}")
    private Integer expirationInSeconds;
    private CustomUserDetailsService userDetailsService;
    private final String TOKEN_HEADER = "Authorization";
    private final String TOKEN_PREFIX = "Bearer ";




    public SecretKey getSignInKey(){
        byte[] keyBytes = Decoders.BASE64.decode(secret_key);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public ValidTokenDto validateToken(String bearerToken) {
        String tokenValue = retrieveToken(bearerToken);
        Claims claimsFromToken = getClaimsFromToken(tokenValue);
        String emailFromClaims = getEmailFromClaims(claimsFromToken);
        boolean userExist = userDetailsService.userExists(emailFromClaims);
        boolean validateExpirationDate = validateClaimsExpirationDate(claimsFromToken);
        if (userExist && validateExpirationDate) {
            return ValidTokenDto.builder()
                    .userExist(true)
                    .email(emailFromClaims)
                    .build();
        }
        throw new RuntimeException("Invalid token");
    }

    public String createToken(UserEntity user) {
        Map<String, String> claims = new HashMap<>(){{
            put("email", user.getEmail()); // claims body
        }};
        Long expirationDateInMillis = expirationInSeconds * 1000L;
        Date expirationDate = new Date(new Date().getTime() + expirationDateInMillis);
        Date createdAt = new Date();

        return Jwts.builder()
                .claims(claims)
                .issuer(issuer) // avraam112russo
                .subject(user.getFirstName()) // userID
                .issuedAt(createdAt)
                .id(UUID.randomUUID().toString())
                .expiration(expirationDate)
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();

    }
    public Claims retrieveClaimsFromToken(HttpServletRequest req) {
        try {
            String token = retrieveTokenFromHeader(req);
            if (token != null) {
                return getClaimsFromToken(token);
            }
            return null;
        } catch (ExpiredJwtException ex) {
            req.setAttribute("expired", ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            req.setAttribute("invalid", ex.getMessage());
            throw ex;
        }
    }
    public String retrieveTokenFromHeader(HttpServletRequest request) {

        String bearerToken = request.getHeader(TOKEN_HEADER);
        if (bearerToken != null && bearerToken.startsWith(TOKEN_PREFIX)) {
            return bearerToken.substring(TOKEN_PREFIX.length());
        }
        return null;
    }
    public String retrieveToken(String bearerToken) {
        if (bearerToken != null && bearerToken.startsWith(TOKEN_PREFIX)) {
            return bearerToken.substring(TOKEN_PREFIX.length());
        }
        throw new RuntimeException("Error while retrieving token value IN retrieveToken");
    }

    public boolean validateClaimsExpirationDate(Claims claims) throws AuthenticationException {
        try {
            return claims.getExpiration().after(new Date());
        } catch (Exception e) {
            throw e;
        }
    }

    private Claims getClaimsFromToken(String token){
        byte[] keyBytes = Decoders.BASE64.decode(secret_key);
        SecretKey secretKey = Keys.hmacShaKeyFor(keyBytes);
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getBody();
    }
    public String getEmailFromClaims(Claims claims){
        String email = claims.get("email", String.class);
        return email;
    }
}
