package com.trackademy.service.impl;

import com.trackademy.dto.TermDTO;
import com.trackademy.entity.Term;
import com.trackademy.repository.TermRepository;
import com.trackademy.repository.InstitutionRepository;
import com.trackademy.service.TermService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class TermServiceImpl implements TermService {
    
    private static final Logger logger = LoggerFactory.getLogger(TermServiceImpl.class);
    private final TermRepository termRepository;
    private final InstitutionRepository institutionRepository;
    
    public TermServiceImpl(TermRepository termRepository, InstitutionRepository institutionRepository) {
        this.termRepository = termRepository;
        this.institutionRepository = institutionRepository;
    }
    
    @Override
    public TermDTO createTerm(TermDTO termDTO) {
        logger.info("Creating term: {} ({})", termDTO.getCode(), termDTO.getYear());
        
        Term term = Term.builder()
                .code(termDTO.getCode())
                .name(termDTO.getName())
                .year(termDTO.getYear())
                .cycleNumber(termDTO.getCycleNumber())
                .periodRaw(termDTO.getPeriodRaw())
                .fullName(termDTO.getFullName())
                .startDate(termDTO.getStartDate())
                .endDate(termDTO.getEndDate())
                .isActive(termDTO.getIsActive())
                .build();
        
        if (termDTO.getInstitutionId() != null) {
            term.setInstitution(institutionRepository.findById(termDTO.getInstitutionId())
                    .orElseThrow(() -> new RuntimeException("Institution not found")));
        }
        
        Term saved = termRepository.save(term);
        logger.info("Term created with ID: {}", saved.getId());
        
        return mapToDTO(saved);
    }
    
    @Override
    public Optional<TermDTO> getTermById(Long id) {
        logger.debug("Fetching term by ID: {}", id);
        return termRepository.findById(id).map(this::mapToDTO);
    }
    
    @Override
    public Optional<TermDTO> getTermByCode(String code) {
        logger.debug("Fetching term by code: {}", code);
        return termRepository.findByCode(code).map(this::mapToDTO);
    }
    
    @Override
    public List<TermDTO> getTermsByInstitution(Long institutionId) {
        logger.debug("Fetching terms for institution ID: {}", institutionId);
        return termRepository.findByInstitutionId(institutionId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<TermDTO> getTermsByYear(Integer year) {
        logger.debug("Fetching terms for year: {}", year);
        return termRepository.findByYear(year).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<TermDTO> getAllTerms() {
        logger.debug("Fetching all terms");
        return termRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public TermDTO updateTerm(Long id, TermDTO termDTO) {
        logger.info("Updating term with ID: {}", id);
        
        Term term = termRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Term not found"));
        
        term.setCode(termDTO.getCode());
        term.setName(termDTO.getName());
        term.setYear(termDTO.getYear());
        term.setCycleNumber(termDTO.getCycleNumber());
        term.setPeriodRaw(termDTO.getPeriodRaw());
        term.setFullName(termDTO.getFullName());
        term.setStartDate(termDTO.getStartDate());
        term.setEndDate(termDTO.getEndDate());
        term.setIsActive(termDTO.getIsActive());
        
        Term updated = termRepository.save(term);
        logger.info("Term updated successfully");
        
        return mapToDTO(updated);
    }
    
    @Override
    public void deleteTerm(Long id) {
        logger.info("Deleting term with ID: {}", id);
        termRepository.deleteById(id);
        logger.info("Term deleted successfully");
    }

    @Override
    public java.util.List<com.trackademy.dto.CycleDto> getAvailableCycles() {
        logger.debug("Fetching available cycles for onboarding");
        return java.util.Arrays.asList(
                com.trackademy.dto.CycleDto.builder()
                        .id(1)
                        .label("Ciclo I")
                        .description("Primer Ciclo")
                        .build(),
                com.trackademy.dto.CycleDto.builder()
                        .id(2)
                        .label("Ciclo II")
                        .description("Segundo Ciclo")
                        .build(),
                com.trackademy.dto.CycleDto.builder()
                        .id(3)
                        .label("Ciclo III")
                        .description("Tercer Ciclo")
                        .build(),
                com.trackademy.dto.CycleDto.builder()
                        .id(4)
                        .label("Ciclo IV")
                        .description("Cuarto Ciclo")
                        .build()
        );
    }
    
    private TermDTO mapToDTO(Term term) {
        return TermDTO.builder()
                .id(term.getId())
                .institutionId(term.getInstitution() != null ? term.getInstitution().getId() : null)
                .code(term.getCode())
                .name(term.getName())
                .year(term.getYear())
                .cycleNumber(term.getCycleNumber())
                .periodRaw(term.getPeriodRaw())
                .fullName(term.getFullName())
                .startDate(term.getStartDate())
                .endDate(term.getEndDate())
                .isActive(term.getIsActive())
                .build();
    }
}
