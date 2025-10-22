package com.trackademy.service;

import com.trackademy.dto.CourseDTO;

import java.util.List;

public interface RecommendationService {
    List<CourseDTO> recommendCoursesForUser(Long userId, int limit);
}
