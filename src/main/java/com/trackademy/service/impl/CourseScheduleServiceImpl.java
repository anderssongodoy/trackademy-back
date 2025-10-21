package com.trackademy.service.impl;

import com.trackademy.dto.CourseScheduleDTO;
import com.trackademy.entity.CourseSchedule;
import com.trackademy.repository.CourseScheduleRepository;
import com.trackademy.repository.CourseRepository;
import com.trackademy.repository.TermRepository;
import com.trackademy.repository.CampusRepository;
import com.trackademy.service.CourseScheduleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class CourseScheduleServiceImpl implements CourseScheduleService {
    
    private static final Logger logger = LoggerFactory.getLogger(CourseScheduleServiceImpl.class);
    private final CourseScheduleRepository courseScheduleRepository;
    private final CourseRepository courseRepository;
    private final TermRepository termRepository;
    private final CampusRepository campusRepository;
    
    public CourseScheduleServiceImpl(CourseScheduleRepository courseScheduleRepository,
                                   CourseRepository courseRepository,
                                   TermRepository termRepository,
                                   CampusRepository campusRepository) {
        this.courseScheduleRepository = courseScheduleRepository;
        this.courseRepository = courseRepository;
        this.termRepository = termRepository;
        this.campusRepository = campusRepository;
    }
    
    @Override
    public CourseScheduleDTO createCourseSchedule(CourseScheduleDTO courseScheduleDTO) {
        logger.info("Creating course schedule for course: {}", courseScheduleDTO.getCourseId());
        
        CourseSchedule courseSchedule = CourseSchedule.builder()
                .section(courseScheduleDTO.getSection())
                .dayOfWeek(courseScheduleDTO.getDayOfWeek())
                .startTime(courseScheduleDTO.getStartTime())
                .endTime(courseScheduleDTO.getEndTime())
                .classroom(courseScheduleDTO.getClassroom())
                .building(courseScheduleDTO.getBuilding())
                .teacherName(courseScheduleDTO.getTeacherName())
                .modality(courseScheduleDTO.getModality())
                .meetingUrl(courseScheduleDTO.getMeetingUrl())
                .maxCapacity(courseScheduleDTO.getMaxCapacity())
                .currentEnrollment(courseScheduleDTO.getCurrentEnrollment() != null ? courseScheduleDTO.getCurrentEnrollment() : 0)
                .build();
        
        if (courseScheduleDTO.getCourseId() != null) {
            courseSchedule.setCourse(courseRepository.findById(courseScheduleDTO.getCourseId())
                    .orElseThrow(() -> new RuntimeException("Course not found")));
        }
        
        if (courseScheduleDTO.getTermId() != null) {
            courseSchedule.setTerm(termRepository.findById(courseScheduleDTO.getTermId())
                    .orElseThrow(() -> new RuntimeException("Term not found")));
        }
        
        if (courseScheduleDTO.getCampusId() != null) {
            courseSchedule.setCampus(campusRepository.findById(courseScheduleDTO.getCampusId())
                    .orElseThrow(() -> new RuntimeException("Campus not found")));
        }
        
        CourseSchedule saved = courseScheduleRepository.save(courseSchedule);
        logger.info("Course schedule created with ID: {}", saved.getId());
        
        return mapToDTO(saved);
    }
    
    @Override
    public Optional<CourseScheduleDTO> getCourseScheduleById(Long id) {
        logger.debug("Fetching course schedule by ID: {}", id);
        return courseScheduleRepository.findById(id).map(this::mapToDTO);
    }
    
    @Override
    public List<CourseScheduleDTO> getCourseSchedulesByCourseAndTerm(Long courseId, Long termId) {
        logger.debug("Fetching course schedules for course: {} and term: {}", courseId, termId);
        return courseScheduleRepository.findByCourseIdAndTermId(courseId, termId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<CourseScheduleDTO> getCourseSchedulesByCampusAndTerm(Long campusId, Long termId) {
        logger.debug("Fetching course schedules for campus: {} and term: {}", campusId, termId);
        return courseScheduleRepository.findByCampusIdAndTermId(campusId, termId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<CourseScheduleDTO> getCourseSchedulesByTerm(Long termId) {
        logger.debug("Fetching course schedules for term: {}", termId);
        return courseScheduleRepository.findByTermId(termId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public CourseScheduleDTO updateCourseSchedule(Long id, CourseScheduleDTO courseScheduleDTO) {
        logger.info("Updating course schedule with ID: {}", id);
        
        CourseSchedule courseSchedule = courseScheduleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Course schedule not found"));
        
        courseSchedule.setSection(courseScheduleDTO.getSection());
        courseSchedule.setDayOfWeek(courseScheduleDTO.getDayOfWeek());
        courseSchedule.setStartTime(courseScheduleDTO.getStartTime());
        courseSchedule.setEndTime(courseScheduleDTO.getEndTime());
        courseSchedule.setClassroom(courseScheduleDTO.getClassroom());
        courseSchedule.setBuilding(courseScheduleDTO.getBuilding());
        courseSchedule.setTeacherName(courseScheduleDTO.getTeacherName());
        courseSchedule.setModality(courseScheduleDTO.getModality());
        courseSchedule.setMeetingUrl(courseScheduleDTO.getMeetingUrl());
        courseSchedule.setMaxCapacity(courseScheduleDTO.getMaxCapacity());
        courseSchedule.setCurrentEnrollment(courseScheduleDTO.getCurrentEnrollment());
        
        CourseSchedule updated = courseScheduleRepository.save(courseSchedule);
        logger.info("Course schedule updated successfully");
        
        return mapToDTO(updated);
    }
    
    @Override
    public void deleteCourseSchedule(Long id) {
        logger.info("Deleting course schedule with ID: {}", id);
        courseScheduleRepository.deleteById(id);
        logger.info("Course schedule deleted successfully");
    }
    
    private CourseScheduleDTO mapToDTO(CourseSchedule courseSchedule) {
        return CourseScheduleDTO.builder()
                .id(courseSchedule.getId())
                .courseId(courseSchedule.getCourse() != null ? courseSchedule.getCourse().getId() : null)
                .termId(courseSchedule.getTerm() != null ? courseSchedule.getTerm().getId() : null)
                .campusId(courseSchedule.getCampus() != null ? courseSchedule.getCampus().getId() : null)
                .section(courseSchedule.getSection())
                .dayOfWeek(courseSchedule.getDayOfWeek())
                .startTime(courseSchedule.getStartTime())
                .endTime(courseSchedule.getEndTime())
                .classroom(courseSchedule.getClassroom())
                .building(courseSchedule.getBuilding())
                .teacherName(courseSchedule.getTeacherName())
                .modality(courseSchedule.getModality())
                .meetingUrl(courseSchedule.getMeetingUrl())
                .maxCapacity(courseSchedule.getMaxCapacity())
                .currentEnrollment(courseSchedule.getCurrentEnrollment())
                .build();
    }
}
