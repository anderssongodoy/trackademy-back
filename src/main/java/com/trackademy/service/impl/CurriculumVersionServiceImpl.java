package com.trackademy.service.impl;

import com.trackademy.dto.CurriculumVersionDTO;
import com.trackademy.entity.CurriculumVersion;
import com.trackademy.repository.CurriculumVersionRepository;
import com.trackademy.repository.ProgramRepository;
import com.trackademy.service.CurriculumVersionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class CurriculumVersionServiceImpl implements CurriculumVersionService {
    
    private static final Logger logger = LoggerFactory.getLogger(CurriculumVersionServiceImpl.class);
    private final CurriculumVersionRepository curriculumVersionRepository;
    private final ProgramRepository programRepository;
    
    public CurriculumVersionServiceImpl(CurriculumVersionRepository curriculumVersionRepository,
                                      ProgramRepository programRepository) {
        this.curriculumVersionRepository = curriculumVersionRepository;
        this.programRepository = programRepository;
    }
    
    @Override
    public CurriculumVersionDTO createCurriculumVersion(CurriculumVersionDTO curriculumVersionDTO) {
        logger.info("Creating curriculum version for program: {}", curriculumVersionDTO.getProgramId());
        
        CurriculumVersion curriculumVersion = CurriculumVersion.builder()
                .versionNumber(curriculumVersionDTO.getVersionNumber())
                .academicYearStart(curriculumVersionDTO.getAcademicYearStart())
                .name(curriculumVersionDTO.getName())
                .description(curriculumVersionDTO.getDescription())
                .isActive(curriculumVersionDTO.getIsActive())
                .build();
        
        if (curriculumVersionDTO.getProgramId() != null) {
            curriculumVersion.setProgram(programRepository.findById(curriculumVersionDTO.getProgramId())
                    .orElseThrow(() -> new RuntimeException("Program not found")));
        }
        
        CurriculumVersion saved = curriculumVersionRepository.save(curriculumVersion);
        logger.info("Curriculum version created with ID: {}", saved.getId());
        
        return mapToDTO(saved);
    }
    
    @Override
    public Optional<CurriculumVersionDTO> getCurriculumVersionById(Long id) {
        logger.debug("Fetching curriculum version by ID: {}", id);
        return curriculumVersionRepository.findById(id).map(this::mapToDTO);
    }
    
    @Override
    public List<CurriculumVersionDTO> getCurriculumVersionsByProgram(Long programId) {
        logger.debug("Fetching curriculum versions for program: {}", programId);
        return curriculumVersionRepository.findByProgramId(programId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public Optional<CurriculumVersionDTO> getCurriculumVersionByProgramAndVersion(Long programId, Integer versionNumber) {
        logger.debug("Fetching curriculum version for program: {} and version: {}", programId, versionNumber);
        return curriculumVersionRepository.findByProgramIdAndVersionNumber(programId, versionNumber)
                .map(this::mapToDTO);
    }
    
    @Override
    public CurriculumVersionDTO updateCurriculumVersion(Long id, CurriculumVersionDTO curriculumVersionDTO) {
        logger.info("Updating curriculum version with ID: {}", id);
        
        CurriculumVersion curriculumVersion = curriculumVersionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Curriculum version not found"));
        
        curriculumVersion.setVersionNumber(curriculumVersionDTO.getVersionNumber());
        curriculumVersion.setAcademicYearStart(curriculumVersionDTO.getAcademicYearStart());
        curriculumVersion.setName(curriculumVersionDTO.getName());
        curriculumVersion.setDescription(curriculumVersionDTO.getDescription());
        curriculumVersion.setIsActive(curriculumVersionDTO.getIsActive());
        
        CurriculumVersion updated = curriculumVersionRepository.save(curriculumVersion);
        logger.info("Curriculum version updated successfully");
        
        return mapToDTO(updated);
    }
    
    @Override
    public void deleteCurriculumVersion(Long id) {
        logger.info("Deleting curriculum version with ID: {}", id);
        curriculumVersionRepository.deleteById(id);
        logger.info("Curriculum version deleted successfully");
    }
    
    private CurriculumVersionDTO mapToDTO(CurriculumVersion curriculumVersion) {
        return CurriculumVersionDTO.builder()
                .id(curriculumVersion.getId())
                .programId(curriculumVersion.getProgram() != null ? curriculumVersion.getProgram().getId() : null)
                .versionNumber(curriculumVersion.getVersionNumber())
                .academicYearStart(curriculumVersion.getAcademicYearStart())
                .name(curriculumVersion.getName())
                .description(curriculumVersion.getDescription())
                .isActive(curriculumVersion.getIsActive())
                .build();
    }
}
