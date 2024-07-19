package com.n1nt3nd0.authservice.mapper;

import com.n1nt3nd0.authservice.dto.authDto.UserRegisterDto;
import com.n1nt3nd0.authservice.model.UserEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserEntity toUserEntity(UserRegisterDto userRegisterDto);
}
