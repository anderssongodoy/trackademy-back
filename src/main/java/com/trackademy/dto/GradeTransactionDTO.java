package com.trackademy.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GradeTransactionDTO {
    private Long id;
    private Long enrolledCourseId;
    private BigDecimal oldGrade;
    private BigDecimal newGrade;
    private String changeReason;
    private Long changedBy;
    private LocalDateTime changedAt;
}
