package com.trackademy.service.impl;

import com.trackademy.dto.ProgramDTO;
import com.trackademy.entity.Program;
import com.trackademy.repository.ProgramRepository;
import com.trackademy.repository.InstitutionRepository;
import com.trackademy.repository.CampusRepository;
import com.trackademy.service.ProgramService;
import com.trackademy.util.StringNormalizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProgramServiceImpl implements ProgramService {
    
    private static final Logger logger = LoggerFactory.getLogger(ProgramServiceImpl.class);
    private final ProgramRepository programRepository;
    private final InstitutionRepository institutionRepository;
    private final CampusRepository campusRepository;
    
    public ProgramServiceImpl(ProgramRepository programRepository,
                            InstitutionRepository institutionRepository,
                            CampusRepository campusRepository) {
        this.programRepository = programRepository;
        this.institutionRepository = institutionRepository;
        this.campusRepository = campusRepository;
    }
    
    @Override
    public ProgramDTO createProgram(ProgramDTO programDTO) {
        logger.info(StringNormalizer.buildDetailedLog("PROGRAM", "CREATE", programDTO.getName()));
        
        Program program = Program.builder()
                .slug(StringNormalizer.generateSlug(programDTO.getName()))
                .name(programDTO.getName())
                .modality(programDTO.getModality())
                .build();
        
        if (programDTO.getInstitutionId() != null) {
            program.setInstitution(institutionRepository.findById(programDTO.getInstitutionId())
                    .orElseThrow(() -> new RuntimeException("Institution not found")));
        }
        
        if (programDTO.getCampusId() != null) {
            program.setCampus(campusRepository.findById(programDTO.getCampusId())
                    .orElseThrow(() -> new RuntimeException("Campus not found")));
        }
        
        Program saved = programRepository.save(program);
        logger.info("Program created with ID: {}", saved.getId());
        
        return mapToDTO(saved);
    }
    
    @Override
    public Optional<ProgramDTO> getProgramById(Long id) {
        logger.debug("Fetching program by ID: {}", id);
        return programRepository.findById(id).map(this::mapToDTO);
    }
    
    @Override
    public Optional<ProgramDTO> getProgramBySlug(String slug) {
        logger.debug("Fetching program by slug: {}", slug);
        return programRepository.findBySlug(slug).map(this::mapToDTO);
    }
    
    @Override
    public List<ProgramDTO> getProgramsByInstitution(Long institutionId) {
        logger.debug("Fetching programs for institution ID: {}", institutionId);
        return programRepository.findByInstitutionId(institutionId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public ProgramDTO updateProgram(Long id, ProgramDTO programDTO) {
        logger.info("Updating program with ID: {}", id);
        
        Program program = programRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Program not found"));
        
        program.setName(programDTO.getName());
        program.setSlug(StringNormalizer.generateSlug(programDTO.getName()));
        program.setModality(programDTO.getModality());
        
        Program updated = programRepository.save(program);
        logger.info("Program updated successfully");
        
        return mapToDTO(updated);
    }
    
    @Override
    public void deleteProgram(Long id) {
        logger.info("Deleting program with ID: {}", id);
        programRepository.deleteById(id);
        logger.info("Program deleted successfully");
    }
    
    private ProgramDTO mapToDTO(Program program) {
        return ProgramDTO.builder()
                .id(program.getId())
                .slug(program.getSlug())
                .name(program.getName())
                .institutionId(program.getInstitution() != null ? program.getInstitution().getId() : null)
                .campusId(program.getCampus() != null ? program.getCampus().getId() : null)
                .modality(program.getModality())
                .build();
    }
}
