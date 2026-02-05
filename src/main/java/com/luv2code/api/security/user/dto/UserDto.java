package com.luv2code.api.security.user.dto;

import com.luv2code.api.security.user.annotations.StrongPassword;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@NoArgsConstructor
public class UserDto {

    private Long id;

    @NotBlank(message = "firstname is required")
    private String firstName;

    @NotBlank(message = "lastname is required")
    private String lastName;

    @NotBlank(message = "email is required")
    @Email(message = "email format is not valid")
    private String email;

    @NotBlank(message = "password is required")
    @StrongPassword
    private String password;
}
