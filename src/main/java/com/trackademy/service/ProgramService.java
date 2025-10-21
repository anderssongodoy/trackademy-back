package com.trackademy.service;

import com.trackademy.dto.ProgramDTO;

import java.util.List;
import java.util.Optional;

public interface ProgramService {
    ProgramDTO createProgram(ProgramDTO programDTO);
    Optional<ProgramDTO> getProgramById(Long id);
    Optional<ProgramDTO> getProgramBySlug(String slug);
    List<ProgramDTO> getProgramsByInstitution(Long institutionId);
    ProgramDTO updateProgram(Long id, ProgramDTO programDTO);
    void deleteProgram(Long id);
}
