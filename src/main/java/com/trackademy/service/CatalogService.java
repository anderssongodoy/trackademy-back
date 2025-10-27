package com.trackademy.service;

import com.trackademy.dto.CarreraDto;
import com.trackademy.dto.CursoDetailDto;
import com.trackademy.dto.CursoDto;

import java.util.List;

public interface CatalogService {
    java.util.List<com.trackademy.dto.CampusDto> listarCampus(Long universidadId);
    java.util.List<com.trackademy.dto.PeriodoDto> listarPeriodos(Long universidadId);
    List<CarreraDto> listarCarreras(Long universidadId);
    List<CursoDto> listarCursosPorCarrera(Long carreraId);
    CursoDetailDto obtenerCursoDetalle(Long cursoId);
}
