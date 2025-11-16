package com.trackademy.domain.repo;

import com.trackademy.domain.entity.UsuarioCursoResumen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import java.util.Optional;

public interface UsuarioCursoResumenRepository extends JpaRepository<UsuarioCursoResumen, Long> {
    Optional<UsuarioCursoResumen> findByUsuarioCursoId(Long usuarioCursoId);

    @Modifying
    void deleteByUsuarioCursoId(Long usuarioCursoId);
}
