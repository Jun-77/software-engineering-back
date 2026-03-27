package com.example.EduManager.domain.teacher.repository;

import com.example.EduManager.domain.teacher.entity.TeacherProfile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeacherProfileRepository extends JpaRepository<TeacherProfile, Long> {
}
