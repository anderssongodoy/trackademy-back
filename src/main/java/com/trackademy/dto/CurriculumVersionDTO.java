package com.trackademy.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CurriculumVersionDTO {
    private Long id;
    private Long programId;
    private Integer versionNumber;
    private Integer academicYearStart;
    private String name;
    private String description;
    private Boolean isActive;
}
