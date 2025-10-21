package com.trackademy.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProgramCampusDTO {
    private Long id;
    private Long programId;
    private Long campusId;
    private Boolean isActive;
}
