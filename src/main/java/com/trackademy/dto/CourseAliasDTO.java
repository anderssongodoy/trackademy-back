package com.trackademy.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseAliasDTO {
    private Long id;
    private Long courseId;
    private String aliasCode;
    private String aliasName;
    private Long institutionId;
}
