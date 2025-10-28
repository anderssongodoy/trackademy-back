package com.trackademy.web;

import com.trackademy.dto.*;
import com.trackademy.service.MeService;
import com.trackademy.service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/me")
@RequiredArgsConstructor
public class MeFeaturesController {

    private final MeService meService;
    private final AccountService accountService;

    @GetMapping("/cursos")
    public List<UsuarioCursoResumenDto> cursos(@AuthenticationPrincipal Jwt jwt) {
        return meService.cursos(jwt.getSubject());
    }

    @GetMapping("/evaluaciones")
    public List<UsuarioEvaluacionDto> proximasEvaluaciones(@AuthenticationPrincipal Jwt jwt) {
        return meService.proximasEvaluaciones(jwt.getSubject());
    }

    @PostMapping("/evaluaciones/{id}/nota")
    public void registrarNota(@AuthenticationPrincipal Jwt jwt, @PathVariable("id") Long usuarioEvaluacionId,
                              @Valid @RequestBody NotaRequest body) {
        meService.registrarNota(jwt.getSubject(), usuarioEvaluacionId, body.nota());
    }

    @PostMapping("/preferencias/recordatorios")
    public void upsertRecordatorios(@AuthenticationPrincipal Jwt jwt, @Valid @RequestBody RecordatorioPreferenciaRequest body) {
        meService.upsertPreferenciasRecordatorios(jwt.getSubject(), body.anticipacionDias());
    }

    @PostMapping("/horario")
    public void upsertHorario(@AuthenticationPrincipal Jwt jwt, @Valid @RequestBody List<HorarioEntryDto> entries) {
        meService.upsertHorario(jwt.getSubject(), entries);
    }

    @PostMapping("/habitos")
    public Long crearHabito(@AuthenticationPrincipal Jwt jwt, @Valid @RequestBody HabitoCreateRequest req) {
        return meService.crearHabito(jwt.getSubject(), req);
    }

    @PostMapping("/habitos/{id}/log")
    public void logHabito(@AuthenticationPrincipal Jwt jwt, @PathVariable("id") Long habitoId, @RequestBody HabitoLogRequest req) {
        meService.logHabito(jwt.getSubject(), habitoId, req);
    }

    @GetMapping("/recomendaciones")
    public List<String> recomendaciones(@AuthenticationPrincipal Jwt jwt) {
        return meService.recomendaciones(jwt.getSubject());
    }

    @GetMapping("/status")
    public com.trackademy.dto.LoginStatusDto status(@AuthenticationPrincipal Jwt jwt) {
        String email = jwt.getClaimAsString("preferred_username");
        if (email == null) email = jwt.getClaimAsString("email");
        return accountService.getLoginStatus(jwt.getSubject(), email);
    }
}
