package com.trackademy.controller;

import com.trackademy.dto.EnrollmentDTO;
import com.trackademy.service.EnrollmentService;
import com.trackademy.repository.UserRepository;
import com.trackademy.entity.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/enrollments")
public class EnrollmentController {
    
    private final EnrollmentService enrollmentService;
    private final UserRepository userRepository;
    
    public EnrollmentController(EnrollmentService enrollmentService, UserRepository userRepository) {
        this.enrollmentService = enrollmentService;
        this.userRepository = userRepository;
    }
    
    @PostMapping
    public ResponseEntity<EnrollmentDTO> createEnrollment(Authentication authentication, @RequestBody EnrollmentDTO enrollmentDTO) {
        // If the request is authenticated, force the enrollment to be created for the authenticated user
        if (authentication != null) {
            String email = authentication.getName();
            if (email != null && !email.isBlank()) {
                Optional<User> userOpt = userRepository.findByEmail(email);
                if (userOpt.isEmpty()) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
                }
                enrollmentDTO.setUserId(userOpt.get().getId());
            }
        }
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

    @GetMapping("/me")
    public ResponseEntity<List<EnrollmentDTO>> getMyEnrollments(Authentication authentication) {
        String email = authentication != null ? authentication.getName() : null;
        if (email == null || email.isBlank()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(enrollmentService.getEnrollmentsByUser(user.get().getId()));
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
