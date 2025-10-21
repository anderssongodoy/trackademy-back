package com.trackademy.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OnboardingRequestDto {
    private String campus;
    private Integer cycle;
    private String program;
    private String specialization;
    private List<String> careerInterests;
    private Integer studyHoursPerDay;
    private String learningStyle;
    private List<String> motivationFactors;
    private Boolean wantsAlerts;
    private Boolean wantsIncentives;
    private Boolean allowDataSharing;
}
