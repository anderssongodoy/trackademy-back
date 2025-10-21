package com.trackademy.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EnrollmentHistoryDTO {
    private Long id;
    private Long enrollmentId;
    private String action;
    private String previousStatus;
    private String newStatus;
    private Long changedBy;
    private String changeReason;
    private LocalDateTime changedAt;
}
