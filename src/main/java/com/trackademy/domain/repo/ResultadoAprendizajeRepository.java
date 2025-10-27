package com.trackademy.domain.repo;

import com.trackademy.domain.entity.ResultadoAprendizaje;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ResultadoAprendizajeRepository extends JpaRepository<ResultadoAprendizaje, Long> {
    List<ResultadoAprendizaje> findByCursoId(Long cursoId);
    List<ResultadoAprendizaje> findByUnidadId(Long unidadId);
}

