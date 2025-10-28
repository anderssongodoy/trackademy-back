package com.trackademy.web;

import com.trackademy.dto.OnboardingRequest;
import com.trackademy.dto.UsuarioCursoResumenDto;
import com.trackademy.service.OnboardingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class OnboardingController {

    private final OnboardingService onboardingService;

    @PostMapping("/onboarding")
    public List<UsuarioCursoResumenDto> onboarding(@AuthenticationPrincipal Jwt jwt,
                                                   @Valid @RequestBody OnboardingRequest request,
                                                   @RequestHeader(value = "X-User-Avatar", required = false) String avatar) {
        String sub = jwt.getSubject();
        String email = jwt.getClaimAsString("preferred_username");
        if (email == null) email = jwt.getClaimAsString("email");
        String nombre = jwt.getClaimAsString("name");
        if (avatar == null) avatar = jwt.getClaimAsString("picture");
        return onboardingService.onboard(sub, email, nombre, avatar, request);
    }
}
