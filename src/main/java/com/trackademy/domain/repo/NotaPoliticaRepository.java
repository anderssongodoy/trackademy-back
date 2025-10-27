package com.trackademy.domain.repo;

import com.trackademy.domain.entity.NotaPolitica;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NotaPoliticaRepository extends JpaRepository<NotaPolitica, Long> {
    Optional<NotaPolitica> findByCursoId(Long cursoId);
}

