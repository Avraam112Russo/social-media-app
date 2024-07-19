package com.n1nt3nd0.authservice.http.REST;

import com.n1nt3nd0.authservice.dto.authDto.AuthResponseDto;
import com.n1nt3nd0.authservice.dto.authDto.ConfirmationCodeDto;
import com.n1nt3nd0.authservice.dto.authDto.LoginRequestDto;
import com.n1nt3nd0.authservice.dto.authDto.UserRegisterDto;
import com.n1nt3nd0.authservice.model.UserEntity;
import com.n1nt3nd0.authservice.service.CustomUserDetailsService;
import com.n1nt3nd0.authservice.service.SecurityService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth-service")
@RequiredArgsConstructor
public class SecurityRestControllerV1 {
    private final CustomUserDetailsService customUserDetailsService;
    private final SecurityService securityService;
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDto dto){
        return securityService.login(dto);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid UserRegisterDto dto){
        UserEntity user = customUserDetailsService.registerUser(dto);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }
    @PostMapping("/confirmationCode")
    public ResponseEntity<?> sendConfirmationCode(@RequestBody ConfirmationCodeDto dto){
        AuthResponseDto<String> stringAuthResponseDto = customUserDetailsService.sendConfirmationCode(dto);
        return new ResponseEntity<>(stringAuthResponseDto, HttpStatus.OK);
    }
}
