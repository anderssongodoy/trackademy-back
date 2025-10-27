package com.trackademy.domain.repo;

import com.trackademy.domain.entity.Unidad;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UnidadRepository extends JpaRepository<Unidad, Long> {
    List<Unidad> findByCursoIdOrderByNroAsc(Long cursoId);
}
