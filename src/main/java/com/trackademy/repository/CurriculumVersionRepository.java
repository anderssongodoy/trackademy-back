package com.trackademy.repository;

import com.trackademy.entity.CurriculumVersion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CurriculumVersionRepository extends JpaRepository<CurriculumVersion, Long> {
    List<CurriculumVersion> findByProgramId(Long programId);
    Optional<CurriculumVersion> findByProgramIdAndVersionNumber(Long programId, Integer versionNumber);
}
