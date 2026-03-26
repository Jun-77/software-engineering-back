package com.example.EduManager.domain.user.repository;

import com.example.EduManager.domain.user.entity.RefreshToken;
import com.example.EduManager.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);
    void deleteByUser(User user);
}
