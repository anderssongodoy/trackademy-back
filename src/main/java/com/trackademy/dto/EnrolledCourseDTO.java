package com.trackademy.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EnrolledCourseDTO {
    private Long id;
    private Long enrollmentId;
    private Long courseId;
    private Long termId;
    private Long curriculumCourseId;
    private Boolean isCarryover;
    private Boolean isConvalidation;
    private Boolean isElective;
    private Boolean isRemedial;
    private String status;
    private BigDecimal grade;
    private BigDecimal attendancePercentage;
}
