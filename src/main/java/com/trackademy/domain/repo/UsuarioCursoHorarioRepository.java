package com.trackademy.domain.repo;

import com.trackademy.domain.entity.UsuarioCursoHorario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import java.util.List;

public interface UsuarioCursoHorarioRepository extends JpaRepository<UsuarioCursoHorario, Long> {
    List<UsuarioCursoHorario> findByUsuarioCursoId(Long usuarioCursoId);
    List<UsuarioCursoHorario> findByUsuarioCursoUsuarioId(Long usuarioId);
    List<UsuarioCursoHorario> findByUsuarioCursoIdAndHoraInicioAndDuracionMin(Long usuarioCursoId, java.time.LocalTime horaInicio, Integer duracionMin);

    @Modifying
    void deleteByUsuarioCursoId(Long usuarioCursoId);
}
