package com.trackademy.dto;

import java.util.List;

public record CursoDetailDto(
        Long id,
        String codigo,
        String nombre,
        Integer horasSemanales,
        String silaboDescripcion,
        List<UnidadDto> unidades,
        List<EvaluacionDto> evaluaciones,
        List<String> bibliografia,
        List<String> competencias,
        String notaPolitica
) {}
