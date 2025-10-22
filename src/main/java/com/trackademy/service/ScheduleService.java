package com.trackademy.service;

import com.trackademy.dto.UserCourseScheduleDto;
import com.trackademy.entity.CourseSchedule;
import com.trackademy.entity.EnrolledCourse;
import com.trackademy.entity.Enrollment;
import com.trackademy.entity.User;
import com.trackademy.repository.CourseRepository;
import com.trackademy.repository.CourseScheduleRepository;
import com.trackademy.repository.EnrolledCourseRepository;
import com.trackademy.repository.EnrollmentRepository;
import com.trackademy.repository.StudentCourseScheduleRepository;
import com.trackademy.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class ScheduleService {

    private final UserRepository userRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final EnrolledCourseRepository enrolledCourseRepository;
    private final StudentCourseScheduleRepository studentCourseScheduleRepository;
    private final CourseScheduleRepository courseScheduleRepository;
    private final CourseRepository courseRepository;

    public ScheduleService(UserRepository userRepository,
                           EnrollmentRepository enrollmentRepository,
                           EnrolledCourseRepository enrolledCourseRepository,
                           StudentCourseScheduleRepository studentCourseScheduleRepository,
                           CourseScheduleRepository courseScheduleRepository,
                           CourseRepository courseRepository) {
        this.userRepository = userRepository;
        this.enrollmentRepository = enrollmentRepository;
        this.enrolledCourseRepository = enrolledCourseRepository;
        this.studentCourseScheduleRepository = studentCourseScheduleRepository;
        this.courseScheduleRepository = courseScheduleRepository;
        this.courseRepository = courseRepository;
    }

    public List<UserCourseScheduleDto> getMyCurrentSchedule(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new NoSuchElementException("Usuario no encontrado"));

        List<Enrollment> enrollments = enrollmentRepository.findByUserId(user.getId());
        if (enrollments.isEmpty()) {
            return List.of();
        }

        Enrollment latest = enrollments.stream()
                .max(Comparator.comparing(Enrollment::getEnrollmentDate, Comparator.nullsLast(Comparator.naturalOrder()))
                        .thenComparing(Enrollment::getId))
                .orElse(enrollments.get(0));

        List<EnrolledCourse> enrolledCourses = enrolledCourseRepository.findByEnrollmentId(latest.getId());

        return enrolledCourses.stream().flatMap(ec ->
                studentCourseScheduleRepository.findByEnrolledCourseId(ec.getId()).stream().map(scs -> {
                    CourseSchedule cs = courseScheduleRepository.findById(scs.getCourseSchedule().getId())
                            .orElse(null);
                    var course = ec.getCourse() != null ? courseRepository.findById(ec.getCourse().getId()).orElse(null) : null;
                    return UserCourseScheduleDto.builder()
                            .enrolledCourseId(ec.getId())
                            .courseId(course != null ? course.getId() : null)
                            .courseCode(course != null ? course.getCode() : null)
                            .courseName(course != null ? course.getName() : null)
                            .credits(course != null ? course.getCredits() : null)
                            .courseScheduleId(cs != null ? cs.getId() : null)
                            .dayOfWeek(cs != null ? cs.getDayOfWeek() : null)
                            .startTime(cs != null ? cs.getStartTime() : null)
                            .endTime(cs != null ? cs.getEndTime() : null)
                            .section(cs != null ? cs.getSection() : null)
                            .classroom(cs != null ? cs.getClassroom() : null)
                            .building(cs != null ? cs.getBuilding() : null)
                            .teacherName(cs != null ? cs.getTeacherName() : null)
                            .modality(cs != null ? cs.getModality() : null)
                            .build();
                })
        ).collect(Collectors.toList());
    }
}
