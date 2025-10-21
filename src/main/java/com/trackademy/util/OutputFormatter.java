package com.trackademy.util;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class OutputFormatter {
    
    private static final Logger logger = LoggerFactory.getLogger(OutputFormatter.class);
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    
    public static String formatGrade(BigDecimal grade) {
        if (grade == null) {
            return "N/A";
        }
        String formatted = StringUtils.leftPad(grade.setScale(2, RoundingMode.HALF_UP).toString(), 5);
        logger.debug("Formatted grade: {}", formatted);
        return formatted;
    }
    
    public static String formatEnrollmentSummary(String studentCode, String programName, String status) {
        StringBuilder summary = new StringBuilder();
        summary.append(StringUtils.rightPad(studentCode, 15));
        summary.append(StringUtils.rightPad(StringUtils.abbreviate(programName, 25), 26));
        summary.append(StringUtils.upperCase(status));
        
        logger.debug("Formatted enrollment summary");
        return summary.toString();
    }
    
    public static String formatDate(LocalDate date) {
        if (date == null) {
            return "N/A";
        }
        String formatted = date.format(DATE_FORMATTER);
        logger.debug("Formatted date: {}", formatted);
        return formatted;
    }
    
    public static String formatCourseInfo(String code, String name, Integer credits) {
        StringBuilder info = new StringBuilder();
        info.append(StringUtils.leftPad(code, 12));
        info.append(" | ");
        info.append(StringUtils.rightPad(StringUtils.abbreviate(name, 40), 41));
        info.append(" | ");
        info.append(StringUtils.leftPad(String.valueOf(credits), 2));
        
        logger.debug("Formatted course info");
        return info.toString();
    }
}
