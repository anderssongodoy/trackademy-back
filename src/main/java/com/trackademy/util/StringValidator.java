package com.trackademy.util;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StringValidator {
    
    private static final Logger logger = LoggerFactory.getLogger(StringValidator.class);
    
    public static boolean isValidEmail(String email) {
        if (StringUtils.isBlank(email)) {
            logger.warn("Email validation failed: email is blank");
            return false;
        }
        boolean valid = StringUtils.contains(email, "@") && StringUtils.contains(email, ".");
        logger.debug("Email '{}' validation result: {}", email, valid);
        return valid;
    }
    
    public static boolean isValidStudentCode(String code) {
        if (StringUtils.isBlank(code)) {
            logger.warn("Student code validation failed: code is blank");
            return false;
        }
        boolean valid = StringUtils.length(code) >= 8 && StringUtils.length(code) <= 20;
        logger.debug("Student code '{}' validation result: {}", code, valid);
        return valid;
    }
    
    public static String sanitizeInput(String input) {
        if (StringUtils.isBlank(input)) {
            return StringUtils.EMPTY;
        }
        String sanitized = StringUtils.strip(input);
        sanitized = StringUtils.trimToEmpty(sanitized);
        logger.debug("Sanitized input: '{}' -> '{}'", input, sanitized);
        return sanitized;
    }
}
