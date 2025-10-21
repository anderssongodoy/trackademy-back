package com.trackademy.service.impl;

import com.trackademy.dto.CampusDTO;
import com.trackademy.entity.Campus;
import com.trackademy.repository.CampusRepository;
import com.trackademy.service.CampusService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class CampusServiceImpl implements CampusService {
    
    private static final Logger logger = LoggerFactory.getLogger(CampusServiceImpl.class);
    private final CampusRepository campusRepository;
    
    public CampusServiceImpl(CampusRepository campusRepository) {
        this.campusRepository = campusRepository;
    }
    
    @Override
    public CampusDTO createCampus(CampusDTO campusDTO) {
        logger.info("Creating campus: {}", campusDTO.getName());
        
        Campus campus = Campus.builder()
                .name(campusDTO.getName())
                .city(campusDTO.getCity())
                .region(campusDTO.getRegion())
                .address(campusDTO.getAddress())
                .active(true)
                .build();
        
        Campus saved = campusRepository.save(campus);
        logger.info("Campus created with ID: {}", saved.getId());
        
        return mapToDTO(saved);
    }
    
    @Override
    public Optional<CampusDTO> getCampusById(Long id) {
        logger.debug("Fetching campus by ID: {}", id);
        return campusRepository.findById(id).map(this::mapToDTO);
    }
    
    @Override
    public Optional<CampusDTO> getCampusByName(String name) {
        logger.debug("Fetching campus by name: {}", name);
        return campusRepository.findByName(name).map(this::mapToDTO);
    }
    
    @Override
    public List<CampusDTO> getAllCampus() {
        logger.debug("Fetching all campus");
        return campusRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public CampusDTO updateCampus(Long id, CampusDTO campusDTO) {
        logger.info("Updating campus with ID: {}", id);
        
        Campus campus = campusRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Campus not found with ID: {}", id);
                    return new RuntimeException("Campus not found");
                });
        
        campus.setName(campusDTO.getName());
        campus.setCity(campusDTO.getCity());
        campus.setRegion(campusDTO.getRegion());
        campus.setAddress(campusDTO.getAddress());
        
        Campus updated = campusRepository.save(campus);
        logger.info("Campus updated successfully");
        
        return mapToDTO(updated);
    }
    
    @Override
    public void deleteCampus(Long id) {
        logger.info("Deleting campus with ID: {}", id);
        campusRepository.deleteById(id);
        logger.info("Campus deleted successfully");
    }
    
    private CampusDTO mapToDTO(Campus campus) {
        return CampusDTO.builder()
                .id(campus.getId())
                .name(campus.getName())
                .city(campus.getCity())
                .region(campus.getRegion())
                .address(campus.getAddress())
                .active(campus.getActive())
                .build();
    }
}
