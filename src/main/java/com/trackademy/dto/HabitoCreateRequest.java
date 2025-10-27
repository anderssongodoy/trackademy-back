package com.trackademy.dto;

import jakarta.validation.constraints.NotBlank;

public record HabitoCreateRequest(@NotBlank String titulo, String frecuencia) {}
