package com.trackademy.dto;

import jakarta.validation.constraints.NotBlank;

public record NotaRequest(@NotBlank String nota) {}

