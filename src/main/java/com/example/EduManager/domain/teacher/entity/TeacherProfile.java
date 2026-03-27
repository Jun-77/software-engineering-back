package com.example.EduManager.domain.teacher.entity;

import com.example.EduManager.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "teacher_profiles")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TeacherProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(nullable = false)
    private int grade;

    @Column(name = "class_num", nullable = false)
    private int classNum;

    private TeacherProfile(User user, int grade, int classNum) {
        this.user = user;
        this.grade = grade;
        this.classNum = classNum;
    }

    public static TeacherProfile of(User user, int grade, int classNum) {
        return new TeacherProfile(user, grade, classNum);
    }

    public void update(int grade, int classNum) {
        this.grade = grade;
        this.classNum = classNum;
    }
}
