package com.trackademy.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "student_course_schedule")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentCourseSchedule {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "enrolled_course_id", nullable = false)
    private EnrolledCourse enrolledCourse;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_schedule_id", nullable = false)
    private CourseSchedule courseSchedule;
    
    private Boolean isActive;
}
