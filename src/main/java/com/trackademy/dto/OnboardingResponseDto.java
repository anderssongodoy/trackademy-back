package com.trackademy.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para responder después de guardar onboarding
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OnboardingResponseDto {
    private Boolean success;
    private String message;
    private String enrollmentId;
    private Long userId;
    private String campus;
    private Integer cycle;
}
