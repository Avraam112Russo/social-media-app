package com.n1nt3nd0.social_media_app.mapper;

import com.n1nt3nd0.social_media_app.dto.request.UserInformationCreateEditDto;
import com.n1nt3nd0.social_media_app.dto.response.UserInformationReadDto;
import com.n1nt3nd0.social_media_app.entity.UserInformation;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserInformationMapper {
    UserInformation toUserInformation(UserInformationCreateEditDto dto);
    UserInformationReadDto toUserInformationReadDto(UserInformation userInformation);
}
