package com.trackademy.controller;

import com.trackademy.dto.*;
import com.trackademy.service.StudentService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/student")
public class StudentController {

    private final StudentService studentService;
    private final com.trackademy.service.EnrollmentService enrollmentService;
    private final com.trackademy.service.MotivationService motivationService;

    public StudentController(StudentService studentService, com.trackademy.service.EnrollmentService enrollmentService, com.trackademy.service.MotivationService motivationService) {
        this.studentService = studentService;
        this.enrollmentService = enrollmentService;
        this.motivationService = motivationService;
    }

    private String extractEmail(Authentication authentication) {
        if (authentication instanceof JwtAuthenticationToken jwt) {
            Object claim = jwt.getToken().getClaims().get("email");
            if (claim == null) claim = jwt.getToken().getClaims().get("preferred_username");
            if (claim != null) return claim.toString();
        }
        return authentication != null ? authentication.getName() : null;
    }

    @GetMapping("/me")
    public ResponseEntity<StudentMeDTO> me(Authentication authentication) {
        String email = extractEmail(authentication);
        if (email == null) return ResponseEntity.status(401).build();
        StudentMeDTO dto = studentService.getMe(email);
        if (dto == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(dto);
    }

    // Alerts handled by `AlertController` to avoid duplicate mappings

    // Calendar endpoints are exposed by CalendarController (avoid duplicate mappings)
    // Recommendations are served by RecommendationController to avoid duplicate mappings

    @GetMapping("/dashboard")
    public ResponseEntity<StudentDashboardDTO> dashboard(Authentication authentication,
                                                         @RequestParam(defaultValue = "10") int recLimit) {
        String email = extractEmail(authentication);
        if (email == null) return ResponseEntity.status(401).build();

        StudentMeDTO me = studentService.getMe(email);
        if (me == null) return ResponseEntity.notFound().build();

        List<EnrollmentDTO> enrollments = enrollmentService.getEnrollmentsByUser(me.getId());
        List<AlertItemDTO> alerts = studentService.getAlerts(email);
        // calendar: next 30 days
        java.time.LocalDateTime start = java.time.LocalDateTime.now().minusDays(1);
        java.time.LocalDateTime end = java.time.LocalDateTime.now().plusDays(30);
        List<CalendarEventDTO> calendar = studentService.getCalendar(email, start.toString(), end.toString());
        List<RecommendationDTO> recommendations = studentService.getRecommendations(email, recLimit);

        // ranking breakdown - simple: score and count
        java.util.Map<String, Object> ranking = new java.util.HashMap<>();
        try {
            int score = motivationService.computeSimpleScore(me.getId());
            int count = motivationService.getMotivationsForUser(me.getId()).size();
            ranking.put("score", score);
            ranking.put("count", count);
        } catch (Exception ignore) {
        }

        StudentDashboardDTO dto = StudentDashboardDTO.builder()
                .me(me)
                .enrollments(enrollments)
                .alerts(alerts)
                .calendar(calendar)
                .recommendations(recommendations)
                .ranking(ranking)
                .build();

        return ResponseEntity.ok(dto);
    }
}
