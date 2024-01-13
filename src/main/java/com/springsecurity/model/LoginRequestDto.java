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
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "password")
public class LoginRequestDto {

    @NotBlank(message = "validation.email-empty")
    @Email(message = "validation.email-format")
    private String email;

    @NotBlank(message = "validation.password-empty")
    @Size(min = 6, message = "validation.password-min-length")
    private String password;
}
