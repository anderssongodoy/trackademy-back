package com.trackademy.util;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AuditLogger {
    
    private static final Logger logger = LoggerFactory.getLogger(AuditLogger.class);
    private static final DateTimeFormatter TIMESTAMP_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    public static void logEnrollmentChange(Long enrollmentId, String action, String previousStatus, String newStatus, Long userId) {
        String timestamp = LocalDateTime.now().format(TIMESTAMP_FORMATTER);
        String message = StringUtils.joinWith(" | ",
                timestamp,
                "ENROLLMENT",
                "ID=" + enrollmentId,
                "ACTION=" + StringUtils.upperCase(action),
                "FROM=" + (previousStatus != null ? previousStatus : "NULL"),
                "TO=" + (newStatus != null ? newStatus : "NULL"),
                "USER=" + userId);
        
        logger.info(message);
    }
    
    public static void logGradeChange(Long enrolledCourseId, String oldGrade, String newGrade, String reason) {
        String message = StringUtils.joinWith(" | ",
                LocalDateTime.now().format(TIMESTAMP_FORMATTER),
                "GRADE_CHANGE",
                "COURSE_ID=" + enrolledCourseId,
                "OLD=" + (oldGrade != null ? oldGrade : "NULL"),
                "NEW=" + (newGrade != null ? newGrade : "NULL"),
                "REASON=" + StringUtils.abbreviate(reason, 50));
        
        logger.warn(message);
    }
    
    public static void logCourseEnrollment(Long enrollmentId, Long courseId, String studentCode) {
        String message = StringUtils.joinWith(" | ",
                LocalDateTime.now().format(TIMESTAMP_FORMATTER),
                "COURSE_ENROLLED",
                "STUDENT=" + (studentCode != null ? studentCode : "UNKNOWN"),
                "COURSE_ID=" + courseId,
                "ENROLLMENT_ID=" + enrollmentId);
        
        logger.info(message);
    }
}
