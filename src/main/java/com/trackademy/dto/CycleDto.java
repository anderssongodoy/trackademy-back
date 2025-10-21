package com.trackademy.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para Ciclo Académico en el onboarding
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CycleDto {
    private Integer id;
    private String label;
    private String description;
}
