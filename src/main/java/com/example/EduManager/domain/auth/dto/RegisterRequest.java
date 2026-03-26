package com.example.EduManager.domain.auth.dto;

import com.example.EduManager.domain.user.entity.Role;
import com.example.EduManager.domain.user.entity.School;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class RegisterRequest {

    @NotBlank(message = "이메일을 입력해주세요.")
    @Email(message = "올바른 이메일 형식이 아닙니다.")
    private String email;

    @NotBlank(message = "비밀번호를 입력해주세요.")
    private String password;

    @NotBlank(message = "이름을 입력해주세요.")
    private String name;

    @NotNull(message = "역할을 선택해주세요.")
    private Role role;

    private School school;

    private String schoolNumber;
}
