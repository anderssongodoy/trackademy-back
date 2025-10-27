package com.trackademy.domain.repo;

import com.trackademy.domain.entity.UsuarioEvaluacion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UsuarioEvaluacionRepository extends JpaRepository<UsuarioEvaluacion, Long> {
    List<UsuarioEvaluacion> findByUsuarioCursoIdOrderBySemanaAsc(Long usuarioCursoId);
}

