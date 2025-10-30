package com.trackademy.service.impl;

import com.trackademy.domain.entity.*;
import com.trackademy.domain.repo.*;
import com.trackademy.dto.*;
import com.trackademy.service.MeService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MeServiceImpl implements MeService {
    private static final Logger log = LoggerFactory.getLogger(MeServiceImpl.class);

    private final UsuarioRepository usuarioRepository;
    private final UsuarioCursoRepository usuarioCursoRepository;
    private final UsuarioCursoResumenRepository usuarioCursoResumenRepository;
    private final UsuarioEvaluacionRepository usuarioEvaluacionRepository;
    private final RecomendacionRepository recomendacionRepository;
    private final ReglaRecordatorioRepository reglaRecordatorioRepository;
    private final UsuarioHabitoRepository usuarioHabitoRepository;
    private final UsuarioHabitoLogRepository usuarioHabitoLogRepository;
    private final UsuarioCursoHorarioRepository usuarioCursoHorarioRepository;

    private Usuario requireUsuario(String subject) {
        return usuarioRepository.findBySubject(subject)
                .orElseThrow(() -> new IllegalStateException("Usuario no encontrado para subject"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<UsuarioCursoResumenDto> cursos(String userSubject) {
        Usuario u = requireUsuario(userSubject);
        return usuarioCursoRepository.findByUsuarioId(u.getId()).stream()
                .map(uc -> {
                    var evalDtos = usuarioEvaluacionRepository.findByUsuarioCursoIdOrderBySemanaAsc(uc.getId()).stream()
                            .map(ue -> new UsuarioEvaluacionDto(
                                    ue.getId(), ue.getEvaluacionOriginal().getCodigo(), ue.getEvaluacionOriginal().getDescripcion(),
                                    ue.getSemana(), ue.getPorcentaje(), ue.getFechaEstimada(),
                                    ue.getNota() == null ? null : ue.getNota().toPlainString()
                            ))
                            .collect(Collectors.toList());
                    return new UsuarioCursoResumenDto(uc.getId(), uc.getCurso().getId(), uc.getCurso().getNombre(), evalDtos);
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<UsuarioEvaluacionDto> proximasEvaluaciones(String userSubject) {
        Usuario u = requireUsuario(userSubject);
        LocalDate today = LocalDate.now();
        LocalDate limit = today.plusWeeks(3);
        List<UsuarioEvaluacionDto> out = new ArrayList<>();
        for (UsuarioCurso uc : usuarioCursoRepository.findByUsuarioId(u.getId())) {
            var list = usuarioEvaluacionRepository.findByUsuarioCursoIdOrderBySemanaAsc(uc.getId());
            for (UsuarioEvaluacion ue : list) {
                if (ue.getFechaEstimada() != null && !ue.getFechaEstimada().isBefore(today) && !ue.getFechaEstimada().isAfter(limit)) {
                    out.add(new UsuarioEvaluacionDto(ue.getId(), ue.getEvaluacionOriginal().getCodigo(), ue.getEvaluacionOriginal().getDescripcion(), ue.getSemana(), ue.getPorcentaje(), ue.getFechaEstimada(), ue.getNota() == null ? null : ue.getNota().toPlainString()));
                }
            }
        }
        return out;
    }

    @Override
    @Transactional
    public void registrarNota(String userSubject, Long usuarioEvaluacionId, String notaStr) {
        Usuario u = requireUsuario(userSubject);
        UsuarioEvaluacion ue = usuarioEvaluacionRepository.findById(usuarioEvaluacionId).orElseThrow();
        if (!ue.getUsuarioCurso().getUsuario().getId().equals(u.getId())) {
            throw new IllegalArgumentException("Evaluación no pertenece al usuario");
        }
        BigDecimal n = new BigDecimal(StringUtils.trim(notaStr));
        ue.setNota(n);
        // Recalcular resumen
        recalcResumen(ue.getUsuarioCurso());
    }

    private void recalcResumen(UsuarioCurso uc) {
        var evaluaciones = usuarioEvaluacionRepository.findByUsuarioCursoIdOrderBySemanaAsc(uc.getId());
        BigDecimal sumNotas = BigDecimal.ZERO;
        BigDecimal sumPesosConNota = BigDecimal.ZERO;
        BigDecimal sumPesosPend = BigDecimal.ZERO;
        for (UsuarioEvaluacion e : evaluaciones) {
            if (e.getNota() != null) {
                sumNotas = sumNotas.add(e.getNota().multiply(e.getPorcentaje()));
                sumPesosConNota = sumPesosConNota.add(e.getPorcentaje());
            } else {
                sumPesosPend = sumPesosPend.add(e.getPorcentaje());
            }
        }
        BigDecimal promedioParcial = sumPesosConNota.compareTo(BigDecimal.ZERO) > 0 ? sumNotas.divide(sumPesosConNota, 2, BigDecimal.ROUND_HALF_UP) : null;
        BigDecimal notaNecesaria = null;
        if (sumPesosPend.compareTo(BigDecimal.ZERO) > 0) {
            // objetivo 12; resolver n necesaria si todo el peso pendiente se obtiene con la misma nota
            BigDecimal objetivo = BigDecimal.valueOf(12);
            if (promedioParcial != null) {
                BigDecimal totalActual = promedioParcial.multiply(sumPesosConNota);
                BigDecimal requerido = objetivo.multiply(BigDecimal.valueOf(100)).subtract(totalActual);
                notaNecesaria = requerido.divide(sumPesosPend, 2, BigDecimal.ROUND_HALF_UP);
            } else {
                notaNecesaria = objetivo; // sin notas aún, se asume peso pendiente total
            }
        }

        UsuarioCursoResumen res = usuarioCursoResumenRepository.findByUsuarioCursoId(uc.getId())
                .orElseGet(() -> usuarioCursoResumenRepository.save(UsuarioCursoResumen.builder().usuarioCurso(uc).build()));
        res.setPromedioParcial(promedioParcial);
        res.setNotaNecesaria(notaNecesaria);
    }

    @Override
    @Transactional
    public void upsertPreferenciasRecordatorios(String userSubject, int anticipacionDias) {
        Usuario u = requireUsuario(userSubject);
        var list = reglaRecordatorioRepository.findByUsuarioIdAndActivoTrue(u.getId());
        if (list.isEmpty()) {
            reglaRecordatorioRepository.save(ReglaRecordatorio.builder().usuario(u).anticipacionDias(anticipacionDias).activo(true).build());
        } else {
            for (ReglaRecordatorio r : list) { r.setAnticipacionDias(anticipacionDias); }
        }
    }

    @Override
    @Transactional
    public void upsertHorario(String userSubject, List<HorarioEntryDto> entries) {
        Usuario u = requireUsuario(userSubject);
        for (HorarioEntryDto e : entries) {
            UsuarioCurso uc = usuarioCursoRepository.findById(e.usuarioCursoId()).orElseThrow();
            if (!uc.getUsuario().getId().equals(u.getId())) throw new IllegalArgumentException("Curso no pertenece al usuario");
            UsuarioCursoHorario h = UsuarioCursoHorario.builder()
                    .usuarioCurso(uc)
                    .diaSemana(e.diaSemana())
                    .horaInicio(LocalTime.parse(e.horaInicio()))
                    .duracionMin(e.duracionMin())
                    .build();
            usuarioCursoHorarioRepository.save(h);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public java.util.List<HorarioEntryDto> listarHorario(String userSubject, Long usuarioCursoId) {
        Usuario u = requireUsuario(userSubject);
        List<UsuarioCursoHorario> list = (usuarioCursoId != null)
                ? usuarioCursoHorarioRepository.findByUsuarioCursoId(usuarioCursoId)
                : usuarioCursoHorarioRepository.findByUsuarioCursoUsuarioId(u.getId());
        return list.stream()
                .filter(h -> h.getUsuarioCurso().getUsuario().getId().equals(u.getId()))
                .map(h -> new HorarioEntryDto(
                        h.getUsuarioCurso().getId(),
                        h.getDiaSemana(),
                        h.getHoraInicio().toString(),
                        h.getDuracionMin()
                ))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public java.util.List<PreferenciaDiaItemDto> listarPreferenciasDia(String userSubject) {
        Usuario u = requireUsuario(userSubject);
        List<UsuarioCurso> cursos = usuarioCursoRepository.findByUsuarioId(u.getId());
        List<PreferenciaDiaItemDto> out = new ArrayList<>();
        for (UsuarioCurso uc : cursos) {
            List<UsuarioCursoHorario> canon = usuarioCursoHorarioRepository
                    .findByUsuarioCursoIdAndHoraInicioAndDuracionMin(uc.getId(), LocalTime.MIDNIGHT, 1);
            Integer dia = null;
            if (!canon.isEmpty()) {
                dia = canon.get(0).getDiaSemana();
            } else {
                var all = usuarioCursoHorarioRepository.findByUsuarioCursoId(uc.getId());
                if (!all.isEmpty()) dia = all.get(0).getDiaSemana();
            }
            if (dia != null) out.add(new PreferenciaDiaItemDto(uc.getId(), dia));
        }
        return out;
    }

    @Override
    @Transactional
    public void upsertPreferenciasDia(String userSubject, PreferenciasDiaRequest request) {
        Usuario u = requireUsuario(userSubject);
        if (request == null || request.items() == null) return;
        for (PreferenciaDiaItemDto item : request.items()) {
            UsuarioCurso uc = usuarioCursoRepository.findById(item.usuarioCursoId()).orElseThrow();
            if (!uc.getUsuario().getId().equals(u.getId())) throw new IllegalArgumentException("Curso no pertenece al usuario");
            var prev = usuarioCursoHorarioRepository
                    .findByUsuarioCursoIdAndHoraInicioAndDuracionMin(uc.getId(), LocalTime.MIDNIGHT, 1);
            prev.forEach(h -> usuarioCursoHorarioRepository.deleteById(h.getId()));
            UsuarioCursoHorario marker = UsuarioCursoHorario.builder()
                    .usuarioCurso(uc)
                    .diaSemana(item.diaSemana())
                    .horaInicio(LocalTime.MIDNIGHT)
                    .duracionMin(1)
                    .build();
            usuarioCursoHorarioRepository.save(marker);
        }
    }

    @Override
    @Transactional
    public Long crearHabito(String userSubject, HabitoCreateRequest request) {
        Usuario u = requireUsuario(userSubject);
        UsuarioHabito h = usuarioHabitoRepository.save(UsuarioHabito.builder()
                .usuario(u)
                .titulo(request.titulo())
                .frecuencia(StringUtils.defaultString(request.frecuencia(), "diaria"))
                .activo(true)
                .build());
        return h.getId();
    }

    @Override
    @Transactional
    public void logHabito(String userSubject, Long habitoId, HabitoLogRequest request) {
        Usuario u = requireUsuario(userSubject);
        UsuarioHabito h = usuarioHabitoRepository.findById(habitoId).orElseThrow();
        if (!h.getUsuario().getId().equals(u.getId())) throw new IllegalArgumentException("Hábito no pertenece al usuario");
        usuarioHabitoLogRepository.save(UsuarioHabitoLog.builder()
                .usuarioHabito(h)
                .fecha(request.fecha() != null ? request.fecha() : LocalDate.now())
                .cumplido(true)
                .build());
    }

    @Override
    @Transactional(readOnly = true)
    public List<String> recomendaciones(String userSubject) {
        Usuario u = requireUsuario(userSubject);
        return recomendacionRepository.findByUsuarioIdAndActivoTrue(u.getId()).stream()
                .map(Recomendacion::getTexto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void actualizarAvatar(String userSubject, String base64OrUrl) {
        Usuario u = requireUsuario(userSubject);
        if (base64OrUrl != null && !base64OrUrl.isBlank()) {
            // Guardar hasta 1MB de texto base64/URL
            String val = base64OrUrl.length() > 1_000_000 ? base64OrUrl.substring(0, 1_000_000) : base64OrUrl;
            u.setAvatar(val);
            usuarioRepository.save(u);
        }
    }
}
