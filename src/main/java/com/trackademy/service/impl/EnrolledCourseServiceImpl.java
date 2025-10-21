package com.trackademy.service.impl;

import com.trackademy.dto.EnrolledCourseDTO;
import com.trackademy.entity.EnrolledCourse;
import com.trackademy.repository.EnrolledCourseRepository;
import com.trackademy.repository.EnrollmentRepository;
import com.trackademy.repository.CourseRepository;
import com.trackademy.repository.TermRepository;
import com.trackademy.repository.CurriculumCourseRepository;
import com.trackademy.service.EnrolledCourseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class EnrolledCourseServiceImpl implements EnrolledCourseService {
    
    private static final Logger logger = LoggerFactory.getLogger(EnrolledCourseServiceImpl.class);
    private final EnrolledCourseRepository enrolledCourseRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final CourseRepository courseRepository;
    private final TermRepository termRepository;
    private final CurriculumCourseRepository curriculumCourseRepository;
    
    public EnrolledCourseServiceImpl(EnrolledCourseRepository enrolledCourseRepository,
                                   EnrollmentRepository enrollmentRepository,
                                   CourseRepository courseRepository,
                                   TermRepository termRepository,
                                   CurriculumCourseRepository curriculumCourseRepository) {
        this.enrolledCourseRepository = enrolledCourseRepository;
        this.enrollmentRepository = enrollmentRepository;
        this.courseRepository = courseRepository;
        this.termRepository = termRepository;
        this.curriculumCourseRepository = curriculumCourseRepository;
    }
    
    @Override
    public EnrolledCourseDTO createEnrolledCourse(EnrolledCourseDTO enrolledCourseDTO) {
        logger.info("Creating enrolled course for enrollment: {}", enrolledCourseDTO.getEnrollmentId());
        
        EnrolledCourse enrolledCourse = EnrolledCourse.builder()
                .isCarryover(enrolledCourseDTO.getIsCarryover() != null ? enrolledCourseDTO.getIsCarryover() : false)
                .isConvalidation(enrolledCourseDTO.getIsConvalidation() != null ? enrolledCourseDTO.getIsConvalidation() : false)
                .isElective(enrolledCourseDTO.getIsElective() != null ? enrolledCourseDTO.getIsElective() : false)
                .isRemedial(enrolledCourseDTO.getIsRemedial() != null ? enrolledCourseDTO.getIsRemedial() : false)
                .status(enrolledCourseDTO.getStatus() != null ? enrolledCourseDTO.getStatus() : "enrolled")
                .grade(enrolledCourseDTO.getGrade())
                .attendancePercentage(enrolledCourseDTO.getAttendancePercentage())
                .build();
        
        if (enrolledCourseDTO.getEnrollmentId() != null) {
            enrolledCourse.setEnrollment(enrollmentRepository.findById(enrolledCourseDTO.getEnrollmentId())
                    .orElseThrow(() -> new RuntimeException("Enrollment not found")));
        }
        
        if (enrolledCourseDTO.getCourseId() != null) {
            enrolledCourse.setCourse(courseRepository.findById(enrolledCourseDTO.getCourseId())
                    .orElseThrow(() -> new RuntimeException("Course not found")));
        }
        
        if (enrolledCourseDTO.getTermId() != null) {
            enrolledCourse.setTerm(termRepository.findById(enrolledCourseDTO.getTermId())
                    .orElseThrow(() -> new RuntimeException("Term not found")));
        }
        
        if (enrolledCourseDTO.getCurriculumCourseId() != null) {
            enrolledCourse.setCurriculumCourse(curriculumCourseRepository.findById(enrolledCourseDTO.getCurriculumCourseId())
                    .orElseThrow(() -> new RuntimeException("Curriculum course not found")));
        }
        
        EnrolledCourse saved = enrolledCourseRepository.save(enrolledCourse);
        logger.info("Enrolled course created with ID: {}", saved.getId());
        
        return mapToDTO(saved);
    }
    
    @Override
    public Optional<EnrolledCourseDTO> getEnrolledCourseById(Long id) {
        logger.debug("Fetching enrolled course by ID: {}", id);
        return enrolledCourseRepository.findById(id).map(this::mapToDTO);
    }
    
    @Override
    public List<EnrolledCourseDTO> getEnrolledCoursesByEnrollment(Long enrollmentId) {
        logger.debug("Fetching enrolled courses for enrollment: {}", enrollmentId);
        return enrolledCourseRepository.findByEnrollmentId(enrollmentId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public Optional<EnrolledCourseDTO> getEnrolledCourseByEnrollmentAndCourse(Long enrollmentId, Long courseId, Long termId) {
        logger.debug("Fetching enrolled course for enrollment: {}, course: {}, term: {}", enrollmentId, courseId, termId);
        return enrolledCourseRepository.findByEnrollmentIdAndCourseIdAndTermId(enrollmentId, courseId, termId)
                .map(this::mapToDTO);
    }
    
    @Override
    public List<EnrolledCourseDTO> getEnrolledCoursesByTerm(Long termId) {
        logger.debug("Fetching enrolled courses for term: {}", termId);
        return enrolledCourseRepository.findByTermId(termId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public EnrolledCourseDTO updateEnrolledCourse(Long id, EnrolledCourseDTO enrolledCourseDTO) {
        logger.info("Updating enrolled course with ID: {}", id);
        
        EnrolledCourse enrolledCourse = enrolledCourseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Enrolled course not found"));
        
        enrolledCourse.setIsCarryover(enrolledCourseDTO.getIsCarryover());
        enrolledCourse.setIsConvalidation(enrolledCourseDTO.getIsConvalidation());
        enrolledCourse.setIsElective(enrolledCourseDTO.getIsElective());
        enrolledCourse.setIsRemedial(enrolledCourseDTO.getIsRemedial());
        enrolledCourse.setStatus(enrolledCourseDTO.getStatus());
        enrolledCourse.setGrade(enrolledCourseDTO.getGrade());
        enrolledCourse.setAttendancePercentage(enrolledCourseDTO.getAttendancePercentage());
        
        EnrolledCourse updated = enrolledCourseRepository.save(enrolledCourse);
        logger.info("Enrolled course updated successfully");
        
        return mapToDTO(updated);
    }
    
    @Override
    public void deleteEnrolledCourse(Long id) {
        logger.info("Deleting enrolled course with ID: {}", id);
        enrolledCourseRepository.deleteById(id);
        logger.info("Enrolled course deleted successfully");
    }
    
    private EnrolledCourseDTO mapToDTO(EnrolledCourse enrolledCourse) {
        return EnrolledCourseDTO.builder()
                .id(enrolledCourse.getId())
                .enrollmentId(enrolledCourse.getEnrollment() != null ? enrolledCourse.getEnrollment().getId() : null)
                .courseId(enrolledCourse.getCourse() != null ? enrolledCourse.getCourse().getId() : null)
                .termId(enrolledCourse.getTerm() != null ? enrolledCourse.getTerm().getId() : null)
                .curriculumCourseId(enrolledCourse.getCurriculumCourse() != null ? enrolledCourse.getCurriculumCourse().getId() : null)
                .isCarryover(enrolledCourse.getIsCarryover())
                .isConvalidation(enrolledCourse.getIsConvalidation())
                .isElective(enrolledCourse.getIsElective())
                .isRemedial(enrolledCourse.getIsRemedial())
                .status(enrolledCourse.getStatus())
                .grade(enrolledCourse.getGrade())
                .attendancePercentage(enrolledCourse.getAttendancePercentage())
                .build();
    }
}
