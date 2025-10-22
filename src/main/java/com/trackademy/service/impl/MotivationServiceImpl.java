package com.trackademy.service.impl;

import com.trackademy.dto.MotivationDTO;
import com.trackademy.entity.Motivation;
import com.trackademy.repository.MotivationRepository;
import com.trackademy.service.MotivationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class MotivationServiceImpl implements MotivationService {

    private final MotivationRepository repo;

    public MotivationServiceImpl(MotivationRepository repo) {
        this.repo = repo;
    }

    @Override
    public List<MotivationDTO> getMotivationsForUser(Long userId) {
        return repo.findByUserIdOrderByCreatedAtDesc(userId)
                .stream()
                .map(m -> MotivationDTO.builder()
                        .id(m.getId())
                        .userId(m.getUserId())
                        .text(m.getText())
                        .score(m.getScore())
                        .createdAt(m.getCreatedAt())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public MotivationDTO createMotivation(MotivationDTO dto) {
        Motivation m = Motivation.builder()
                .userId(dto.getUserId())
                .text(dto.getText())
                .score(dto.getScore() != null ? dto.getScore() : 0)
                .createdAt(LocalDateTime.now())
                .build();
        Motivation saved = repo.save(m);
        dto.setId(saved.getId());
        dto.setCreatedAt(saved.getCreatedAt());
        return dto;
    }

    @Override
    public int computeSimpleScore(Long userId) {
        // Simple metric: sum of motivation scores
        return repo.findByUserIdOrderByCreatedAtDesc(userId)
                .stream()
                .mapToInt(m -> m.getScore() != null ? m.getScore() : 0)
                .sum();
    }
}
