package com.trackademy.controller;

import com.trackademy.dto.CourseDTO;
import com.trackademy.entity.User;
import com.trackademy.repository.UserRepository;
import com.trackademy.service.RecommendationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/student/recommendations")
public class RecommendationController {

    private final RecommendationService recommendationService;
    private final UserRepository userRepository;

    public RecommendationController(RecommendationService recommendationService, UserRepository userRepository) {
        this.recommendationService = recommendationService;
        this.userRepository = userRepository;
    }

    @GetMapping
    public ResponseEntity<List<CourseDTO>> recommend(Authentication authentication, @RequestParam(defaultValue = "5") int limit) {
        String email = authentication != null ? authentication.getName() : null;
        if (email == null) return ResponseEntity.status(401).build();
        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(recommendationService.recommendCoursesForUser(user.getId(), limit));
    }
}
