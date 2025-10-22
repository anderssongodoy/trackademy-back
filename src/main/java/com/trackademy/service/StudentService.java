package com.trackademy.service;

import com.trackademy.dto.AlertItemDTO;
import com.trackademy.dto.CalendarEventDTO;
import com.trackademy.dto.RecommendationDTO;
import com.trackademy.dto.StudentMeDTO;

import java.util.List;

public interface StudentService {
    StudentMeDTO getMe(String userEmail);
    List<AlertItemDTO> getAlerts(String userEmail);
    List<CalendarEventDTO> getCalendar(String userEmail, String start, String end);
    List<RecommendationDTO> getRecommendations(String userEmail, int limit);
}
