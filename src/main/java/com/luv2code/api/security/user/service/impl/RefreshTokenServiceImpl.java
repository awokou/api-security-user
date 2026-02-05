package com.luv2code.api.security.user.service.impl;

import com.luv2code.api.security.user.dto.RefreshTokenDto;
import com.luv2code.api.security.user.entity.RefreshToken;
import com.luv2code.api.security.user.entity.User;
import com.luv2code.api.security.user.dto.RefreshTokenResponse;
import com.luv2code.api.security.user.exception.TokenException;
import com.luv2code.api.security.user.repository.RefreshTokenRepository;
import com.luv2code.api.security.user.repository.UserRepository;
import com.luv2code.api.security.user.service.RefreshTokenService;
import com.luv2code.api.security.user.service.UserService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

import java.time.Instant;
import java.util.Base64;
import java.util.UUID;

@Slf4j
@Service
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserService jwtService;

    @Value("${application.security.jwt.refresh-token.expiration}")
    private long refreshExpiration;

    public RefreshTokenServiceImpl(UserRepository userRepository, RefreshTokenRepository refreshTokenRepository,
            UserService jwtService) {
        this.userRepository = userRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.jwtService = jwtService;
    }

    @Override
    public RefreshToken createRefreshToken(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        RefreshToken refreshToken = RefreshToken.builder()
                .revoked(false)
                .user(user)
                .token(Base64.getEncoder().encodeToString(UUID.randomUUID().toString().getBytes()))
                .expiryDate(Instant.now().plusMillis(refreshExpiration))
                .build();

        return refreshTokenRepository.save(refreshToken);
    }

    @Override
    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token == null) {
            log.error("Token is null");
            throw new TokenException(null, "Token is null");
        }
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(token);
            throw new TokenException(token.getToken(),
                    "Refresh token was expired. Please make a new authentication request");
        }
        return token;
    }

    @Override
    public RefreshTokenResponse generateNewToken(RefreshTokenDto request) {
        User user = refreshTokenRepository.findByToken(request.getRefreshToken())
                .map(this::verifyExpiration)
                .map(RefreshToken::getUser)
                .orElseThrow(() -> new TokenException(request.getRefreshToken(), "Refresh token does not exist"));

        String token = jwtService.generateToken(user);

        return RefreshTokenResponse.builder()
                .accessToken(token)
                .refreshToken(request.getRefreshToken())
                .build();
    }
}
