package com.trackademy.controller;

import com.trackademy.dto.EnrollmentDTO;
import com.trackademy.service.EnrollmentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/enrollments")
public class EnrollmentController {
    
    private final EnrollmentService enrollmentService;
    
    public EnrollmentController(EnrollmentService enrollmentService) {
        this.enrollmentService = enrollmentService;
    }
    
    @PostMapping
    public ResponseEntity<EnrollmentDTO> createEnrollment(@RequestBody EnrollmentDTO enrollmentDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(enrollmentService.createEnrollment(enrollmentDTO));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<EnrollmentDTO> getEnrollment(@PathVariable Long id) {
        return enrollmentService.getEnrollmentById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/user/{userId}/term/{termId}")
    public ResponseEntity<EnrollmentDTO> getEnrollmentByUserAndTerm(@PathVariable Long userId, @PathVariable Long termId) {
        return enrollmentService.getEnrollmentByUserAndTerm(userId, termId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<EnrollmentDTO>> getEnrollmentsByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(enrollmentService.getEnrollmentsByUser(userId));
    }
    
    @GetMapping("/term/{termId}")
    public ResponseEntity<List<EnrollmentDTO>> getEnrollmentsByTerm(@PathVariable Long termId) {
        return ResponseEntity.ok(enrollmentService.getEnrollmentsByTerm(termId));
    }
    
    @GetMapping("/program/{programId}")
    public ResponseEntity<List<EnrollmentDTO>> getEnrollmentsByProgram(@PathVariable Long programId) {
        return ResponseEntity.ok(enrollmentService.getEnrollmentsByProgram(programId));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<EnrollmentDTO> updateEnrollment(@PathVariable Long id, @RequestBody EnrollmentDTO enrollmentDTO) {
        return ResponseEntity.ok(enrollmentService.updateEnrollment(id, enrollmentDTO));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEnrollment(@PathVariable Long id) {
        enrollmentService.deleteEnrollment(id);
        return ResponseEntity.noContent().build();
    }
}
