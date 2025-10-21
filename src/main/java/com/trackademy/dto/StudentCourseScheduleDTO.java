package com.trackademy.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentCourseScheduleDTO {
    private Long id;
    private Long enrolledCourseId;
    private Long courseScheduleId;
    private Boolean isActive;
}
