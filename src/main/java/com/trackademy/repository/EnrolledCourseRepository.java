package com.trackademy.repository;

import com.trackademy.entity.EnrolledCourse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EnrolledCourseRepository extends JpaRepository<EnrolledCourse, Long> {
    List<EnrolledCourse> findByEnrollmentId(Long enrollmentId);
    Optional<EnrolledCourse> findByEnrollmentIdAndCourseIdAndTermId(Long enrollmentId, Long courseId, Long termId);
    List<EnrolledCourse> findByTermId(Long termId);
}
