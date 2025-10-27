package com.trackademy.domain.repo;

import com.trackademy.domain.entity.Recomendacion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface RecomendacionRepository extends JpaRepository<Recomendacion, Long> {
    List<Recomendacion> findByUsuarioIdAndActivoTrue(Long usuarioId);
    List<Recomendacion> findByUsuarioIdAndFechaSugeridaBetween(Long usuarioId, LocalDate from, LocalDate to);
}

