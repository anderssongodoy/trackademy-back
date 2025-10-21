package com.trackademy.repository;

import com.trackademy.entity.TermEnrollmentWindow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TermEnrollmentWindowRepository extends JpaRepository<TermEnrollmentWindow, Long> {
    List<TermEnrollmentWindow> findByTermId(Long termId);
    List<TermEnrollmentWindow> findByProgramId(Long programId);
}
