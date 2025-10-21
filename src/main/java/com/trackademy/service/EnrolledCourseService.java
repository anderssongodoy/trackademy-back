package com.trackademy.service;

import com.trackademy.dto.EnrolledCourseDTO;

import java.util.List;
import java.util.Optional;

public interface EnrolledCourseService {
    EnrolledCourseDTO createEnrolledCourse(EnrolledCourseDTO enrolledCourseDTO);
    Optional<EnrolledCourseDTO> getEnrolledCourseById(Long id);
    List<EnrolledCourseDTO> getEnrolledCoursesByEnrollment(Long enrollmentId);
    Optional<EnrolledCourseDTO> getEnrolledCourseByEnrollmentAndCourse(Long enrollmentId, Long courseId, Long termId);
    List<EnrolledCourseDTO> getEnrolledCoursesByTerm(Long termId);
    EnrolledCourseDTO updateEnrolledCourse(Long id, EnrolledCourseDTO enrolledCourseDTO);
    void deleteEnrolledCourse(Long id);
}
