package com.trackademy.domain.repo;

import com.trackademy.domain.entity.Carrera;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CarreraRepository extends JpaRepository<Carrera, Long> {
    List<Carrera> findByUniversidadIdOrderByNombreAsc(Long universidadId);
}

