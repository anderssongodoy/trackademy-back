package com.trackademy.controller;

import com.trackademy.dto.EnrolledCourseDTO;
import com.trackademy.service.EnrolledCourseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/enrolled-courses")
public class EnrolledCourseController {
    
    private final EnrolledCourseService enrolledCourseService;
    
    public EnrolledCourseController(EnrolledCourseService enrolledCourseService) {
        this.enrolledCourseService = enrolledCourseService;
    }
    
    @PostMapping
    public ResponseEntity<EnrolledCourseDTO> createEnrolledCourse(@RequestBody EnrolledCourseDTO enrolledCourseDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(enrolledCourseService.createEnrolledCourse(enrolledCourseDTO));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<EnrolledCourseDTO> getEnrolledCourse(@PathVariable Long id) {
        return enrolledCourseService.getEnrolledCourseById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/enrollment/{enrollmentId}")
    public ResponseEntity<List<EnrolledCourseDTO>> getEnrolledCoursesByEnrollment(@PathVariable Long enrollmentId) {
        return ResponseEntity.ok(enrolledCourseService.getEnrolledCoursesByEnrollment(enrollmentId));
    }
    
    @GetMapping("/enrollment/{enrollmentId}/course/{courseId}/term/{termId}")
    public ResponseEntity<EnrolledCourseDTO> getEnrolledCourseByEnrollmentAndCourse(
            @PathVariable Long enrollmentId, 
            @PathVariable Long courseId, 
            @PathVariable Long termId) {
        return enrolledCourseService.getEnrolledCourseByEnrollmentAndCourse(enrollmentId, courseId, termId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/term/{termId}")
    public ResponseEntity<List<EnrolledCourseDTO>> getEnrolledCoursesByTerm(@PathVariable Long termId) {
        return ResponseEntity.ok(enrolledCourseService.getEnrolledCoursesByTerm(termId));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<EnrolledCourseDTO> updateEnrolledCourse(@PathVariable Long id, @RequestBody EnrolledCourseDTO enrolledCourseDTO) {
        return ResponseEntity.ok(enrolledCourseService.updateEnrolledCourse(id, enrolledCourseDTO));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEnrolledCourse(@PathVariable Long id) {
        enrolledCourseService.deleteEnrolledCourse(id);
        return ResponseEntity.noContent().build();
    }
}
