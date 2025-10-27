package com.trackademy.domain.repo;

import com.trackademy.domain.entity.AgendaEvento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface AgendaEventoRepository extends JpaRepository<AgendaEvento, Long> {
    List<AgendaEvento> findByUsuarioIdAndInicioBetween(Long usuarioId, LocalDateTime start, LocalDateTime end);
}

