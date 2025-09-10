package com.luv2code.api.security.user.service;

import com.luv2code.api.security.user.dto.RefreshTokenDto;
import com.luv2code.api.security.user.entity.RefreshToken;
import com.luv2code.api.security.user.dto.RefreshTokenResponse;

import java.util.Optional;

public interface RefreshTokenService {
    RefreshToken createRefreshToken(Long id);
    RefreshToken verifyExpiration(RefreshToken token);
    Optional<RefreshToken> findByToken(String token);
    RefreshTokenResponse generateNewToken(RefreshTokenDto refreshTokenDto);
}
