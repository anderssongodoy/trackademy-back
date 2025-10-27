package com.trackademy.dto;

import java.math.BigDecimal;

public record EvaluacionDto(Long id, String codigo, String descripcion, Integer semana, BigDecimal porcentaje) {}
