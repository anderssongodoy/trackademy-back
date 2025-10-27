package com.trackademy.domain.repo;

import com.trackademy.domain.entity.Curso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CursoRepository extends JpaRepository<Curso, Long> {
    @Query("select c from Curso c join CursoCarrera cc on cc.curso.id = c.id where cc.carrera.id = :carreraId order by c.nombre asc")
    List<Curso> findByCarreraId(@Param("carreraId") Long carreraId);
}

