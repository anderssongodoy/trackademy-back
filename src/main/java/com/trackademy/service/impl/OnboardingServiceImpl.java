package com.trackademy.service.impl;

import com.trackademy.domain.entity.*;
import com.trackademy.domain.repo.*;
import com.trackademy.dto.OnboardingRequest;
import com.trackademy.dto.UsuarioCursoResumenDto;
import com.trackademy.dto.UsuarioEvaluacionDto;
import com.trackademy.service.OnboardingService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OnboardingServiceImpl implements OnboardingService {
    private static final Logger log = LoggerFactory.getLogger(OnboardingServiceImpl.class);

    private final UsuarioRepository usuarioRepository;
    private final UsuarioPerfilRepository usuarioPerfilRepository;
    private final UsuarioCursoRepository usuarioCursoRepository;
    private final UsuarioEvaluacionRepository usuarioEvaluacionRepository;
    private final CursoRepository cursoRepository;
    private final EvaluacionRepository evaluacionRepository;
    private final CampusRepository campusRepository;
    private final PeriodoRepository periodoRepository;
    private final CarreraRepository carreraRepository;

    @Override
    @Transactional
    public List<UsuarioCursoResumenDto> onboard(String userSubject, String email, String nombre, String avatar, OnboardingRequest request) {
        log.info("Onboarding para sub={}, cursos={}", userSubject, request.cursoIds());

        Usuario usuario = usuarioRepository.findBySubject(userSubject)
                .orElseGet(() -> usuarioRepository.save(Usuario.builder()
                        .subject(userSubject)
                        .email(StringUtils.defaultString(email))
                        .nombre(StringUtils.defaultString(nombre))
                        .avatar(StringUtils.abbreviate(StringUtils.defaultString(avatar), 1_000_000))
                        .build()));

        boolean changed = false;
        if (email != null && !StringUtils.equals(usuario.getEmail(), email)) { usuario.setEmail(email); changed = true; }
        if (nombre != null && !StringUtils.equals(usuario.getNombre(), nombre)) { usuario.setNombre(nombre); changed = true; }
        if (avatar != null && !StringUtils.equals(usuario.getAvatar(), avatar)) { usuario.setAvatar(StringUtils.abbreviate(avatar, 1_000_000)); changed = true; }
        if (changed) { usuarioRepository.save(usuario); }

        var campus = campusRepository.findById(request.campusId()).orElseThrow();
        var periodo = periodoRepository.findById(request.periodoId()).orElseThrow();
        var carrera = carreraRepository.findById(request.carreraId()).orElseThrow();

        UsuarioPerfil perfil = usuarioPerfilRepository.findByUsuarioId(usuario.getId())
        .orElse(null);

        if (perfil == null) {
            perfil = new UsuarioPerfil();
            perfil.setUsuario(usuario);
        }

        perfil.setCampus(campus);   
        perfil.setPeriodo(periodo);
        perfil.setCarrera(carrera);

        usuarioPerfilRepository.save(perfil);

        List<UsuarioCursoResumenDto> respuesta = new ArrayList<>();

        for (Long cursoId : request.cursoIds()) {
            Curso curso = cursoRepository.findById(cursoId).orElseThrow();

            UsuarioCurso uc = usuarioCursoRepository.findByUsuarioIdAndCursoId(usuario.getId(), cursoId)
                    .orElseGet(() -> usuarioCursoRepository.save(UsuarioCurso.builder()
                            .usuario(usuario)
                            .curso(curso)
                            .build()));

            var evalsOrigen = evaluacionRepository.findByCursoIdOrderBySemanaAsc(cursoId);
            List<UsuarioEvaluacion> clones = new ArrayList<>();
            for (Evaluacion ev : evalsOrigen) {
                UsuarioEvaluacion ue = UsuarioEvaluacion.builder()
                        .usuarioCurso(uc)
                        .evaluacionOriginal(ev)
                        .semana(ev.getSemana())
                        .porcentaje(ev.getPorcentaje())
                        .fechaEstimada(calcularFechaEstimada(periodo.getFechaInicio(), ev.getSemana()))
                        .nota(null)
                        .build();
                clones.add(ue);
            }
            if (!clones.isEmpty()) {
                usuarioEvaluacionRepository.saveAll(clones);
            }

            var evalDtos = usuarioEvaluacionRepository.findByUsuarioCursoIdOrderBySemanaAsc(uc.getId())
                    .stream()
                    .map(ue -> new UsuarioEvaluacionDto(
                            ue.getId(),
                            ue.getEvaluacionOriginal().getCodigo(),
                            ue.getEvaluacionOriginal().getDescripcion(),
                            ue.getSemana(), ue.getPorcentaje(), ue.getFechaEstimada(),
                            ue.getNota() == null ? null : ue.getNota().toPlainString()
                    ))
                    .collect(Collectors.toList());

            respuesta.add(new UsuarioCursoResumenDto(uc.getId(), curso.getId(), curso.getNombre(), evalDtos));
        }

        return respuesta;
    }

    private LocalDate calcularFechaEstimada(LocalDate fechaInicioPeriodo, Integer semana) {
        if (fechaInicioPeriodo == null || semana == null || semana <= 0) return null;
        // Ajustar al lunes de la semana 1
        LocalDate startMonday = fechaInicioPeriodo;
        if (startMonday.getDayOfWeek() != DayOfWeek.MONDAY) {
            startMonday = startMonday.minusDays((startMonday.getDayOfWeek().getValue() + 6) % 7);
        }
        return startMonday.plusWeeks(semana - 1L);
    }
}
