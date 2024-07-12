package com.n1nt3nd0.social_media_app.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserInformationReadDto implements Serializable {
    private String status;
    private String description;
    private String email;
    private String firstName;
    private String lastName;
    private LocalDate birthday;
}
