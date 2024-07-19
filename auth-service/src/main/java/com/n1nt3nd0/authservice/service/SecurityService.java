package com.n1nt3nd0.authservice.service;

import com.n1nt3nd0.authservice.dto.authDto.ErrorResponse;
import com.n1nt3nd0.authservice.dto.authDto.LoginRequestDto;
import com.n1nt3nd0.authservice.dto.authDto.LoginResponseDto;
import com.n1nt3nd0.authservice.model.UserEntity;
import com.n1nt3nd0.authservice.repository.UserRepository;
import com.n1nt3nd0.authservice.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SecurityService {

    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    public ResponseEntity<?> login(LoginRequestDto dto){
        try {
            UserEntity mayBeUSer = userRepository.findByEmail(dto.getEmail()).orElseThrow(() -> new UsernameNotFoundException("User not found."));
            if (!mayBeUSer.getIsEnabled()){
                throw new RuntimeException("user account disabled");
            }
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
}
