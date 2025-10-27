package com.trackademy.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record UsuarioEvaluacionDto(Long id, String codigo, String descripcion, Integer semana, BigDecimal porcentaje, LocalDate fechaEstimada, String nota) {}
