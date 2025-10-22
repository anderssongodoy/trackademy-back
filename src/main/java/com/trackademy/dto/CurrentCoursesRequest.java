package com.trackademy.dto;

import java.util.List;

public class CurrentCoursesRequest {
    private Long termId;
    private String termCode;
    private Long campusId;
    private Long programId;
    private String programSlug;
    private List<CourseRef> courses;

    public Long getTermId() { return termId; }
    public void setTermId(Long termId) { this.termId = termId; }
    public String getTermCode() { return termCode; }
    public void setTermCode(String termCode) { this.termCode = termCode; }
    public Long getCampusId() { return campusId; }
    public void setCampusId(Long campusId) { this.campusId = campusId; }
    public Long getProgramId() { return programId; }
    public void setProgramId(Long programId) { this.programId = programId; }
    public String getProgramSlug() { return programSlug; }
    public void setProgramSlug(String programSlug) { this.programSlug = programSlug; }
    public List<CourseRef> getCourses() { return courses; }
    public void setCourses(List<CourseRef> courses) { this.courses = courses; }

    public static class CourseRef {
        private Long courseId;
        private String courseCode;

        public Long getCourseId() { return courseId; }
        public void setCourseId(Long courseId) { this.courseId = courseId; }
        public String getCourseCode() { return courseCode; }
        public void setCourseCode(String courseCode) { this.courseCode = courseCode; }
    }
}
