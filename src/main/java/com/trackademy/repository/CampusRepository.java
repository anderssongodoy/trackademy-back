package com.trackademy.repository;

import com.trackademy.entity.Campus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CampusRepository extends JpaRepository<Campus, Long> {
    Optional<Campus> findByName(String name);
    Optional<Campus> findByCity(String city);
}
