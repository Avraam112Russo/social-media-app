package com.n1nt3nd0.apigateway.validationTokenDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ValidTokenDto {
    private String email;
    private boolean userExist;
}
