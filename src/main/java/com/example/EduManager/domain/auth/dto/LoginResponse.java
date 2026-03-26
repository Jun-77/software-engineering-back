package com.example.EduManager.domain.auth.dto;

import com.example.EduManager.domain.user.entity.User;
import lombok.Getter;

@Getter
public class LoginResponse {

    private final String accessToken;
    private final UserResponse user;

    private LoginResponse(String accessToken, UserResponse user) {
        this.accessToken = accessToken;
        this.user = user;
    }

    public static LoginResponse of(User user, String accessToken) {
        return new LoginResponse(accessToken, UserResponse.of(user));
    }
}
