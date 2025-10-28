package com.trackademy.dto;

import java.util.List;

public record LoginStatusDto(
        boolean needsOnboarding,
        List<String> missing,
        Long usuarioId,
        String subject,
        String email,
        Long campusId,
        Long periodoId,
        Long carreraId,
        int cursosCount
) {}

