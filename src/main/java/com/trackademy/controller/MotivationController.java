package com.trackademy.controller;

import com.trackademy.dto.MotivationDTO;
import com.trackademy.entity.User;
import com.trackademy.repository.UserRepository;
import com.trackademy.service.MotivationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/student/motivations")
public class MotivationController {

    private final MotivationService motivationService;
    private final UserRepository userRepository;

    public MotivationController(MotivationService motivationService, UserRepository userRepository) {
        this.motivationService = motivationService;
        this.userRepository = userRepository;
    }

    @GetMapping
    public ResponseEntity<List<MotivationDTO>> getMyMotivations(Authentication authentication) {
        String email = authentication != null ? authentication.getName() : null;
        if (email == null) return ResponseEntity.status(401).build();
        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(motivationService.getMotivationsForUser(user.getId()));
    }

    @PostMapping
    public ResponseEntity<MotivationDTO> createMotivation(Authentication authentication, @RequestBody MotivationDTO dto) {
        String email = authentication != null ? authentication.getName() : null;
        if (email == null) return ResponseEntity.status(401).build();
        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null) return ResponseEntity.notFound().build();
        dto.setUserId(user.getId());
        return ResponseEntity.ok(motivationService.createMotivation(dto));
    }

    @GetMapping("/ranking")
    public ResponseEntity<?> getRanking(Authentication authentication) {
        String email = authentication != null ? authentication.getName() : null;
        if (email == null) return ResponseEntity.status(401).build();
        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null) return ResponseEntity.notFound().build();
        int score = motivationService.computeSimpleScore(user.getId());
        int count = motivationService.getMotivationsForUser(user.getId()).size();
        return ResponseEntity.ok(java.util.Map.of("score", score, "count", count));
    }
}
