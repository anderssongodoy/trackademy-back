package com.trackademy.domain.repo;

import com.trackademy.domain.entity.UsuarioPerfil;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioPerfilRepository extends JpaRepository<UsuarioPerfil, Long> {
    Optional<UsuarioPerfil> findByUsuarioId(Long usuarioId);
}

