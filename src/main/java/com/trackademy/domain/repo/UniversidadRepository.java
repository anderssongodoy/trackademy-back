package com.trackademy.domain.repo;

import com.trackademy.domain.entity.Universidad;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UniversidadRepository extends JpaRepository<Universidad, Long> {
    Optional<Universidad> findByNombre(String nombre);
}

