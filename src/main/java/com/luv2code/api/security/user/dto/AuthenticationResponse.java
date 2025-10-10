package com.luv2code.api.security.user.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
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
