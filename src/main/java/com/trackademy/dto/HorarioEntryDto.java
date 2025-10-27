package com.trackademy.dto;

import jakarta.validation.constraints.*;

public record HorarioEntryDto(
        @NotNull Long usuarioCursoId,
        @NotNull @Min(1) @Max(7) Integer diaSemana,
        @NotBlank String horaInicio, // HH:mm
        @NotNull @Min(45) @Max(300) Integer duracionMin
) {}

