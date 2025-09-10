package com.luv2code.api.security.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationResponse {

    private  Long id;
    private String firstname;
    private String lastname;
    private String email;
    private List<String> roles;
    private String accessToken;
    private String refreshToken;

}
