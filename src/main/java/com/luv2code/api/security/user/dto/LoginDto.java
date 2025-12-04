package com.luv2code.api.security.user.dto;

import lombok.*;

@Data
@Builder
public class LoginDto {
    private String email;
    private String password;
}
