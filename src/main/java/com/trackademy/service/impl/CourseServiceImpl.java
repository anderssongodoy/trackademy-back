package com.trackademy.service.impl;

import com.trackademy.dto.CourseDTO;
import com.trackademy.entity.Course;
import com.trackademy.repository.CourseRepository;
import com.trackademy.service.CourseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class CourseServiceImpl implements CourseService {
    
    private static final Logger logger = LoggerFactory.getLogger(CourseServiceImpl.class);
    private final CourseRepository courseRepository;
    
    public CourseServiceImpl(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }
    
    @Override
    public CourseDTO createCourse(CourseDTO courseDTO) {
        logger.info("Creating course: {} ({})", courseDTO.getName(), courseDTO.getCode());
        
        Course course = Course.builder()
                .code(courseDTO.getCode())
                .name(courseDTO.getName())
                .credits(courseDTO.getCredits())
                .weeklyHours(courseDTO.getWeeklyHours())
                .modality(courseDTO.getModality())
                .build();
        
        Course saved = courseRepository.save(course);
        logger.info("Course created with ID: {}", saved.getId());
        
        return mapToDTO(saved);
    }
    
    @Override
    public Optional<CourseDTO> getCourseById(Long id) {
        logger.debug("Fetching course by ID: {}", id);
        return courseRepository.findById(id).map(this::mapToDTO);
    }
    
    @Override
    public Optional<CourseDTO> getCourseByCode(String code) {
        logger.debug("Fetching course by code: {}", code);
        return courseRepository.findByCode(code).map(this::mapToDTO);
    }
    
    @Override
    public List<CourseDTO> getAllCourses() {
        logger.debug("Fetching all courses");
        return courseRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public CourseDTO updateCourse(Long id, CourseDTO courseDTO) {
        logger.info("Updating course with ID: {}", id);
        
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Course not found with ID: {}", id);
                    return new RuntimeException("Course not found");
                });
        
        course.setCode(courseDTO.getCode());
        course.setName(courseDTO.getName());
        course.setCredits(courseDTO.getCredits());
        course.setWeeklyHours(courseDTO.getWeeklyHours());
        course.setModality(courseDTO.getModality());
        
        Course updated = courseRepository.save(course);
        logger.info("Course updated successfully");
        
        return mapToDTO(updated);
    }
    
    @Override
    public void deleteCourse(Long id) {
        logger.info("Deleting course with ID: {}", id);
        courseRepository.deleteById(id);
        logger.info("Course deleted successfully");
    }
    
    private CourseDTO mapToDTO(Course course) {
        return CourseDTO.builder()
                .id(course.getId())
                .code(course.getCode())
                .name(course.getName())
                .credits(course.getCredits())
                .weeklyHours(course.getWeeklyHours())
                .modality(course.getModality())
                .build();
    }
}
