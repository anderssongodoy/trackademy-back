package com.trackademy.service;

import com.trackademy.dto.CourseDTO;

import java.util.List;
import java.util.Optional;

public interface CourseService {
    CourseDTO createCourse(CourseDTO courseDTO);
    Optional<CourseDTO> getCourseById(Long id);
    Optional<CourseDTO> getCourseByCode(String code);
    List<CourseDTO> getAllCourses();
    CourseDTO updateCourse(Long id, CourseDTO courseDTO);
    void deleteCourse(Long id);
}
