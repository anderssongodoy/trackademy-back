package com.trackademy.web.integration;

import com.trackademy.dto.*;
import com.trackademy.service.AccountService;
import com.trackademy.service.CatalogService;
import com.trackademy.service.MeService;
import com.trackademy.service.OnboardingService;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * Beans de prueba simples para diferenciar las pruebas de integración del slice web.
 * No usan base de datos: devuelven datos en memoria, suficientes para validar rutas, seguridad y JSON.
 */
@TestConfiguration
class IntegrationTestConfig {

    @Bean
    @Primary
    CatalogService catalogServiceStub() {
        return new CatalogService() {
            @Override
            public List<com.trackademy.dto.CampusDto> listarCampus(Long universidadId) {
                return List.of(new CampusDto(3L, "Lima Centro"));
            }
            @Override
            public List<com.trackademy.dto.PeriodoDto> listarPeriodos(Long universidadId) {
                return List.of(new PeriodoDto(10L, "2025-1", LocalDate.of(2025, 3, 4), LocalDate.of(2025, 7, 20)));
            }
            @Override
            public List<com.trackademy.dto.CarreraDto> listarCarreras(Long universidadId) {
                return List.of(new CarreraDto(1L, "Ingenieria de Sistemas"));
            }
            @Override
            public List<com.trackademy.dto.CursoDto> listarCursosPorCarrera(Long carreraId) {
                return List.of(new CursoDto(100L, "MAT101", "Matematica I"));
            }
            @Override
            public CursoDetailDto obtenerCursoDetalle(Long cursoId) {
                return new CursoDetailDto(
                        100L, "MAT101", "Matematica I", 4,
                        "Sumilla del curso",
                        List.of(),
                        List.of(),
                        List.of(),
                        List.of("Stewart"),
                        List.of("Pensamiento critico"),
                        List.of(new NotaPoliticaDto("Calculo de nota", "Se promedian"))
                );
            }
        };
    }

    @Bean
    @Primary
    MeService meServiceStub() {
        return new MeService() {
            @Override
            public List<UsuarioCursoResumenDto> cursos(String userSubject) {
                return List.of(new UsuarioCursoResumenDto(10L, 5L, "Algebra", List.of()));
            }
            @Override
            public List<UsuarioEvaluacionDto> proximasEvaluaciones(String userSubject) {
                return List.of(new UsuarioEvaluacionDto(30L, "EV01", "Parcial", 5, new BigDecimal("30.0"),
                        LocalDate.of(2025, 2, 15), "15"));
            }
            @Override
            public void registrarNota(String userSubject, Long usuarioEvaluacionId, String notaStr) {}
            @Override
            public void upsertPreferenciasRecordatorios(String userSubject, int anticipacionDias) {}
            @Override
            public void upsertHorario(String userSubject, List<HorarioEntryDto> entries) {}
            @Override
            public List<HorarioEntryDto> listarHorario(String userSubject, Long usuarioCursoId) {
                return List.of(new HorarioEntryDto(11L, 3, "09:30", 120));
            }
            @Override
            public List<PreferenciaDiaItemDto> listarPreferenciasDia(String userSubject) {
                return List.of(new PreferenciaDiaItemDto(5L, 2));
            }
            @Override
            public void upsertPreferenciasDia(String userSubject, PreferenciasDiaRequest request) {}
            @Override
            public Long crearHabito(String userSubject, HabitoCreateRequest request) {
                return 77L;
            }
            @Override
            public void logHabito(String userSubject, Long habitoId, HabitoLogRequest request) {}
            @Override
            public List<String> recomendaciones(String userSubject) {
                return List.of("Descansa mas", "Organiza tu semana");
            }
            @Override
            public void actualizarAvatar(String userSubject, String base64OrUrl) {}
            @Override
            public List<UsuarioCursoResumenDto> reemplazarCursos(String userSubject, OnboardingRequest request) {
                return List.of(new UsuarioCursoResumenDto(20L, 4L, "Algoritmos", List.of()));
            }
        };
    }

    @Bean
    @Primary
    AccountService accountServiceStub() {
        return (subject, email, nombre, avatar) ->
                new LoginStatusDto(false, List.of(), 50L, subject, email, 1L, 2L, 3L, 4);
    }

    @Bean
    @Primary
    OnboardingService onboardingServiceStub() {
        return (userSubject, email, nombre, avatar, request) -> List.of(
                new UsuarioCursoResumenDto(10L, 4L, "Algoritmos", List.of(
                        new UsuarioEvaluacionDto(20L, "EV1", "Parcial 1", 3, new BigDecimal("20.0"), LocalDate.of(2025, 3, 5), null)
                ))
        );
    }

    @Bean
    @Primary
    JwtDecoder jwtDecoderStub() {
        return token -> {
            // Devuelve un JWT simulado para que el filtro de seguridad lo acepte en pruebas de integración.
            return new Jwt(token, Instant.now(), Instant.now().plusSeconds(3600),
                    Map.of("alg", "none"),
                    Map.of("sub", "it-user", "preferred_username", "it@example.com"));
        };
    }
}
