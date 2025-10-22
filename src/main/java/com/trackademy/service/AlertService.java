package com.trackademy.service;

import com.trackademy.dto.AlertItemDTO;

import java.util.List;

public interface AlertService {
    List<AlertItemDTO> getAlertsForUser(Long userId);
    AlertItemDTO createAlert(AlertItemDTO dto);
    void markRead(Long alertId, Long userId);
}
