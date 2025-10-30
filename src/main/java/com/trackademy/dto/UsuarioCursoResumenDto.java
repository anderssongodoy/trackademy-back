package com.trackademy.dto;

import java.util.List;

public record UsuarioCursoResumenDto(Long usuarioCursoId, Long cursoId, String cursoNombre, List<UsuarioEvaluacionDto> evaluaciones) {}
