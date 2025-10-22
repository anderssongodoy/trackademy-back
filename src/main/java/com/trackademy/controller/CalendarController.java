package com.trackademy.controller;

import com.trackademy.dto.CalendarEventDTO;
import com.trackademy.entity.User;
import com.trackademy.repository.UserRepository;
import com.trackademy.service.CalendarService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/student/calendar")
public class CalendarController {

    private final CalendarService calendarService;
    private final UserRepository userRepository;

    public CalendarController(CalendarService calendarService, UserRepository userRepository) {
        this.calendarService = calendarService;
        this.userRepository = userRepository;
    }

    @GetMapping
    public ResponseEntity<List<CalendarEventDTO>> getEvents(Authentication authentication,
                                                            @RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
                                                            @RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        String email = authentication != null ? authentication.getName() : null;
        if (email == null) return ResponseEntity.status(401).build();
        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(calendarService.getEventsForUser(user.getId(), start, end));
    }

    @PostMapping
    public ResponseEntity<CalendarEventDTO> createEvent(Authentication authentication, @RequestBody CalendarEventDTO dto) {
        String email = authentication != null ? authentication.getName() : null;
        if (email == null) return ResponseEntity.status(401).build();
        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null) return ResponseEntity.notFound().build();
        dto.setUserId(user.getId());
        return ResponseEntity.ok(calendarService.createEvent(dto));
    }
}
