package com.n1nt3nd0.authservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRegisterDto {
    @Email(regexp = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
            + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$",
            message = "incorrect email pattern..")
    @Size(min = 5, max = 64)
    private String email;
    @Size(min = 5, max = 64, message = "The password must be contain from 5 to 64 characters...")
    private String password;
    @Size(min = 5, max = 32)
    private String firstName;
    @Size(min = 5, max = 32)
    private String lastName;
}
