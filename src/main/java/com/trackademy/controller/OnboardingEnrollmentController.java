package com.trackademy.controller;

import com.trackademy.dto.CurrentCoursesRequest;
import com.trackademy.entity.*;
import com.trackademy.repository.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/onboarding")
public class OnboardingEnrollmentController {

    private final UserRepository userRepository;
    private final TermRepository termRepository;
    private final CampusRepository campusRepository;
    private final ProgramRepository programRepository;
    private final CourseRepository courseRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final EnrolledCourseRepository enrolledCourseRepository;

    public OnboardingEnrollmentController(UserRepository userRepository,
                                          TermRepository termRepository,
                                          CampusRepository campusRepository,
                                          ProgramRepository programRepository,
                                          CourseRepository courseRepository,
                                          EnrollmentRepository enrollmentRepository,
                                          EnrolledCourseRepository enrolledCourseRepository) {
        this.userRepository = userRepository;
        this.termRepository = termRepository;
        this.campusRepository = campusRepository;
        this.programRepository = programRepository;
        this.courseRepository = courseRepository;
        this.enrollmentRepository = enrollmentRepository;
        this.enrolledCourseRepository = enrolledCourseRepository;
    }

    @PostMapping("/courses")
    public ResponseEntity<?> setCurrentCourses(@RequestBody CurrentCoursesRequest request, Authentication authentication) {
        if (request == null || request.getCourses() == null || request.getCourses().isEmpty()) {
            return ResponseEntity.badRequest().body("courses required");
        }

        // resolve user from authentication token and ignore any userId coming from the client
        String email = null;
        if (authentication instanceof JwtAuthenticationToken jwtAuth) {
            Object claim = jwtAuth.getToken().getClaims().get("email");
            if (claim == null) claim = jwtAuth.getToken().getClaims().get("preferred_username");
            if (claim != null) email = claim.toString();
        }
        if (email == null && authentication != null) email = authentication.getName();

        if (email == null) {
            return ResponseEntity.status(401).body("unauthenticated");
        }

        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null) {
            return ResponseEntity.status(404).body("user not found");
        }

        Term term = null;
        if (request.getTermId() != null) {
            term = termRepository.findById(request.getTermId()).orElse(null);
        } else if (request.getTermCode() != null) {
            term = termRepository.findByCode(request.getTermCode()).orElse(null);
        }
        if (term == null) {
            return ResponseEntity.badRequest().body("term required");
        }

        Campus campus = null;
        if (request.getCampusId() != null) {
            campus = campusRepository.findById(request.getCampusId()).orElse(null);
        } else if (user.getPreferredCampus() != null) {
            campus = user.getPreferredCampus();
        }

        Program program = null;
        if (request.getProgramId() != null) {
            program = programRepository.findById(request.getProgramId()).orElse(null);
        } else if (request.getProgramSlug() != null) {
            program = programRepository.findBySlug(request.getProgramSlug()).orElse(null);
        } else if (user.getPreferredProgram() != null) {
            try {
                Long pid = Long.parseLong(user.getPreferredProgram());
                program = programRepository.findById(pid).orElse(null);
            } catch (NumberFormatException ignore) {
                program = programRepository.findBySlug(user.getPreferredProgram()).orElse(null);
            }
        }

        if (campus == null || program == null) {
            return ResponseEntity.badRequest().body("campus/program required");
        }

    // ensure enrollment is for authenticated user
    Enrollment enrollment = enrollmentRepository.findByUserIdAndTermId(user.getId(), term.getId()).orElse(null);
        if (enrollment == null) {
            enrollment = Enrollment.builder()
                    .userId(user.getId())
                    .term(term)
                    .campus(campus)
                    .program(program)
                    .currentCycle(user.getPreferredCycle() != null ? user.getPreferredCycle() : 1)
                    .entryCycle(user.getPreferredCycle() != null ? user.getPreferredCycle() : 1)
                    .status("ACTIVE")
                    .enrollmentDate(LocalDate.now())
                    .build();
            enrollment = enrollmentRepository.save(enrollment);
        }

        for (CurrentCoursesRequest.CourseRef ref : request.getCourses()) {
            Course course = null;
            if (ref.getCourseId() != null) {
                course = courseRepository.findById(ref.getCourseId()).orElse(null);
            } else if (ref.getCourseCode() != null) {
                course = courseRepository.findByCode(ref.getCourseCode()).orElse(null);
            }
            if (course == null) continue;
            Long enrollmentId = enrollment.getId();
            boolean exists = enrolledCourseRepository
                    .findByEnrollmentIdAndCourseIdAndTermId(enrollmentId, course.getId(), term.getId())
                    .isPresent();
            if (!exists) {
                EnrolledCourse ec = EnrolledCourse.builder()
                        .enrollment(enrollment)
                        .course(course)
                        .term(term)
                        .status("IN_PROGRESS")
                        .build();
                enrolledCourseRepository.save(ec);
            }
        }

        return ResponseEntity.ok().build();
    }
}
