package com.trackademy.util;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StringNormalizer {
    
    private static final Logger logger = LoggerFactory.getLogger(StringNormalizer.class);
    
    public static String normalize(String input) {
        if (StringUtils.isBlank(input)) {
            return StringUtils.EMPTY;
        }
        
        String normalized = StringUtils.stripAccents(input);
        normalized = StringUtils.lowerCase(normalized);
        normalized = StringUtils.trim(normalized);
        normalized = normalized.replaceAll("\\s+", "_");
        normalized = normalized.replaceAll("[^a-z0-9_]", "");
        
        logger.debug("Normalized '{}' to '{}'", input, normalized);
        
        return normalized;
    }
    
    public static String generateSlug(String input) {
        if (StringUtils.isBlank(input)) {
            return StringUtils.EMPTY;
        }
        
        String slug = normalize(input);
        slug = StringUtils.abbreviate(slug, 100);
        
        logger.debug("Generated slug '{}' from '{}'", slug, input);
        
        return slug;
    }
    
    public static String buildDetailedLog(String entityType, String action, Object... details) {
        StringBuilder builder = new StringBuilder();
        builder.append("[").append(entityType).append("] ");
        builder.append(action);
        
        for (Object detail : details) {
            builder.append(" | ").append(detail);
        }
        
        logger.debug(builder.toString());
        
        return builder.toString();
    }
}
