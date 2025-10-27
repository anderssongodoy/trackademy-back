package com.trackademy.domain.repo;

import com.trackademy.domain.entity.Evaluacion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EvaluacionRepository extends JpaRepository<Evaluacion, Long> {
    List<Evaluacion> findByCursoIdOrderBySemanaAsc(Long cursoId);
}

