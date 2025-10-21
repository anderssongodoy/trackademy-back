package com.trackademy.repository;

import com.trackademy.entity.CurriculumCourse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CurriculumCourseRepository extends JpaRepository<CurriculumCourse, Long> {
    List<CurriculumCourse> findByCurriculumVersionId(Long curriculumVersionId);
    List<CurriculumCourse> findByCourseId(Long courseId);
}
