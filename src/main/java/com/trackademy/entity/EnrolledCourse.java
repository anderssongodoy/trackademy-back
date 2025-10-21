package com.trackademy.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Entity
@Table(name = "enrolled_course")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EnrolledCourse {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "enrollment_id", nullable = false)
    private Enrollment enrollment;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "term_id", nullable = false)
    private Term term;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "curriculum_course_id")
    private CurriculumCourse curriculumCourse;
    
    private Boolean isCarryover;
    
    private Boolean isConvalidation;
    
    private Boolean isElective;
    
    private Boolean isRemedial;
    
    private String status;
    
    private BigDecimal grade;
    
    private BigDecimal attendancePercentage;
}
