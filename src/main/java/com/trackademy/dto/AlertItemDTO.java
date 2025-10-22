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
public class AlertItemDTO {
    private Long id;
    private Long userId;
    private String title;
    private String message;
    private String level; // INFO, WARNING, CRITICAL
    private Boolean read;
    private LocalDateTime createdAt;
}

