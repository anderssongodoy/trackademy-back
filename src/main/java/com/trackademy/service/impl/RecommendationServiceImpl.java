package com.trackademy.service.impl;

import com.trackademy.dto.CourseDTO;
import com.trackademy.entity.CurriculumVersion;
import com.trackademy.entity.User;
import com.trackademy.repository.CourseRepository;
import com.trackademy.repository.EnrolledCourseRepository;
import com.trackademy.repository.CurriculumCourseRepository;
import com.trackademy.repository.CurriculumVersionRepository;
import com.trackademy.repository.UserRepository;
import com.trackademy.repository.EnrollmentRepository;
import com.trackademy.service.RecommendationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class RecommendationServiceImpl implements RecommendationService {

    private final CourseRepository courseRepository;
    private final EnrolledCourseRepository enrolledCourseRepository;
    private final CurriculumCourseRepository curriculumCourseRepository;
    private final CurriculumVersionRepository curriculumVersionRepository;
    private final UserRepository userRepository;
    private final EnrollmentRepository enrollmentRepository;

    public RecommendationServiceImpl(CourseRepository courseRepository,
                                     EnrolledCourseRepository enrolledCourseRepository,
                                     CurriculumCourseRepository curriculumCourseRepository,
                                     CurriculumVersionRepository curriculumVersionRepository,
                                     UserRepository userRepository,
                                     EnrollmentRepository enrollmentRepository) {
        this.courseRepository = courseRepository;
        this.enrolledCourseRepository = enrolledCourseRepository;
        this.curriculumCourseRepository = curriculumCourseRepository;
        this.curriculumVersionRepository = curriculumVersionRepository;
        this.userRepository = userRepository;
        this.enrollmentRepository = enrollmentRepository;
    }

    @Override
    public List<CourseDTO> recommendCoursesForUser(Long userId, int limit) {
        // Simple heuristic: recommend top courses by credits/weeklyHours that the user is NOT enrolled in
    // build a set of course ids the user is already enrolled in
    java.util.Set<Long> enrolledCourseIds = enrollmentRepository.findByUserId(userId)
        .stream()
        .flatMap(enr -> enrolledCourseRepository.findByEnrollmentId(enr.getId()).stream())
        .map(ec -> ec.getCourse().getId())
        .collect(Collectors.toSet());
    // try to recommend by next cycle based on user's preferred program/cycle
    User user = userRepository.findById(userId).orElse(null);
    if (user != null && user.getPreferredProgram() != null && user.getPreferredCycle() != null) {
        try {
        Long programId = Long.parseLong(user.getPreferredProgram());
        int nextCycle = user.getPreferredCycle() + 1;
        // find active curriculum version for the program (pick highest version if multiple)
        List<CurriculumVersion> cvs = curriculumVersionRepository.findByProgramId(programId);
        CurriculumVersion chosen = cvs.stream().filter(v -> Boolean.TRUE.equals(v.getIsActive())).findFirst()
            .orElseGet(() -> cvs.stream().max((a,b) -> Integer.compare(a.getVersionNumber(), b.getVersionNumber())).orElse(null));
        if (chosen != null) {
            // curriculum courses for next cycle
            List<Long> nextCycleCourseIds = curriculumCourseRepository.findByCurriculumVersionId(chosen.getId())
                .stream().filter(cc -> cc.getCycleNumber() != null && cc.getCycleNumber() == nextCycle)
                .map(cc -> cc.getCourse().getId())
                .filter(id -> !enrolledCourseIds.contains(id))
                .collect(Collectors.toList());

            if (!nextCycleCourseIds.isEmpty()) {
            return courseRepository.findAllById(nextCycleCourseIds)
                .stream()
                .map(c -> CourseDTO.builder().id(c.getId()).code(c.getCode()).name(c.getName()).credits(c.getCredits()).weeklyHours(c.getWeeklyHours()).modality(c.getModality()).build())
                .limit(limit)
                .collect(Collectors.toList());
            }
        }
        } catch (NumberFormatException ignored) {
        // preferredProgram was not numeric, fallback to general heuristic below
        }
    }

    // fallback: global top by credits+weeklyHours, excluding enrolled courses
    return courseRepository.findAll()
        .stream()
        .filter(c -> !enrolledCourseIds.contains(c.getId()))
        .sorted((a,b) -> Integer.compare(
            (b.getCredits() != null ? b.getCredits() : 0) + (b.getWeeklyHours() != null ? b.getWeeklyHours() : 0),
            (a.getCredits() != null ? a.getCredits() : 0) + (a.getWeeklyHours() != null ? a.getWeeklyHours() : 0)
        ))
        .limit(limit)
        .map(c -> CourseDTO.builder().id(c.getId()).code(c.getCode()).name(c.getName()).credits(c.getCredits()).weeklyHours(c.getWeeklyHours()).modality(c.getModality()).build())
        .collect(Collectors.toList());
    }
}
