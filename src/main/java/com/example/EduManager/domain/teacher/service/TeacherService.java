package com.example.EduManager.domain.teacher.service;

import com.example.EduManager.domain.teacher.entity.TeacherProfile;
import com.example.EduManager.domain.teacher.repository.TeacherProfileRepository;
import com.example.EduManager.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TeacherService {

    private final TeacherProfileRepository teacherProfileRepository;

    public TeacherProfile createProfile(User user, int grade, int classNum) {
        return teacherProfileRepository.save(TeacherProfile.of(user, grade, classNum));
    }
}
