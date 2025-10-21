package com.trackademy.service;

import com.trackademy.dto.CurriculumVersionDTO;

import java.util.List;
import java.util.Optional;

public interface CurriculumVersionService {
    CurriculumVersionDTO createCurriculumVersion(CurriculumVersionDTO curriculumVersionDTO);
    Optional<CurriculumVersionDTO> getCurriculumVersionById(Long id);
    List<CurriculumVersionDTO> getCurriculumVersionsByProgram(Long programId);
    Optional<CurriculumVersionDTO> getCurriculumVersionByProgramAndVersion(Long programId, Integer versionNumber);
    CurriculumVersionDTO updateCurriculumVersion(Long id, CurriculumVersionDTO curriculumVersionDTO);
    void deleteCurriculumVersion(Long id);
}
