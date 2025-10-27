package com.trackademy.domain.repo;

import com.trackademy.domain.entity.RecordatorioEvento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface RecordatorioEventoRepository extends JpaRepository<RecordatorioEvento, Long> {
    List<RecordatorioEvento> findByUsuarioIdAndFechaProgramadaBetweenAndEnviadoFalse(Long usuarioId, LocalDateTime from, LocalDateTime to);
}

