package com.trackademy.domain.repo;

import com.trackademy.domain.entity.UsuarioHabitoLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface UsuarioHabitoLogRepository extends JpaRepository<UsuarioHabitoLog, Long> {
    List<UsuarioHabitoLog> findByUsuarioHabitoIdAndFechaBetween(Long usuarioHabitoId, LocalDate from, LocalDate to);
}

