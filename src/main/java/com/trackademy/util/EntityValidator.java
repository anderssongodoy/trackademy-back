package com.trackademy.util;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.trackademy.entity.Enrollment;

public class EntityValidator {
    
    private static final Logger logger = LoggerFactory.getLogger(EntityValidator.class);
    
    public static boolean isValidEnrollment(Enrollment enrollment) {
        if (ObjectUtils.isEmpty(enrollment)) {
            logger.error("Enrollment is null");
            return false;
        }
        
        boolean valid = ObjectUtils.allNotNull(enrollment.getUserId(), enrollment.getTerm(), 
                enrollment.getCampus(), enrollment.getProgram());
        
        if (!valid) {
            logger.error("Enrollment missing required fields: userId={}, term={}, campus={}, program={}", 
                enrollment.getUserId(), enrollment.getTerm() != null ? enrollment.getTerm().getId() : "null", 
                enrollment.getCampus() != null ? enrollment.getCampus().getId() : "null",
                enrollment.getProgram() != null ? enrollment.getProgram().getId() : "null");
        }
        
        return valid;
    }
    
    public static boolean isValidStudentCode(String studentCode) {
        if (StringUtils.isBlank(studentCode)) {
            logger.warn("Student code is blank");
            return false;
        }
        
        boolean valid = StringUtils.length(studentCode) >= 8 && StringUtils.length(studentCode) <= 20;
        if (!valid) {
            logger.warn("Invalid student code length: {}", studentCode);
        }
        return valid;
    }
    
    public static boolean isValidCycleRange(Integer entryCycle, Integer currentCycle) {
        if (entryCycle == null || currentCycle == null) {
            logger.error("Cycle values are null");
            return false;
        }
        
        boolean valid = currentCycle >= entryCycle && entryCycle > 0 && currentCycle > 0;
        if (!valid) {
            logger.warn("Invalid cycle range: entry={}, current={}", entryCycle, currentCycle);
        }
        return valid;
    }
}
