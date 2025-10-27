package com.trackademy.service;

import com.trackademy.dto.OnboardingRequest;
import com.trackademy.dto.UsuarioCursoResumenDto;

import java.util.List;

public interface OnboardingService {
    List<UsuarioCursoResumenDto> onboard(String userSubject, String email, OnboardingRequest request);
}

