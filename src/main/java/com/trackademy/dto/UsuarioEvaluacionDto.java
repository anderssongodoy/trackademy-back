package com.trackademy.dto;

import java.time.LocalDate;

public record UsuarioEvaluacionDto(Long id, String codigo, String nombre, Integer semana, Integer porcentaje, LocalDate fechaEstimada, String nota) {}

