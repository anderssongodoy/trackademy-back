package com.trackademy.domain.repo;

import com.trackademy.domain.entity.StudyTask;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface StudyTaskRepository extends JpaRepository<StudyTask, Long> {
    List<StudyTask> findByUsuarioIdAndCompletadoFalse(Long usuarioId);
    List<StudyTask> findByUsuarioIdAndFechaSugeridaBetween(Long usuarioId, LocalDate from, LocalDate to);
}

