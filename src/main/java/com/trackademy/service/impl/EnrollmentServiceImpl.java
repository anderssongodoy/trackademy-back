package com.trackademy.service.impl;

import com.trackademy.dto.EnrollmentDTO;
import com.trackademy.entity.Enrollment;
import com.trackademy.repository.EnrollmentRepository;
import com.trackademy.repository.TermRepository;
import com.trackademy.repository.CampusRepository;
import com.trackademy.repository.ProgramRepository;
import com.trackademy.service.EnrollmentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class EnrollmentServiceImpl implements EnrollmentService {
    
    private static final Logger logger = LoggerFactory.getLogger(EnrollmentServiceImpl.class);
    private final EnrollmentRepository enrollmentRepository;
    private final TermRepository termRepository;
    private final CampusRepository campusRepository;
    private final ProgramRepository programRepository;
    
    public EnrollmentServiceImpl(EnrollmentRepository enrollmentRepository,
                               TermRepository termRepository,
                               CampusRepository campusRepository,
                               ProgramRepository programRepository) {
        this.enrollmentRepository = enrollmentRepository;
        this.termRepository = termRepository;
        this.campusRepository = campusRepository;
        this.programRepository = programRepository;
    }
    
    @Override
    public EnrollmentDTO createEnrollment(EnrollmentDTO enrollmentDTO) {
        logger.info("Creating enrollment for user: {} in term: {}", enrollmentDTO.getUserId(), enrollmentDTO.getTermId());
        
        Enrollment enrollment = Enrollment.builder()
                .userId(enrollmentDTO.getUserId())
                .studentCode(enrollmentDTO.getStudentCode())
                .currentCycle(enrollmentDTO.getCurrentCycle())
                .entryCycle(enrollmentDTO.getEntryCycle())
                .status(enrollmentDTO.getStatus() != null ? enrollmentDTO.getStatus() : "active")
                .enrollmentDate(enrollmentDTO.getEnrollmentDate())
                .expectedGraduationDate(enrollmentDTO.getExpectedGraduationDate())
                .gpa(enrollmentDTO.getGpa())
                .totalCreditsCompleted(enrollmentDTO.getTotalCreditsCompleted() != null ? enrollmentDTO.getTotalCreditsCompleted() : 0)
                .totalCreditsInProgress(enrollmentDTO.getTotalCreditsInProgress() != null ? enrollmentDTO.getTotalCreditsInProgress() : 0)
                .build();
        
        if (enrollmentDTO.getTermId() != null) {
            enrollment.setTerm(termRepository.findById(enrollmentDTO.getTermId())
                    .orElseThrow(() -> new RuntimeException("Term not found")));
        }
        
        if (enrollmentDTO.getCampusId() != null) {
            enrollment.setCampus(campusRepository.findById(enrollmentDTO.getCampusId())
                    .orElseThrow(() -> new RuntimeException("Campus not found")));
        }
        
        if (enrollmentDTO.getProgramId() != null) {
            enrollment.setProgram(programRepository.findById(enrollmentDTO.getProgramId())
                    .orElseThrow(() -> new RuntimeException("Program not found")));
        }
        
        Enrollment saved = enrollmentRepository.save(enrollment);
        logger.info("Enrollment created with ID: {}", saved.getId());
        
        return mapToDTO(saved);
    }
    
    @Override
    public Optional<EnrollmentDTO> getEnrollmentById(Long id) {
        logger.debug("Fetching enrollment by ID: {}", id);
        return enrollmentRepository.findById(id).map(this::mapToDTO);
    }
    
    @Override
    public Optional<EnrollmentDTO> getEnrollmentByUserAndTerm(Long userId, Long termId) {
        logger.debug("Fetching enrollment for user: {} and term: {}", userId, termId);
        return enrollmentRepository.findByUserIdAndTermId(userId, termId).map(this::mapToDTO);
    }
    
    @Override
    public List<EnrollmentDTO> getEnrollmentsByUser(Long userId) {
        logger.debug("Fetching enrollments for user: {}", userId);
        return enrollmentRepository.findByUserId(userId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<EnrollmentDTO> getEnrollmentsByTerm(Long termId) {
        logger.debug("Fetching enrollments for term: {}", termId);
        return enrollmentRepository.findByTermId(termId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<EnrollmentDTO> getEnrollmentsByProgram(Long programId) {
        logger.debug("Fetching enrollments for program: {}", programId);
        return enrollmentRepository.findByProgramId(programId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public EnrollmentDTO updateEnrollment(Long id, EnrollmentDTO enrollmentDTO) {
        logger.info("Updating enrollment with ID: {}", id);
        
        Enrollment enrollment = enrollmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Enrollment not found"));
        
        enrollment.setStudentCode(enrollmentDTO.getStudentCode());
        enrollment.setCurrentCycle(enrollmentDTO.getCurrentCycle());
        enrollment.setEntryCycle(enrollmentDTO.getEntryCycle());
        enrollment.setStatus(enrollmentDTO.getStatus());
        enrollment.setExpectedGraduationDate(enrollmentDTO.getExpectedGraduationDate());
        enrollment.setGpa(enrollmentDTO.getGpa());
        enrollment.setTotalCreditsCompleted(enrollmentDTO.getTotalCreditsCompleted());
        enrollment.setTotalCreditsInProgress(enrollmentDTO.getTotalCreditsInProgress());
        
        Enrollment updated = enrollmentRepository.save(enrollment);
        logger.info("Enrollment updated successfully");
        
        return mapToDTO(updated);
    }
    
    @Override
    public void deleteEnrollment(Long id) {
        logger.info("Deleting enrollment with ID: {}", id);
        enrollmentRepository.deleteById(id);
        logger.info("Enrollment deleted successfully");
    }
    
    private EnrollmentDTO mapToDTO(Enrollment enrollment) {
        return EnrollmentDTO.builder()
                .id(enrollment.getId())
                .userId(enrollment.getUserId())
                .termId(enrollment.getTerm() != null ? enrollment.getTerm().getId() : null)
                .campusId(enrollment.getCampus() != null ? enrollment.getCampus().getId() : null)
                .programId(enrollment.getProgram() != null ? enrollment.getProgram().getId() : null)
                .termCode(enrollment.getTerm() != null ? enrollment.getTerm().getCode() : null)
                .campusName(enrollment.getCampus() != null ? enrollment.getCampus().getName() : null)
                .programName(enrollment.getProgram() != null ? enrollment.getProgram().getName() : null)
                .studentCode(enrollment.getStudentCode())
                .currentCycle(enrollment.getCurrentCycle())
                .entryCycle(enrollment.getEntryCycle())
                .expectedGraduationTermId(enrollment.getExpectedGraduationTerm() != null ? enrollment.getExpectedGraduationTerm().getId() : null)
                .status(enrollment.getStatus())
                .enrollmentDate(enrollment.getEnrollmentDate())
                .expectedGraduationDate(enrollment.getExpectedGraduationDate())
                .gpa(enrollment.getGpa())
                .totalCreditsCompleted(enrollment.getTotalCreditsCompleted())
                .totalCreditsInProgress(enrollment.getTotalCreditsInProgress())
                .build();
    }
}
