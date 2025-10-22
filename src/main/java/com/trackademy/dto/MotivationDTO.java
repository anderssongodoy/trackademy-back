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
public class MotivationDTO {
    private Long id;
    private Long userId;
    private String text;
    private Integer score;
    private LocalDateTime createdAt;
}
