package com.trackademy.domain.repo;

import com.trackademy.domain.entity.RiskEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import java.util.List;

public interface RiskEventRepository extends JpaRepository<RiskEvent, Long> {
    List<RiskEvent> findByUsuarioCursoIdAndActivoTrue(Long usuarioCursoId);

    @Modifying
    void deleteByUsuarioCursoId(Long usuarioCursoId);
}
