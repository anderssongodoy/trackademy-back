package com.trackademy.util;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CodeGenerator {
    
    private static final Logger logger = LoggerFactory.getLogger(CodeGenerator.class);
    
    public static String generateStudentCode(String programSlug, Integer year) {
        if (StringUtils.isBlank(programSlug)) {
            logger.error("Cannot generate student code: program slug is blank");
            throw new IllegalArgumentException("Program slug cannot be blank");
        }
        
        String yearStr = String.valueOf(year);
        String random = RandomStringUtils.randomNumeric(6);
        String programPrefix = StringUtils.upperCase(programSlug.substring(0, Math.min(3, programSlug.length())));
        String code = programPrefix + yearStr + random;
        
        logger.info("Generated student code: {}", code);
        return code;
    }
    
    public static String generateCourseSection(String courseCode, Integer sectionNumber) {
        if (StringUtils.isBlank(courseCode) || sectionNumber == null) {
            logger.error("Invalid parameters for section generation");
            throw new IllegalArgumentException("Course code and section number required");
        }
        
        String section = StringUtils.upperCase(courseCode) + StringUtils.leftPad(String.valueOf(sectionNumber), 2, "0");
        logger.debug("Generated course section: {}", section);
        return section;
    }
}
