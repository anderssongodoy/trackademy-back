package com.trackademy.domain.repo;

import com.trackademy.domain.entity.CursoCompetencia;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CursoCompetenciaRepository extends JpaRepository<CursoCompetencia, Long> {
    List<CursoCompetencia> findByCursoId(Long cursoId);
}

