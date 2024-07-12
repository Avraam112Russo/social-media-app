package com.n1nt3nd0.social_media_app.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldNameConstants;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldNameConstants
public class UserCreateEditDto {
    private UserInformationCreateEditDto userInformationCreateEditDto;
}