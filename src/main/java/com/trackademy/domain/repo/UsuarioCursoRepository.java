package com.trackademy.domain.repo;

import com.trackademy.domain.entity.UsuarioCurso;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UsuarioCursoRepository extends JpaRepository<UsuarioCurso, Long> {
    Optional<UsuarioCurso> findByUsuarioIdAndCursoId(Long usuarioId, Long cursoId);
    List<UsuarioCurso> findByUsuarioId(Long usuarioId);
}

