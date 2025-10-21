package com.trackademy.controller;

import com.trackademy.dto.OnboardingRequestDto;
import com.trackademy.dto.OnboardingResponseDto;
import com.trackademy.service.OnboardingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/onboarding")
public class OnboardingController {

    @Autowired
    private OnboardingService onboardingService;

    @PostMapping("/submit")
    public ResponseEntity<OnboardingResponseDto> submitOnboarding(
            @RequestBody OnboardingRequestDto request,
            Authentication authentication) {
        
        String userEmail = authentication.getName();
        OnboardingResponseDto response = onboardingService.saveOnboarding(request, userEmail);
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/me")
    public ResponseEntity<OnboardingRequestDto> getMyOnboarding(Authentication authentication) {
        String userEmail = authentication.getName();
        OnboardingRequestDto onboarding = onboardingService.getOnboarding(userEmail);
        
        return ResponseEntity.ok(onboarding);
    }

    @PatchMapping("/me")
    public ResponseEntity<OnboardingResponseDto> updateOnboarding(
            @RequestBody OnboardingRequestDto request,
            Authentication authentication) {
        
        String userEmail = authentication.getName();
        OnboardingResponseDto response = onboardingService.updateOnboarding(request, userEmail);
        
        return ResponseEntity.ok(response);
    }
}