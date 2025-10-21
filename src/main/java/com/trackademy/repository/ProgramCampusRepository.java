package com.trackademy.repository;

import com.trackademy.entity.ProgramCampus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProgramCampusRepository extends JpaRepository<ProgramCampus, Long> {
    List<ProgramCampus> findByProgramId(Long programId);
    List<ProgramCampus> findByCampusId(Long campusId);
}
