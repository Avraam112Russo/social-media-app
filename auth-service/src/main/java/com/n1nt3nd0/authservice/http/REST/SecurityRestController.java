package com.n1nt3nd0.authservice.http.REST;

import com.n1nt3nd0.authservice.dto.LoginResponseDto;
import com.n1nt3nd0.authservice.dto.ErrorResponse;
import com.n1nt3nd0.authservice.dto.LoginRequestDto;
import com.n1nt3nd0.authservice.dto.UserRegisterDto;
import com.n1nt3nd0.authservice.model.UserEntity;
import com.n1nt3nd0.authservice.security.JwtUtil;
import com.n1nt3nd0.authservice.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth-service")
@RequiredArgsConstructor
public class SecurityRestController {
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService customUserDetailsService;
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDto dto){
        try {
            Authentication authentication =
                    authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword()));
            String email = authentication.getName();
            UserEntity user = UserEntity.builder()
                    .email(email)
                    .password(dto.getPassword())
                    .build();
            String token = jwtUtil.createToken(user);
            LoginResponseDto loginRes = LoginResponseDto.builder()
                    .token(token)
                    .email(email)
                    .build();
            return ResponseEntity.ok(loginRes);
        } catch (BadCredentialsException e){
            ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST,"Invalid username or password");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }catch (Exception e){
            ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }

    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserRegisterDto dto){
        UserEntity user = customUserDetailsService.registerUser(dto);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }
}
