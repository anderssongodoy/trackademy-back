package com.trackademy.dto;

import jakarta.validation.constraints.NotNull;

import java.util.List;

public record OnboardingRequest(
        @NotNull Long campusId,
        @NotNull Long periodoId,
        @NotNull Long carreraId,
        @NotNull List<Long> cursoIds
) {}

