package com.trackademy.service;

import com.trackademy.dto.*;

import java.util.List;

public interface MeService {
    List<UsuarioCursoResumenDto> cursos(String userSubject);
    List<UsuarioEvaluacionDto> proximasEvaluaciones(String userSubject);
    void registrarNota(String userSubject, Long usuarioEvaluacionId, String notaStr);
    void upsertPreferenciasRecordatorios(String userSubject, int anticipacionDias);
    void upsertHorario(String userSubject, List<HorarioEntryDto> entries);
    Long crearHabito(String userSubject, HabitoCreateRequest request);
    void logHabito(String userSubject, Long habitoId, HabitoLogRequest request);
    List<String> recomendaciones(String userSubject);
    void actualizarAvatar(String userSubject, String base64OrUrl);
}
