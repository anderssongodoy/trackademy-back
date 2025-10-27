package com.trackademy.domain.repo;

import com.trackademy.domain.entity.UsuarioResumen;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioResumenRepository extends JpaRepository<UsuarioResumen, Long> {
    Optional<UsuarioResumen> findByUsuarioId(Long usuarioId);
}

