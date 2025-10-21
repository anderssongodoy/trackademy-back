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
public class TermEnrollmentWindowDTO {
    private Long id;
    private Long termId;
    private Long campusId;
    private Long programId;
    private LocalDate enrollmentStartDate;
    private LocalDate enrollmentEndDate;
    private LocalDate addDropDeadline;
    private Integer minCycleToEnroll;
    private Integer maxCycleToEnroll;
}
