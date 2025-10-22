package com.trackademy.service.impl;

import com.trackademy.dto.*;
import com.trackademy.entity.Enrollment;
import com.trackademy.entity.User;
import com.trackademy.repository.EnrollmentRepository;
import com.trackademy.repository.EnrolledCourseRepository;
import com.trackademy.repository.UserRepository;
import com.trackademy.service.AlertService;
import com.trackademy.service.CalendarService;
import com.trackademy.service.MotivationService;
import com.trackademy.service.RecommendationService;
import com.trackademy.service.StudentService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Comparator;
 

@Service
@Transactional
public class StudentServiceImpl implements StudentService {

    private static final Logger logger = LoggerFactory.getLogger(StudentServiceImpl.class);
    private final UserRepository userRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final EnrolledCourseRepository enrolledCourseRepository;
    private final com.trackademy.repository.ProgramRepository programRepository;
    private final AlertService alertService;
    private final CalendarService calendarService;
    private final RecommendationService recommendationService;
    private final MotivationService motivationService;

    public StudentServiceImpl(UserRepository userRepository,
                              EnrollmentRepository enrollmentRepository,
                              EnrolledCourseRepository enrolledCourseRepository,
                              com.trackademy.repository.ProgramRepository programRepository,
                              AlertService alertService,
                              CalendarService calendarService,
                              RecommendationService recommendationService,
                              MotivationService motivationService) {
        this.userRepository = userRepository;
        this.enrollmentRepository = enrollmentRepository;
        this.enrolledCourseRepository = enrolledCourseRepository;
        this.programRepository = programRepository;
        this.alertService = alertService;
        this.calendarService = calendarService;
        this.recommendationService = recommendationService;
        this.motivationService = motivationService;
    }

    @Override
    public StudentMeDTO getMe(String userEmail) {
        User user = userRepository.findByEmail(userEmail).orElse(null);
        if (user == null) return null;

        StudentMeDTO.StudentMeDTOBuilder dtoBuilder = StudentMeDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .campusId(user.getPreferredCampus() != null ? user.getPreferredCampus().getId() : null)
                .programId(user.getPreferredProgram() != null ? parseProgramId(user.getPreferredProgram()) : null)
                .currentTerm(user.getPreferredCycle() != null ? Map.of("cycle", user.getPreferredCycle()) : null)
                .creditsApproved(null)
                .creditsRequired(null)
                .gpa(null);

        // populate readable names
        if (user.getPreferredCampus() != null) {
            dtoBuilder.campusName(user.getPreferredCampus().getName());
        }

        if (user.getPreferredProgram() != null) {
            Long pid = parseProgramId(user.getPreferredProgram());
            if (pid != null) {
                programRepository.findById(pid).ifPresent(p -> dtoBuilder.programName(p.getName()));
            }
        }

        // termLabel: try to show termCode if available or fallback to cycle
        if (user.getPreferredCycle() != null) {
            dtoBuilder.termLabel("Ciclo " + user.getPreferredCycle());
        }

        StudentMeDTO dto = dtoBuilder.build();

        // compute simple credit summary from enrollments
        try {
            List<Enrollment> enrollments = enrollmentRepository.findByUserId(user.getId());
            int totalCompleted = enrollments.stream().mapToInt(e -> e.getTotalCreditsCompleted() != null ? e.getTotalCreditsCompleted() : 0).sum();
            int totalInProgress = enrollments.stream().mapToInt(e -> e.getTotalCreditsInProgress() != null ? e.getTotalCreditsInProgress() : 0).sum();
            dto.setCreditsApproved(totalCompleted);
            dto.setCreditsRequired(null);
            // compute best (max) gpa across enrollments if any
            // compute max GPA (Enrollment.gpa is BigDecimal)
            java.util.Optional<BigDecimal> maxGpa = enrollments.stream().map(Enrollment::getGpa).filter(Objects::nonNull).max(Comparator.naturalOrder());
            dto.setGpa(maxGpa.map(BigDecimal::doubleValue).orElse(null));

            // compute enrolled courses count using enrolledCourseRepository
            int enrolledCoursesCount = enrollments.stream()
                    .mapToInt(enr -> enrolledCourseRepository.findByEnrollmentId(enr.getId()).size())
                    .sum();

            // attach progress info into currentTerm map
            Map<String, Object> termMap = dto.getCurrentTerm() != null ? dto.getCurrentTerm() : new java.util.HashMap<>();
            termMap.put("totalEnrollments", enrollments.size());
            termMap.put("creditsInProgress", totalInProgress);
            termMap.put("enrolledCoursesCount", enrolledCoursesCount);
            // attach a simple motivation score
            try {
                int motivationScore = motivationService.computeSimpleScore(user.getId());
                termMap.put("motivationScore", motivationScore);
            } catch (Exception ignore) {
            }
            dto.setCurrentTerm(termMap);
        } catch (Exception ex) {
            logger.warn("No se pudo calcular resumen de créditos para {}: {}", userEmail, ex.getMessage());
        }

        return dto;
    }

    private Long parseProgramId(String preferredProgram) {
        try {
            return Long.parseLong(preferredProgram);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    @Override
    public List<AlertItemDTO> getAlerts(String userEmail) {
        User user = userRepository.findByEmail(userEmail).orElse(null);
        if (user == null) return new ArrayList<>();
        return alertService.getAlertsForUser(user.getId());
    }

    @Override
    public List<CalendarEventDTO> getCalendar(String userEmail, String start, String end) {
        User user = userRepository.findByEmail(userEmail).orElse(null);
        if (user == null) return new ArrayList<>();
        LocalDateTime s = null, e = null;
        try {
            if (StringUtils.isNotBlank(start)) s = LocalDateTime.parse(start);
            if (StringUtils.isNotBlank(end)) e = LocalDateTime.parse(end);
        } catch (DateTimeParseException dex) {
            logger.warn("Invalid calendar range params: start={} end={}", start, end);
        }
        // fallback to 30 days window if nulls
        if (s == null) s = LocalDateTime.now().minusDays(7);
        if (e == null) e = LocalDateTime.now().plusDays(30);
        return calendarService.getEventsForUser(user.getId(), s, e);
    }

    @Override
    public List<RecommendationDTO> getRecommendations(String userEmail, int limit) {
        List<RecommendationDTO> out = new ArrayList<>();
        User user = userRepository.findByEmail(userEmail).orElse(null);
        if (user == null) return out;

        List<com.trackademy.dto.CourseDTO> courses = recommendationService.recommendCoursesForUser(user.getId(), limit);
        for (com.trackademy.dto.CourseDTO c : courses) {
            String reason = "Suggested";
            // try to give a friendly reason
            if (user.getPreferredCycle() != null && c.getCredits() != null && c.getCredits() > 3) {
                reason = "Buena carga para próximo ciclo";
            }
            RecommendationDTO r = RecommendationDTO.builder()
                    .type("course")
                    .courseId(c.getId())
                    .code(c.getCode())
                    .reason(reason)
                    .build();
            out.add(r);
        }
        return out;
    }
}
