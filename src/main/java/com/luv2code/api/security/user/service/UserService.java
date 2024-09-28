package com.luv2code.api.security.user.service;

import com.luv2code.api.security.user.dto.ChangePasswordDto;
import com.luv2code.api.security.user.dto.LoginDto;
import com.luv2code.api.security.user.dto.UserDto;
import com.luv2code.api.security.user.entity.Historic;
import com.luv2code.api.security.user.entity.User;
import com.luv2code.api.security.user.entity.response.AuthenticationResponse;

import java.security.Principal;
import java.util.List;

public interface UserService {
    AuthenticationResponse register(UserDto userDto);
    AuthenticationResponse authenticate(LoginDto loginDto);
    List<User> getAllUsers();
    List<Historic> getAllConnect();
    void changePassword(ChangePasswordDto changePasswordDto, Principal principal);
}
