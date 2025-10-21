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
public class CoursePrerequisiteDTO {
    private Long id;
    private Long courseId;
    private Long prerequisiteCourseId;
    private String prerequisiteType;
    private BigDecimal minGrade;
}
