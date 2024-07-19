package com.n1nt3nd0.authservice.http.REST;

import com.n1nt3nd0.authservice.dto.validationTokenDto.ValidTokenDto;
import com.n1nt3nd0.authservice.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v2")
@RequiredArgsConstructor
public class ValidTokenRestControllerV1 {

    private final JwtUtil jwtUtil;
    @GetMapping("/valid-token")
    public ResponseEntity<?> validToken(@RequestParam("token") String bearerToken) {
        ValidTokenDto validTokenDto = jwtUtil.validateToken(bearerToken);
        return ResponseEntity.ok(validTokenDto);
    }
}
