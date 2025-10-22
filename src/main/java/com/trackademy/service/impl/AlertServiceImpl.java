package com.trackademy.service.impl;

import com.trackademy.dto.AlertItemDTO;
import com.trackademy.entity.Alert;
import com.trackademy.repository.AlertRepository;
import com.trackademy.service.AlertService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class AlertServiceImpl implements AlertService {

    private final AlertRepository alertRepository;

    public AlertServiceImpl(AlertRepository alertRepository) {
        this.alertRepository = alertRepository;
    }

    @Override
    public List<AlertItemDTO> getAlertsForUser(Long userId) {
        return alertRepository.findByUserIdOrderByCreatedAtDesc(userId)
                .stream()
                .map(a -> AlertItemDTO.builder()
                        .id(a.getId())
                        .userId(a.getUserId())
                        .title(a.getTitle())
                        .message(a.getMessage())
                        .level(a.getLevel())
                        .read(a.getRead())
                        .createdAt(a.getCreatedAt())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public AlertItemDTO createAlert(AlertItemDTO dto) {
        Alert a = Alert.builder()
                .userId(dto.getUserId())
                .title(dto.getTitle())
                .message(dto.getMessage())
                .level(dto.getLevel() != null ? dto.getLevel() : "INFO")
                .read(dto.getRead() != null ? dto.getRead() : false)
                .createdAt(LocalDateTime.now())
                .build();

        Alert saved = alertRepository.save(a);
        dto.setId(saved.getId());
        dto.setCreatedAt(saved.getCreatedAt());
        return dto;
    }

    @Override
    public void markRead(Long alertId, Long userId) {
        alertRepository.findById(alertId).ifPresent(a -> {
            if (a.getUserId().equals(userId)) {
                a.setRead(true);
                alertRepository.save(a);
            }
        });
    }
}
