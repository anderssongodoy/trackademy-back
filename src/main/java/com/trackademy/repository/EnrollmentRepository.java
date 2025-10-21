package com.trackademy.repository;

import com.trackademy.entity.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    Optional<Enrollment> findByUserIdAndTermId(Long userId, Long termId);
    List<Enrollment> findByUserId(Long userId);
    List<Enrollment> findByTermId(Long termId);
    List<Enrollment> findByProgramId(Long programId);
}
