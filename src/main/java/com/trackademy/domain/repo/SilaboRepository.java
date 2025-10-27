package com.trackademy.domain.repo;

import com.trackademy.domain.entity.Silabo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SilaboRepository extends JpaRepository<Silabo, Long> {
    Optional<Silabo> findFirstByCursoIdAndVigenteTrueOrderByIdDesc(Long cursoId);
}
