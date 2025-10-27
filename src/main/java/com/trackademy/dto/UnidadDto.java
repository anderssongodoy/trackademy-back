package com.trackademy.dto;

import java.util.List;

public record UnidadDto(Long id, Integer numero, String titulo, List<TemaDto> temas) {}

