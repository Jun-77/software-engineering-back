package com.example.EduManager.domain.auth;

import com.example.EduManager.domain.auth.dto.*;
import com.example.EduManager.domain.auth.service.AuthService;
import com.example.EduManager.domain.student.entity.StudentProfile;
import com.example.EduManager.domain.student.service.StudentService;
import com.example.EduManager.domain.teacher.service.TeacherService;
import com.example.EduManager.domain.user.entity.RefreshToken;
import com.example.EduManager.domain.user.entity.Role;
import com.example.EduManager.domain.user.entity.User;
import com.example.EduManager.domain.user.service.UserService;
import com.example.EduManager.global.exception.CustomException;
import com.example.EduManager.global.exception.ErrorCode;
import com.example.EduManager.global.security.JwtTokenProvider;
import com.example.EduManager.global.security.exception.JwtAuthException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class AuthFacade {

    private final UserService userService;
    private final StudentService studentService;
    private final TeacherService teacherService;
    private final AuthService authService;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UserResponse registerTeacher(TeacherRegisterRequest request) {
        User user = userService.registerSchoolUser(
                request.getEmail(), request.getPassword(), request.getName(),
                Role.TEACHER, request.getSchool(), request.getSchoolNumber());
        teacherService.createProfile(user, request.getGrade(), request.getClassNum());
        return UserResponse.of(user);
    }

    @Transactional
    public UserResponse registerStudent(StudentRegisterRequest request) {
        User user = userService.registerSchoolUser(
                request.getEmail(), request.getPassword(), request.getName(),
                Role.STUDENT, request.getSchool(), request.getSchoolNumber());
        studentService.createProfile(user, request.getGrade(), request.getClassNum(), request.getNumber());
        return UserResponse.of(user);
    }

    @Transactional
    public UserResponse registerParent(ParentRegisterRequest request) {
        User parent = userService.registerParentUser(
                request.getEmail(), request.getPassword(), request.getName());
        User childUser = userService.getStudentBySchoolAndSchoolNumber(
                request.getChildSchool(), request.getChildSchoolNumber());
        StudentProfile childProfile = studentService.getProfileByUser(childUser);
        studentService.linkParent(parent, childProfile);
        return UserResponse.of(parent);
    }

    @Transactional
    public LoginResult loginBySchool(SchoolLoginRequest request) {
        User user = userService.getBySchoolAndSchoolNumber(request.getSchool(), request.getSchoolNumber());
        validateLogin(user, request.getPassword());
        return issueTokens(user);
    }

    @Transactional
    public LoginResult loginByEmail(EmailLoginRequest request) {
        User user = userService.getByEmail(request.getEmail());
        validateLogin(user, request.getPassword());
        return issueTokens(user);
    }

    @Transactional
    public RefreshResult refresh(String refreshToken) {
        RefreshToken token = authService.getByToken(refreshToken);

        if (token.isExpired()) {
            authService.deleteRefreshToken(token.getUser());
            throw new JwtAuthException(ErrorCode.JWT_REFRESH_TOKEN_EXPIRED);
        }

        User user = token.getUser();
        authService.deleteRefreshToken(user);

        String newAccessToken = jwtTokenProvider.createAccessToken(user.getId());
        String newRefreshToken = jwtTokenProvider.createRefreshToken(user.getId());
        authService.saveRefreshToken(user, newRefreshToken, jwtTokenProvider.getRefreshTokenExpiry());

        return RefreshResult.of(newAccessToken, newRefreshToken);
    }

    @Transactional
    public void logout(Long userId) {
        User user = userService.getById(userId);
        authService.deleteRefreshToken(user);
    }

    private void validateLogin(User user, String rawPassword) {
        if (user.isDeleted()) {
            throw new CustomException(ErrorCode.USER_DELETED);
        }
        if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
            throw new CustomException(ErrorCode.BAD_CREDENTIALS);
        }
    }

    private LoginResult issueTokens(User user) {
        authService.deleteRefreshToken(user);
        String accessToken = jwtTokenProvider.createAccessToken(user.getId());
        String refreshToken = jwtTokenProvider.createRefreshToken(user.getId());
        authService.saveRefreshToken(user, refreshToken, jwtTokenProvider.getRefreshTokenExpiry());
        return LoginResult.of(user, accessToken, refreshToken);
    }
}
