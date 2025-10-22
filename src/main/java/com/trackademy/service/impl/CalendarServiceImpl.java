package com.trackademy.service.impl;

import com.trackademy.dto.CalendarEventDTO;
import com.trackademy.entity.CalendarEvent;
import com.trackademy.repository.CalendarEventRepository;
import com.trackademy.service.CalendarService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CalendarServiceImpl implements CalendarService {

    private final CalendarEventRepository repo;

    public CalendarServiceImpl(CalendarEventRepository repo) {
        this.repo = repo;
    }

    @Override
    public List<CalendarEventDTO> getEventsForUser(Long userId, LocalDateTime start, LocalDateTime end) {
        return repo.findByUserIdAndStartAtBetweenOrderByStartAt(userId, start, end)
                .stream()
                .map(e -> CalendarEventDTO.builder()
                        .id(e.getId())
                        .userId(e.getUserId())
                        .title(e.getTitle())
                        .description(e.getDescription())
                        .startAt(e.getStartAt())
                        .endAt(e.getEndAt())
                        .allDay(e.getAllDay())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public CalendarEventDTO createEvent(CalendarEventDTO dto) {
        CalendarEvent e = CalendarEvent.builder()
                .userId(dto.getUserId())
                .title(dto.getTitle())
                .description(dto.getDescription())
                .startAt(dto.getStartAt())
                .endAt(dto.getEndAt())
                .allDay(dto.getAllDay() != null ? dto.getAllDay() : false)
                .build();

        CalendarEvent saved = repo.save(e);
        dto.setId(saved.getId());
        return dto;
    }
}
