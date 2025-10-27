package com.trackademy.domain.repo;

import com.trackademy.domain.entity.ReglaRecordatorio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReglaRecordatorioRepository extends JpaRepository<ReglaRecordatorio, Long> {
    List<ReglaRecordatorio> findByUsuarioIdAndActivoTrue(Long usuarioId);
}

