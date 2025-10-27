package com.trackademy.domain.repo;

import com.trackademy.domain.entity.Tema;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TemaRepository extends JpaRepository<Tema, Long> {
    List<Tema> findByUnidadIdOrderByIdAsc(Long unidadId);
}

