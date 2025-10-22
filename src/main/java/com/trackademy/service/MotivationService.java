package com.trackademy.service;

import com.trackademy.dto.MotivationDTO;

import java.util.List;

public interface MotivationService {
    List<MotivationDTO> getMotivationsForUser(Long userId);
    MotivationDTO createMotivation(MotivationDTO dto);
    int computeSimpleScore(Long userId); // simple ranking metric
}
