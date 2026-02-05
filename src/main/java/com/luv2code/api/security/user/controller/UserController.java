package com.luv2code.api.security.user.controller;

import com.luv2code.api.security.user.dto.LoginDto;
import com.luv2code.api.security.user.dto.RefreshTokenDto;
import com.luv2code.api.security.user.dto.UserDto;
import com.luv2code.api.security.user.dto.UserResponse;
import com.luv2code.api.security.user.dto.AuthResponse;
import com.luv2code.api.security.user.dto.RefreshTokenResponse;
import com.luv2code.api.security.user.service.RefreshTokenService;
import com.luv2code.api.security.user.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@Tag(name = "User Controller", description = "API pour la gestion des utilisateurs et l'authentification")
public class UserController {

    private final UserService userService;
    private final RefreshTokenService refreshTokenService;

    public UserController(UserService userService, RefreshTokenService refreshTokenService) {
        this.userService = userService;
        this.refreshTokenService = refreshTokenService;
    }

    @PostMapping("/register")
    @Operation(summary = "Enregistrer un nouvel utilisateur")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody UserDto userDto) {
        return ResponseEntity.ok(userService.register(userDto));
    }

    @PostMapping("/authenticate")
    @Operation(summary = "Authentifier un utilisateur")
    public ResponseEntity<AuthResponse> authenticate(@RequestBody LoginDto loginDto) {
        return ResponseEntity.ok(userService.authenticate(loginDto));
    }

    @PostMapping("/refresh-token")
    @Operation(summary = "Générer un nouveau token à partir d'un refresh token")
    public ResponseEntity<RefreshTokenResponse> refreshToken(@RequestBody RefreshTokenDto refreshTokenDto) {
        return ResponseEntity.ok(refreshTokenService.generateNewToken(refreshTokenDto));
    }

    @GetMapping("/users")
    @PreAuthorize("hasAuthority('READ') and hasRole('ADMIN')")
    @Operation(summary = "Récupérer tous les utilisateurs (ADMIN uniquement)")
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }
}
