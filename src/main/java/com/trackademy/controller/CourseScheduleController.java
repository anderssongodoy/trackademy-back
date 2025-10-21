package com.trackademy.controller;

import com.trackademy.dto.CourseScheduleDTO;
import com.trackademy.service.CourseScheduleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/course-schedules")
public class CourseScheduleController {
    
    private final CourseScheduleService courseScheduleService;
    
    public CourseScheduleController(CourseScheduleService courseScheduleService) {
        this.courseScheduleService = courseScheduleService;
    }
    
    @PostMapping
    public ResponseEntity<CourseScheduleDTO> createCourseSchedule(@RequestBody CourseScheduleDTO courseScheduleDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(courseScheduleService.createCourseSchedule(courseScheduleDTO));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<CourseScheduleDTO> getCourseSchedule(@PathVariable Long id) {
        return courseScheduleService.getCourseScheduleById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/course/{courseId}/term/{termId}")
    public ResponseEntity<List<CourseScheduleDTO>> getCourseSchedulesByCourseAndTerm(
            @PathVariable Long courseId, 
            @PathVariable Long termId) {
        return ResponseEntity.ok(courseScheduleService.getCourseSchedulesByCourseAndTerm(courseId, termId));
    }
    
    @GetMapping("/campus/{campusId}/term/{termId}")
    public ResponseEntity<List<CourseScheduleDTO>> getCourseSchedulesByCampusAndTerm(
            @PathVariable Long campusId, 
            @PathVariable Long termId) {
        return ResponseEntity.ok(courseScheduleService.getCourseSchedulesByCampusAndTerm(campusId, termId));
    }
    
    @GetMapping("/term/{termId}")
    public ResponseEntity<List<CourseScheduleDTO>> getCourseSchedulesByTerm(@PathVariable Long termId) {
        return ResponseEntity.ok(courseScheduleService.getCourseSchedulesByTerm(termId));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<CourseScheduleDTO> updateCourseSchedule(@PathVariable Long id, @RequestBody CourseScheduleDTO courseScheduleDTO) {
        return ResponseEntity.ok(courseScheduleService.updateCourseSchedule(id, courseScheduleDTO));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourseSchedule(@PathVariable Long id) {
        courseScheduleService.deleteCourseSchedule(id);
        return ResponseEntity.noContent().build();
    }
}
