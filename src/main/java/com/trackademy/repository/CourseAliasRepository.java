package com.trackademy.repository;

import com.trackademy.entity.CourseAlias;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseAliasRepository extends JpaRepository<CourseAlias, Long> {
    List<CourseAlias> findByCourseId(Long courseId);
    Optional<CourseAlias> findByAliasCode(String aliasCode);
}
