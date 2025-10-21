package com.trackademy.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.trackademy.entity.Course;
import com.trackademy.entity.EnrolledCourse;

public class DataComparator {
    
    private static final Logger logger = LoggerFactory.getLogger(DataComparator.class);
    
    public static boolean isCourseDataChanged(Course oldCourse, Course newCourse) {
        if (oldCourse == null || newCourse == null) {
            logger.warn("Null course in comparison");
            return true;
        }
        
        boolean changed = !new EqualsBuilder()
                .append(oldCourse.getCode(), newCourse.getCode())
                .append(StringUtils.trim(oldCourse.getName()), StringUtils.trim(newCourse.getName()))
                .append(oldCourse.getCredits(), newCourse.getCredits())
                .isEquals();
        
        if (changed) {
            logger.info("Course {} data has changed", oldCourse.getId());
        }
        
        return changed;
    }
    
    public static boolean isEnrolledCourseDataChanged(EnrolledCourse old, EnrolledCourse newCourse) {
        if (old == null || newCourse == null) {
            logger.warn("Null enrolled course in comparison");
            return true;
        }
        
        boolean changed = !new EqualsBuilder()
                .append(old.getGrade(), newCourse.getGrade())
                .append(old.getStatus(), newCourse.getStatus())
                .append(old.getAttendancePercentage(), newCourse.getAttendancePercentage())
                .isEquals();
        
        if (changed) {
            logger.info("Enrolled course {} data has changed", old.getId());
        }
        
        return changed;
    }
    
    public static int compareStatusValues(String status1, String status2) {
        String normalized1 = StringUtils.upperCase(StringUtils.trim(status1));
        String normalized2 = StringUtils.upperCase(StringUtils.trim(status2));
        
        logger.debug("Comparing status: {} vs {}", normalized1, normalized2);
        return normalized1.compareTo(normalized2);
    }
}
