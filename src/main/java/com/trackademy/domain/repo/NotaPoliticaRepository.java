package com.trackademy.domain.repo;

import com.trackademy.domain.entity.NotaPolitica;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotaPoliticaRepository extends JpaRepository<NotaPolitica, Long> {
    List<NotaPolitica> findByCursoId(Long cursoId);
}
