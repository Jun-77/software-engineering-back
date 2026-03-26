package com.example.EduManager.domain.auth.dto;

import com.example.EduManager.domain.user.entity.User;
import lombok.Getter;

@Getter
public class LoginResult {

    private final String accessToken;
    private final String refreshToken;
    private final User user;

    private LoginResult(String accessToken, String refreshToken, User user) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.user = user;
    }

    public static LoginResult of(User user, String accessToken, String refreshToken) {
        return new LoginResult(accessToken, refreshToken, user);
    }
}
