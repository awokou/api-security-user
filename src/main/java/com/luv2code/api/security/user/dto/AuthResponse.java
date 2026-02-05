package com.luv2code.api.security.user.dto;

import lombok.*;

import java.util.List;

@Data
@Builder
public class AuthResponse {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private List<String> roles;
    private String accessToken;
    private String refreshToken;
}
