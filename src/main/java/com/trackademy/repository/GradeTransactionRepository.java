package com.trackademy.repository;

import com.trackademy.entity.GradeTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GradeTransactionRepository extends JpaRepository<GradeTransaction, Long> {
    List<GradeTransaction> findByEnrolledCourseId(Long enrolledCourseId);
}
