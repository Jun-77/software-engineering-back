package com.example.EduManager.domain.grade.entity;

import com.example.EduManager.domain.student.entity.StudentProfile;
import com.example.EduManager.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "grades",
        uniqueConstraints = @UniqueConstraint(name = "uq_grades", columnNames = {"student_id", "semester", "subject", "exam_type"}))
@EntityListeners(AuditingEntityListener.class)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Grade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private StudentProfile student;

    @Column(nullable = false, length = 10)
    private String semester;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private Subject subject;

    @Column(nullable = false)
    private int score;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 1)
    private GradeLevel grade;

    @Enumerated(EnumType.STRING)
    @Column(name = "exam_type", nullable = false, length = 10)
    private ExamType examType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teacher_id", nullable = false)
    private User teacher;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    public static Grade of(StudentProfile student, String semester, Subject subject,
                           int score, ExamType examType, User teacher) {
        Grade grade = new Grade();
        grade.student = student;
        grade.semester = semester;
        grade.subject = subject;
        grade.score = score;
        grade.grade = GradeLevel.from(score);
        grade.examType = examType;
        grade.teacher = teacher;
        return grade;
    }

    public void updateScore(int score) {
        this.score = score;
        this.grade = GradeLevel.from(score);
    }
}
