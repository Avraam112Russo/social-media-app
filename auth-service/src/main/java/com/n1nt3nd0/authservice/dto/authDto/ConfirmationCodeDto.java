package com.n1nt3nd0.authservice.dto.authDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ConfirmationCodeDto {
    private String email;
    private String confirmationCode;
}
