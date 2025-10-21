package com.trackademy.repository;

import com.trackademy.entity.CourseSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseScheduleRepository extends JpaRepository<CourseSchedule, Long> {
    List<CourseSchedule> findByCourseIdAndTermId(Long courseId, Long termId);
    List<CourseSchedule> findByCampusIdAndTermId(Long campusId, Long termId);
    List<CourseSchedule> findByTermId(Long termId);
}
