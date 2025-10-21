package com.trackademy.service;

import com.trackademy.dto.EnrollmentDTO;

import java.util.List;
import java.util.Optional;

public interface EnrollmentService {
    EnrollmentDTO createEnrollment(EnrollmentDTO enrollmentDTO);
    Optional<EnrollmentDTO> getEnrollmentById(Long id);
    Optional<EnrollmentDTO> getEnrollmentByUserAndTerm(Long userId, Long termId);
    List<EnrollmentDTO> getEnrollmentsByUser(Long userId);
    List<EnrollmentDTO> getEnrollmentsByTerm(Long termId);
    List<EnrollmentDTO> getEnrollmentsByProgram(Long programId);
    EnrollmentDTO updateEnrollment(Long id, EnrollmentDTO enrollmentDTO);
    void deleteEnrollment(Long id);
}
