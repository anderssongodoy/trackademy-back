package com.trackademy.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CurriculumCourseDTO {
    private Long id;
    private Long curriculumVersionId;
    private Long courseId;
    private Integer cycleNumber;
    private Boolean isRequired;
    private Boolean isCorequisite;
    private Integer sequenceOrder;
}
