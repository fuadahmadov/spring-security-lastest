package com.springsecurity.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(exclude = "password")
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationRequestDto {

    @NotBlank(message = "validation.name-empty")
    @Size(min = 3, message = "validation.name-min-length")
    private String name;

    @NotBlank(message = "validation.email-empty")
    @Email(message = "validation.email-format")
    private String email;

    @NotBlank(message = "validation.password-empty")
    @Size(min = 6, message = "validation.password-min-length")
    private String password;

}
