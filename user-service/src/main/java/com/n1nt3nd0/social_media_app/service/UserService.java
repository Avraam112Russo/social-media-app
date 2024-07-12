package com.n1nt3nd0.social_media_app.service;

import com.n1nt3nd0.social_media_app.dto.request.UserCreateEditDto;
import com.n1nt3nd0.social_media_app.dto.response.UserReadDto;
import com.n1nt3nd0.social_media_app.entity.UserInformation;
import com.n1nt3nd0.social_media_app.entity.UserSocialMedia;
import com.n1nt3nd0.social_media_app.exception.userExceptions.UserNotFoundException;
import com.n1nt3nd0.social_media_app.mapper.UserMapper;
import com.n1nt3nd0.social_media_app.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserReadDto saveUser(UserCreateEditDto dto){
        UserSocialMedia user = userMapper.toUser(dto);
        UserSocialMedia savedUser = userRepository.save(user);
        return userMapper.toUserReadDto(savedUser);
    }

    @Cacheable(cacheNames = "usersById")
    public UserReadDto getUserById(long id){
        Optional<UserSocialMedia> mayBeUser = userRepository.findById(id);
        UserSocialMedia userSocialMedia = mayBeUser.orElseThrow(() -> new UserNotFoundException("User %s not found".formatted(id)));
        log.info("Get user by id: {}", userSocialMedia.toString());
        return userMapper.toUserReadDto(userSocialMedia);
    }

    @CacheEvict(cacheNames = "usersById", key = "#id")
    public UserReadDto updateUserById(long id, UserCreateEditDto dto){
        UserSocialMedia userSocialMedia = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User %s not found".formatted(id)));
        UserInformation userInformation = userSocialMedia.getUserInformation();
        userInformation.setBirthday(dto.getUserInformationCreateEditDto().getBirthday());
        userInformation.setDescription(dto.getUserInformationCreateEditDto().getDescription());
        userInformation.setEmail(dto.getUserInformationCreateEditDto().getEmail());
        userInformation.setFirstName(dto.getUserInformationCreateEditDto().getFirstName());
        userInformation.setLastName(dto.getUserInformationCreateEditDto().getLastName());
        return userMapper.toUserReadDto(userRepository.save(userSocialMedia));
    }

}
