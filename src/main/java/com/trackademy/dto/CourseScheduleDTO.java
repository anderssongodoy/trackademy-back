package com.trackademy.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseScheduleDTO {
    private Long id;
    private Long courseId;
    private Long termId;
    private Long campusId;
    private String section;
    private Integer dayOfWeek;
    private LocalTime startTime;
    private LocalTime endTime;
    private String classroom;
    private String building;
    private String teacherName;
    private String modality;
    private String meetingUrl;
    private Integer maxCapacity;
    private Integer currentEnrollment;
}
