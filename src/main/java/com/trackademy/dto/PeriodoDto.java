package com.trackademy.dto;

import java.time.LocalDate;

public record PeriodoDto(Long id, String etiqueta, LocalDate fechaInicio, LocalDate fechaFin) {}

