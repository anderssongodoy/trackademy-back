package com.trackademy.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.math.BigDecimal;

@Entity
@Table(name = "grade_transaction")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GradeTransaction {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "enrolled_course_id", nullable = false)
    private EnrolledCourse enrolledCourse;
    
    private BigDecimal oldGrade;
    
    private BigDecimal newGrade;
    
    private String changeReason;
    
    private Long changedBy;
    
    private LocalDateTime changedAt;
}
