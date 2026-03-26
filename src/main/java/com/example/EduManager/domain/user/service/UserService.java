package com.example.EduManager.domain.user.service;

import com.example.EduManager.domain.auth.dto.RegisterRequest;
import com.example.EduManager.domain.user.entity.School;
import com.example.EduManager.domain.user.entity.User;
import com.example.EduManager.domain.user.repository.UserRepository;
import com.example.EduManager.global.exception.CustomException;
import com.example.EduManager.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new CustomException(ErrorCode.DUPLICATED_USER);
        }
        User user = User.of(
                request.getEmail(),
                passwordEncoder.encode(request.getPassword()),
                request.getName(),
                request.getRole(),
                request.getSchool(),
                request.getSchoolNumber()
        );
        return userRepository.save(user);
    }

    public User getById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }

    public User getByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.BAD_CREDENTIALS));
    }

    public User getBySchoolAndSchoolNumber(School school, String schoolNumber) {
        return userRepository.findBySchoolAndSchoolNumber(school, schoolNumber)
                .orElseThrow(() -> new CustomException(ErrorCode.BAD_CREDENTIALS));
    }
}
