package com.trackademy.repository;

import com.trackademy.entity.Program;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProgramRepository extends JpaRepository<Program, Long> {
    Optional<Program> findBySlug(String slug);
    List<Program> findByInstitutionId(Long institutionId);
}
