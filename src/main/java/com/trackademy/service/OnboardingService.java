package com.trackademy.service;

import com.trackademy.dto.OnboardingRequestDto;
import com.trackademy.dto.OnboardingResponseDto;
import com.trackademy.entity.User;
import com.trackademy.entity.Campus;
import com.trackademy.repository.UserRepository;
import com.trackademy.repository.CampusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.NoSuchElementException;

@Service
@Transactional
public class OnboardingService {

    private static final Logger logger = LoggerFactory.getLogger(OnboardingService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CampusRepository campusRepository;

    public OnboardingResponseDto saveOnboarding(OnboardingRequestDto request, String userEmail) {
        try {
            User user = userRepository.findByEmail(userEmail)
                    .orElseThrow(() -> new NoSuchElementException("Usuario no encontrado"));

            user.setWantsAlerts(request.getWantsAlerts());
            user.setWantsIncentives(request.getWantsIncentives());
            user.setAllowDataSharing(request.getAllowDataSharing());
            user.setOnboarded(true);

            if (request.getCampus() != null) {
                Campus campus;
                String campusVal = String.valueOf(request.getCampus());
                if (campusVal.matches("^\\d+$")) {
                    Long id = Long.parseLong(campusVal);
                    campus = campusRepository.findById(id)
                            .orElseThrow(() -> new NoSuchElementException("Campus no encontrado"));
                } else {
                    campus = campusRepository.findByName(campusVal)
                            .orElseThrow(() -> new NoSuchElementException("Campus no encontrado"));
                }
                user.setPreferredCampus(campus);
            }

            if (request.getCycle() != null) {
                user.setPreferredCycle(request.getCycle());
            }

            if (request.getProgram() != null) {
                user.setPreferredProgram(String.valueOf(request.getProgram()));
            }

            if (request.getSpecialization() != null) {
                user.setSpecialization(request.getSpecialization());
            }

            if (request.getCareerInterests() != null) {
                user.setCareerInterests(String.join(",", request.getCareerInterests()));
            }

            if (request.getStudyHoursPerDay() != null) {
                user.setStudyHoursPerDay(request.getStudyHoursPerDay());
            }

            if (request.getLearningStyle() != null) {
                user.setLearningStyle(request.getLearningStyle());
            }

            if (request.getMotivationFactors() != null) {
                user.setMotivationFactors(String.join(",", request.getMotivationFactors()));
            }

            if (request.getPreferredStudyTimes() != null) {
                user.setPreferredStudyTimes(String.join(",", request.getPreferredStudyTimes()));
            }

            if (request.getWorkHoursPerWeek() != null) {
                user.setWorkHoursPerWeek(request.getWorkHoursPerWeek());
            }

            if (request.getExtracurricularHoursPerWeek() != null) {
                user.setExtracurricularHoursPerWeek(request.getExtracurricularHoursPerWeek());
            }

            if (request.getWeeklyAvailabilityJson() != null) {
                user.setWeeklyAvailability(request.getWeeklyAvailabilityJson());
            }

            userRepository.save(user);

            logger.info("Onboarding completado para usuario: {}", userEmail);

            return OnboardingResponseDto.builder()
                    .success(true)
                    .message("Onboarding completado exitosamente")
                    .userId(user.getId())
                    .campus(request.getCampus())
                    .cycle(request.getCycle())
                    .build();

        } catch (NoSuchElementException e) {
            logger.error("Error en onboarding: {}", e.getMessage());
            return OnboardingResponseDto.builder()
                    .success(false)
                    .message(e.getMessage())
                    .build();
        } catch (Exception e) {
            logger.error("Error inesperado en onboarding", e);
            return OnboardingResponseDto.builder()
                    .success(false)
                    .message("Error al procesar onboarding")
                    .build();
        }
    }

    public OnboardingRequestDto getOnboarding(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new NoSuchElementException("Usuario no encontrado"));

        return OnboardingRequestDto.builder()
                .campus(user.getPreferredCampus() != null ? user.getPreferredCampus().getName() : null)
                .cycle(user.getPreferredCycle())
                .wantsAlerts(user.getWantsAlerts())
                .wantsIncentives(user.getWantsIncentives())
                .allowDataSharing(user.getAllowDataSharing())
        .preferredStudyTimes(user.getPreferredStudyTimes() != null ? java.util.Arrays.asList(user.getPreferredStudyTimes().split(",")) : null)
        .workHoursPerWeek(user.getWorkHoursPerWeek())
        .extracurricularHoursPerWeek(user.getExtracurricularHoursPerWeek())
        .weeklyAvailabilityJson(user.getWeeklyAvailability())
                .build();
    }

    public OnboardingResponseDto updateOnboarding(OnboardingRequestDto request, String userEmail) {
        return saveOnboarding(request, userEmail);
    }

    public boolean isOnboarded(String userEmail) {
        return userRepository.findByEmail(userEmail)
                .map(u -> Boolean.TRUE.equals(u.getOnboarded()))
                .orElse(false);
    }
}
