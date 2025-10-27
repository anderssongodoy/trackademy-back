package com.trackademy.service;

import com.trackademy.dto.CarreraDto;
import com.trackademy.dto.CursoDetailDto;
import com.trackademy.dto.CursoDto;

import java.util.List;

public interface CatalogService {
    List<CarreraDto> listarCarreras(Long universidadId);
    List<CursoDto> listarCursosPorCarrera(Long carreraId);
    CursoDetailDto obtenerCursoDetalle(Long cursoId);
}

