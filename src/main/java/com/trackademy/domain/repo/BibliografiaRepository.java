package com.trackademy.domain.repo;

import com.trackademy.domain.entity.Bibliografia;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BibliografiaRepository extends JpaRepository<Bibliografia, Long> {
    List<Bibliografia> findByCursoId(Long cursoId);
}

