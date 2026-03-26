package com.example.EduManager.domain.auth.service;

import com.example.EduManager.domain.user.entity.RefreshToken;
import com.example.EduManager.domain.user.entity.User;
import com.example.EduManager.domain.user.repository.RefreshTokenRepository;
import com.example.EduManager.global.exception.ErrorCode;
import com.example.EduManager.global.security.exception.JwtAuthException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final RefreshTokenRepository refreshTokenRepository;

    public void saveRefreshToken(User user, String token, long expiryMs) {
        LocalDateTime expiresAt = LocalDateTime.now().plusSeconds(expiryMs / 1000);
        refreshTokenRepository.save(RefreshToken.of(user, token, expiresAt));
    }

    public RefreshToken getByToken(String token) {
        return refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new JwtAuthException(ErrorCode.JWT_REFRESH_TOKEN_NOT_FOUND));
    }

    public void deleteRefreshToken(User user) {
        refreshTokenRepository.deleteByUser(user);
    }
}
