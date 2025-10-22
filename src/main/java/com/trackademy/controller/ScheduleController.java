package com.trackademy.controller;

import com.trackademy.dto.UserCourseScheduleDto;
import com.trackademy.service.ScheduleService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/schedule")
public class ScheduleController {

    private final ScheduleService scheduleService;

    public ScheduleController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    @GetMapping("/me")
    public ResponseEntity<List<UserCourseScheduleDto>> getMySchedule(Authentication authentication) {
        String userEmail = authentication.getName();
        return ResponseEntity.ok(scheduleService.getMyCurrentSchedule(userEmail));
    }
}
