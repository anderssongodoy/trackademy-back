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
public class UserCourseScheduleDto {
    private Long enrolledCourseId;
    private Long courseId;
    private String courseCode;
    private String courseName;
    private Integer credits;
    private Long courseScheduleId;
    private Integer dayOfWeek;
    private LocalTime startTime;
    private LocalTime endTime;
    private String section;
    private String classroom;
    private String building;
    private String teacherName;
    private String modality;
}
