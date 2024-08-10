package com.n1nt3nd0.authservice.dto;

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

