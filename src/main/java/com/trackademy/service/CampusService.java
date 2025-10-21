package com.trackademy.service;

import com.trackademy.dto.CampusDTO;

import java.util.List;
import java.util.Optional;

public interface CampusService {
    CampusDTO createCampus(CampusDTO campusDTO);
    Optional<CampusDTO> getCampusById(Long id);
    Optional<CampusDTO> getCampusByName(String name);
    List<CampusDTO> getAllCampus();
    CampusDTO updateCampus(Long id, CampusDTO campusDTO);
    void deleteCampus(Long id);
}
