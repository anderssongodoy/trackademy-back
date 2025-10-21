package com.trackademy.repository;

import com.trackademy.entity.StudentCourseSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentCourseScheduleRepository extends JpaRepository<StudentCourseSchedule, Long> {
    List<StudentCourseSchedule> findByEnrolledCourseId(Long enrolledCourseId);
    List<StudentCourseSchedule> findByCourseScheduleId(Long courseScheduleId);
}
