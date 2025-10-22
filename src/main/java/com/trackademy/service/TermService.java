package com.trackademy.service;

import com.trackademy.dto.TermDTO;
import com.trackademy.dto.CycleDto;

import java.util.List;
import java.util.Optional;

public interface TermService {
    TermDTO createTerm(TermDTO termDTO);
    Optional<TermDTO> getTermById(Long id);
    Optional<TermDTO> getTermByCode(String code);
    List<TermDTO> getTermsByInstitution(Long institutionId);
    List<TermDTO> getTermsByYear(Integer year);
    List<TermDTO> getAllTerms();
    TermDTO updateTerm(Long id, TermDTO termDTO);
    void deleteTerm(Long id);
    List<CycleDto> getAvailableCycles();
}
