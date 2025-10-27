package com.trackademy.domain.repo;

import com.trackademy.domain.entity.Campus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CampusRepository extends JpaRepository<Campus, Long> { }

