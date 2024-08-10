package com.n1nt3nd0.social_media_app.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterMessageDto {
    private String id;
    private String email;
    private String firstName;
    private String lastName;
}

