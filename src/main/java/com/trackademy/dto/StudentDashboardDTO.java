package com.trackademy.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentDashboardDTO {
    private StudentMeDTO me;
    private List<EnrollmentDTO> enrollments;
    private List<AlertItemDTO> alerts;
    private List<CalendarEventDTO> calendar;
    private List<RecommendationDTO> recommendations;
    private Map<String, Object> ranking;
}
