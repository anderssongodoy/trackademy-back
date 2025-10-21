package com.trackademy.repository;

import com.trackademy.entity.Term;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TermRepository extends JpaRepository<Term, Long> {
    Optional<Term> findByCode(String code);
    List<Term> findByInstitutionId(Long institutionId);
    List<Term> findByYear(Integer year);
}
