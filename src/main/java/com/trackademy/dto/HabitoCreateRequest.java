package com.trackademy.dto;

import jakarta.validation.constraints.NotBlank;

public record HabitoCreateRequest(@NotBlank String nombre, String periodicidad) {}

