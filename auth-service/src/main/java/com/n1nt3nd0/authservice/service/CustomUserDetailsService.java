package com.n1nt3nd0.authservice.service;

import com.n1nt3nd0.authservice.dto.authDto.AuthResponseDto;
import com.n1nt3nd0.authservice.dto.authDto.ConfirmationCodeDto;
import com.n1nt3nd0.authservice.dto.authDto.UserRegisterDto;
import com.n1nt3nd0.authservice.feignClient.EmailServiceFeignClient;
import com.n1nt3nd0.authservice.feignClient.NotificationDto;
import com.n1nt3nd0.authservice.feignClient.ResponseDto;
import com.n1nt3nd0.authservice.mapper.UserMapper;
import com.n1nt3nd0.authservice.model.UserEntity;
import com.n1nt3nd0.authservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    @Autowired
    private EmailServiceFeignClient feignClient;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity savedUser = userRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException(username));
        return User.builder()
                .username(savedUser.getEmail())
                .password(savedUser.getPassword())
                .authorities(Collections.singletonList(new SimpleGrantedAuthority("USER")))
                .build();
    }

    public boolean userExists(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    @Transactional
    public UserEntity registerUser(UserRegisterDto dto) {
        try {
            UserEntity userEntity = userMapper.toUserEntity(dto);
            String confirmationCode = getRandomNumberString();
            userEntity.setCreatedAt(LocalDateTime.now());
            userEntity.setUpdatedAt(LocalDateTime.now());
            userEntity.setConfirmationCode(confirmationCode);
            userEntity.setPassword(passwordEncoder.encode(dto.getPassword()));
            userEntity.setIsEnabled(false);
            UserEntity savedUser = userRepository.save(userEntity);
            log.info("User saved successfully: {}", savedUser);
            NotificationDto notificationDto = NotificationDto.builder()
                    .CONFIRMATION_CODE(confirmationCode)
                    .email(userEntity.getEmail())
                    .build();
            ResponseEntity<ResponseDto> responseDtoResponseEntity = feignClient.sendMessageToNotificationService(notificationDto);
            log.info(responseDtoResponseEntity.toString());
            return savedUser;
        }catch (Exception e){
            throw new RuntimeException("Error while registerUser IN CustomUserDetailsService" + e.getMessage());
        }

    }
    @Transactional
    public AuthResponseDto<String> sendConfirmationCode(ConfirmationCodeDto dto) {
            UserEntity mayBeUser = userRepository.findByEmail(dto.getEmail())
                    .orElseThrow(() -> new UsernameNotFoundException("User not found: " + dto.getEmail()));
            String confirmationCode_saved_account = mayBeUser.getConfirmationCode();
            if (confirmationCode_saved_account.equals(dto.getConfirmationCode())) {
                mayBeUser.setIsEnabled(true);
                userRepository.save(mayBeUser);
                log.info("user account successfully enabled: {}", mayBeUser);
                return new AuthResponseDto<>("user account successfully enabled: %s".formatted( mayBeUser ), HttpStatus.OK);
            }
            throw new RuntimeException("Confirmation code is incorrect");
    }
    public String getRandomNumberString() {
        // It will generate 6 digit random Number.
        // from 0 to 999999
        Random rnd = new Random();
        int number = rnd.nextInt(999999);
        // this will convert any number sequence into 6 character.
        return String.format("%06d", number);
    }
}
