package com.trackademy.domain.repo;

import com.trackademy.domain.entity.Carrera;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CarreraRepository extends JpaRepository<Carrera, Long> {
    List<Carrera> findByUniversidadIdOrderByNombreAsc(Long universidadId);
    Optional<Carrera> findFirstByUniversidadIdAndNombreIgnoreCase(Long universidadId, String nombre);
}
