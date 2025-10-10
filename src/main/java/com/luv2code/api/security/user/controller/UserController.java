package com.luv2code.api.security.user.controller;


import com.luv2code.api.security.user.dto.LoginDto;
import com.luv2code.api.security.user.dto.RefreshTokenDto;
import com.luv2code.api.security.user.dto.UserDto;
import com.luv2code.api.security.user.entity.User;
import com.luv2code.api.security.user.dto.AuthenticationResponse;
import com.luv2code.api.security.user.dto.RefreshTokenResponse;
import com.luv2code.api.security.user.service.RefreshTokenService;
import com.luv2code.api.security.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class UserController {

    private final UserService userService;
    private final RefreshTokenService refreshTokenService;
    private final AuthenticationManager authenticationManager;

    public UserController(UserService userService, RefreshTokenService refreshTokenService, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.refreshTokenService = refreshTokenService;
        this.authenticationManager = authenticationManager;
    }

    @GetMapping("/users")
    @PreAuthorize("hasAuthority('READ') and hasRole('ADMIN')")
    @Operation(
            description = "This endpoint require a valid JWT, ADMIN role with READ",
            summary = "User secured endpoint",
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200"),
                    @ApiResponse(description = "Unauthorized", responseCode = "401")
            }
    )
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@Valid @RequestBody UserDto userDto) {
        return ResponseEntity.ok(userService.register(userDto));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody LoginDto loginDto) {
        return ResponseEntity.ok(userService.authenticate(loginDto));
    }
    @PostMapping("/refresh-token")
    public ResponseEntity<RefreshTokenResponse> refreshToken(@RequestBody RefreshTokenDto refreshTokenDto) {
        return ResponseEntity.ok(refreshTokenService.generateNewToken(refreshTokenDto));
    }

    @GetMapping("/info")
    public Authentication getAuthentication(@RequestBody LoginDto loginDto){
        return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getEmail(),loginDto.getPassword()));
    }
}
