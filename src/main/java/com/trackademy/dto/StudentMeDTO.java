package com.trackademy.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentMeDTO {
    private Long id;
    private String name;
    private String email;
    private Long campusId;
    private Long programId;
    private String campusName;
    private String programName;
    private String termLabel;
    private Map<String, Object> currentTerm;
    private Integer creditsRequired;
    private Integer creditsApproved;
    private Double gpa;
}
