package com.n1nt3nd0.authservice.service;

import com.n1nt3nd0.authservice.dto.UserRegisterDto;
import com.n1nt3nd0.authservice.mapper.UserMapper;
import com.n1nt3nd0.authservice.model.UserEntity;
import com.n1nt3nd0.authservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity savedUser = userRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException(username));
        return User.builder()
                .username(savedUser.getEmail())
                .password(savedUser.getPassword())
                .authorities(Collections.singletonList(new SimpleGrantedAuthority("USER")))
                .build();
    }

    public UserEntity registerUser(UserRegisterDto dto) {
        UserEntity userEntity = userMapper.toUserEntity(dto);
        userEntity.setPassword(passwordEncoder.encode(dto.getPassword()));
        UserEntity savedUser = userRepository.save(userEntity);
        log.info("User saved successfully: {}", savedUser);
        return savedUser;
    }
}
