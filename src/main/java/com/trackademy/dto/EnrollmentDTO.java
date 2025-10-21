package com.trackademy.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EnrollmentDTO {
    private Long id;
    private Long userId;
    private Long termId;
    private Long campusId;
    private Long programId;
    private String studentCode;
    private Integer currentCycle;
    private Integer entryCycle;
    private Long expectedGraduationTermId;
    private String status;
    private LocalDate enrollmentDate;
    private LocalDate expectedGraduationDate;
    private BigDecimal gpa;
    private Integer totalCreditsCompleted;
    private Integer totalCreditsInProgress;
}
