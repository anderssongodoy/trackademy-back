package com.trackademy.service;

import com.trackademy.dto.CourseScheduleDTO;

import java.util.List;
import java.util.Optional;

public interface CourseScheduleService {
    CourseScheduleDTO createCourseSchedule(CourseScheduleDTO courseScheduleDTO);
    Optional<CourseScheduleDTO> getCourseScheduleById(Long id);
    List<CourseScheduleDTO> getCourseSchedulesByCourseAndTerm(Long courseId, Long termId);
    List<CourseScheduleDTO> getCourseSchedulesByCampusAndTerm(Long campusId, Long termId);
    List<CourseScheduleDTO> getCourseSchedulesByTerm(Long termId);
    CourseScheduleDTO updateCourseSchedule(Long id, CourseScheduleDTO courseScheduleDTO);
    void deleteCourseSchedule(Long id);
}
