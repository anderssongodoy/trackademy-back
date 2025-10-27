package com.trackademy.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record RecordatorioPreferenciaRequest(@NotNull @Min(0) Integer anticipacionDias) {}

