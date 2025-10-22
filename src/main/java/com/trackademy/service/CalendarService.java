package com.trackademy.service;

import com.trackademy.dto.CalendarEventDTO;

import java.time.LocalDateTime;
import java.util.List;

public interface CalendarService {
    List<CalendarEventDTO> getEventsForUser(Long userId, LocalDateTime start, LocalDateTime end);
    CalendarEventDTO createEvent(CalendarEventDTO dto);
}
