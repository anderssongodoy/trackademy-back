package com.trackademy.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TermDTO {
    private Long id;
    private Long institutionId;
    private String code;
    private String name;
    private Integer year;
    private Integer cycleNumber;
    private String periodRaw;
    private String fullName;
    private LocalDate startDate;
    private LocalDate endDate;
    private Boolean isActive;
}
