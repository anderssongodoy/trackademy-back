package com.trackademy.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProgramDTO {
    private Long id;
    private String slug;
    private String name;
    private Long institutionId;
    private Long campusId;
    private String modality;
}
