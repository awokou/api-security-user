package com.luv2code.api.security.user.service;

import com.luv2code.api.security.user.dto.LoginDto;
import com.luv2code.api.security.user.dto.UserDto;
import com.luv2code.api.security.user.dto.UserResponse;
import com.luv2code.api.security.user.dto.AuthResponse;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetails;

public interface UserService {

    AuthResponse register(UserDto userDto);

    AuthResponse authenticate(LoginDto loginDto);

    List<UserResponse> getAllUsers();

    String extractUserName(String token);

    String generateToken(UserDetails userDetails);

    boolean isTokenValid(String token, UserDetails userDetails);
}
