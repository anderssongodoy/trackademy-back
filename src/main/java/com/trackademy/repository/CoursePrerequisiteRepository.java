package com.trackademy.repository;

import com.trackademy.entity.CoursePrerequisite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CoursePrerequisiteRepository extends JpaRepository<CoursePrerequisite, Long> {
    List<CoursePrerequisite> findByCourseId(Long courseId);
    List<CoursePrerequisite> findByPrerequisiteCourseId(Long courseId);
}
