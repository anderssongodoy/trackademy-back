package com.trackademy.domain.repo;

import com.trackademy.domain.entity.UsuarioHabito;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UsuarioHabitoRepository extends JpaRepository<UsuarioHabito, Long> {
    List<UsuarioHabito> findByUsuarioIdAndActivoTrue(Long usuarioId);
}

