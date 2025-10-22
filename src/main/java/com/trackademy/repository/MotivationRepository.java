package com.trackademy.repository;

import com.trackademy.entity.Motivation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MotivationRepository extends JpaRepository<Motivation, Long> {
    List<Motivation> findByUserIdOrderByCreatedAtDesc(Long userId);
}
