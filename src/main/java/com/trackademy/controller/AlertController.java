package com.trackademy.controller;

import com.trackademy.dto.AlertItemDTO;
import com.trackademy.entity.User;
import com.trackademy.repository.UserRepository;
import com.trackademy.service.AlertService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/student/alerts")
public class AlertController {

    private final AlertService alertService;
    private final UserRepository userRepository;

    public AlertController(AlertService alertService, UserRepository userRepository) {
        this.alertService = alertService;
        this.userRepository = userRepository;
    }

    @GetMapping
    public ResponseEntity<List<AlertItemDTO>> getMyAlerts(Authentication authentication) {
        String email = authentication != null ? authentication.getName() : null;
        if (email == null) return ResponseEntity.status(401).build();
        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(alertService.getAlertsForUser(user.getId()));
    }

    @PostMapping
    public ResponseEntity<AlertItemDTO> createAlert(Authentication authentication, @RequestBody AlertItemDTO dto) {
        String email = authentication != null ? authentication.getName() : null;
        if (email == null) return ResponseEntity.status(401).build();
        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null) return ResponseEntity.notFound().build();
        dto.setUserId(user.getId());
        return ResponseEntity.ok(alertService.createAlert(dto));
    }

    @PostMapping("/{id}/read")
    public ResponseEntity<Void> markRead(Authentication authentication, @PathVariable Long id) {
        String email = authentication != null ? authentication.getName() : null;
        if (email == null) return ResponseEntity.status(401).build();
        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null) return ResponseEntity.notFound().build();
        alertService.markRead(id, user.getId());
        return ResponseEntity.noContent().build();
    }
}
