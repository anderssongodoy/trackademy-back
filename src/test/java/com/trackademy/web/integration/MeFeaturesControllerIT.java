package com.trackademy.web.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.trackademy.config.SecurityConfig;
import com.trackademy.dto.AvatarRequest;
import com.trackademy.dto.HabitoCreateRequest;
import com.trackademy.dto.HabitoLogRequest;
import com.trackademy.dto.HorarioEntryDto;
import com.trackademy.dto.NotaRequest;
import com.trackademy.dto.OnboardingRequest;
import com.trackademy.dto.PreferenciaDiaItemDto;
import com.trackademy.dto.PreferenciasDiaRequest;
import com.trackademy.dto.RecordatorioPreferenciaRequest;
import com.trackademy.dto.UsuarioCursoResumenDto;
import com.trackademy.dto.UsuarioEvaluacionDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Import({SecurityConfig.class, IntegrationTestConfig.class})
class MeFeaturesControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void cursosReturnsResumen() throws Exception {
        System.out.println("===== START IT-MEF-01: GET /api/me/cursos =====");
        mockMvc.perform(get("/api/me/cursos").with(jwt().jwt(jwt -> jwt.subject("user-1"))))
                .andDo(print())
                .andDo(r -> System.out.println("===== END IT-MEF-01 ====="))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].usuarioCursoId").value(10))
                .andExpect(jsonPath("$[0].cursoNombre").value("Algebra"));
    }

    @Test
    void reemplazarCursosActualizaLista() throws Exception {
        System.out.println("===== START IT-MEF-02: PUT /api/me/cursos =====");
        OnboardingRequest request = new OnboardingRequest(1L, 2L, 3L, List.of(4L, 5L));
        mockMvc.perform(put("/api/me/cursos")
                        .with(jwt().jwt(jwt -> jwt.subject("user-1"))).contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andDo(r -> System.out.println("===== END IT-MEF-02 ====="))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].usuarioCursoId").value(20));
    }

    @Test
    void proximasEvaluacionesLista() throws Exception {
        System.out.println("===== START IT-MEF-03: GET /api/me/evaluaciones =====");
        mockMvc.perform(get("/api/me/evaluaciones").with(jwt().jwt(jwt -> jwt.subject("user-1"))))
                .andDo(print())
                .andDo(r -> System.out.println("===== END IT-MEF-03 ====="))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(30))
                .andExpect(jsonPath("$[0].codigo").value("EV01"))
                .andExpect(jsonPath("$[0].porcentaje").value(30.0))
                .andExpect(jsonPath("$[0].fechaEstimada").value("2025-02-15"));
    }

    @Test
    void registrarNotaLlamaServicio() throws Exception {
        System.out.println("===== START IT-MEF-04: POST /api/me/evaluaciones/{id}/nota =====");
        NotaRequest body = new NotaRequest("18");
        mockMvc.perform(post("/api/me/evaluaciones/{id}/nota", 30L)
                        .with(jwt().jwt(jwt -> jwt.subject("user-1")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andDo(print())
                .andDo(r -> System.out.println("===== END IT-MEF-04 ====="))
                .andExpect(status().isOk());
    }

    @Test
    void upsertRecordatoriosActualizaPreferencia() throws Exception {
        System.out.println("===== START IT-MEF-05: POST /api/me/preferencias/recordatorios =====");
        RecordatorioPreferenciaRequest body = new RecordatorioPreferenciaRequest(3);
        mockMvc.perform(post("/api/me/preferencias/recordatorios")
                        .with(jwt().jwt(jwt -> jwt.subject("user-1")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andDo(print())
                .andDo(r -> System.out.println("===== END IT-MEF-05 ====="))
                .andExpect(status().isOk());
    }

    @Test
    void upsertHorarioGuardaEntradas() throws Exception {
        System.out.println("===== START IT-MEF-06: POST /api/me/horario =====");
        List<HorarioEntryDto> entries = List.of(new HorarioEntryDto(10L, 2, "08:00", 90));

        mockMvc.perform(post("/api/me/horario")
                        .with(jwt().jwt(jwt -> jwt.subject("user-1")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(entries)))
                .andDo(print())
                .andDo(r -> System.out.println("===== END IT-MEF-06 ====="))
                .andExpect(status().isNoContent());
    }

    @Test
    void listarHorarioDevuelveEntradas() throws Exception {
        System.out.println("===== START IT-MEF-07: GET /api/me/horario =====");
        mockMvc.perform(get("/api/me/horario").with(jwt().jwt(jwt -> jwt.subject("user-1"))))
                .andDo(print())
                .andDo(r -> System.out.println("===== END IT-MEF-07 ====="))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].usuarioCursoId").value(11))
                .andExpect(jsonPath("$[0].horaInicio").value("09:30"))
                .andExpect(jsonPath("$[0].duracionMin").value(120));
    }

    @Test
    void crearHabitoRetornaId() throws Exception {
        System.out.println("===== START IT-MEF-08: POST /api/me/habitos =====");
        HabitoCreateRequest req = new HabitoCreateRequest("Leer 20 min", "diario");
        mockMvc.perform(post("/api/me/habitos")
                        .with(jwt().jwt(jwt -> jwt.subject("user-1")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andDo(print())
                .andDo(r -> System.out.println("===== END IT-MEF-08 ====="))
                .andExpect(status().isOk())
                .andExpect(content().string("77"));
    }

    @Test
    void logHabitoGuardaEvento() throws Exception {
        System.out.println("===== START IT-MEF-09: POST /api/me/habitos/{id}/log =====");
        HabitoLogRequest req = new HabitoLogRequest(LocalDate.of(2025, 1, 10));

        mockMvc.perform(post("/api/me/habitos/{id}/log", 9L)
                        .with(jwt().jwt(jwt -> jwt.subject("user-1")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andDo(print())
                .andDo(r -> System.out.println("===== END IT-MEF-09 ====="))
                .andExpect(status().isOk());
    }

    @Test
    void recomendacionesDevueltas() throws Exception {
        System.out.println("===== START IT-MEF-10: GET /api/me/recomendaciones =====");
        mockMvc.perform(get("/api/me/recomendaciones").with(jwt().jwt(jwt -> jwt.subject("user-1"))))
                .andDo(print())
                .andDo(r -> System.out.println("===== END IT-MEF-10 ====="))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").value("Descansa mas"))
                .andExpect(jsonPath("$[1]").value("Organiza tu semana"));
    }

    @Test
    void statusTomaAvatarDeHeaders() throws Exception {
        System.out.println("===== START IT-MEF-11: GET /api/me/status =====");
        mockMvc.perform(get("/api/me/status")
                        .with(jwt().jwt(jwt -> jwt
                                .subject("user-1")
                                .claim("preferred_username", "john@example.com")
                                .claim("name", "John Doe")
                                .claim("picture", "https://cdn/default.png")))
                        .header("X-User-Image", "https://cdn/header.png"))
                .andDo(print())
                .andDo(r -> System.out.println("===== END IT-MEF-11 ====="))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.usuarioId").value(50))
                .andExpect(jsonPath("$.subject").value("user-1"));
    }

    @Test
    void actualizarAvatarLlamaServicio() throws Exception {
        System.out.println("===== START IT-MEF-12: POST /api/me/avatar =====");
        AvatarRequest req = new AvatarRequest("data:image/png;base64,AAA");

        mockMvc.perform(post("/api/me/avatar")
                        .with(jwt().jwt(jwt -> jwt.subject("user-1")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andDo(print())
                .andDo(r -> System.out.println("===== END IT-MEF-12 ====="))
                .andExpect(status().isOk());
    }

    @Test
    void listarPreferenciasDevuelveLista() throws Exception {
        System.out.println("===== START IT-MEF-13: GET /api/me/hitos/preferencias =====");
        mockMvc.perform(get("/api/me/hitos/preferencias").with(jwt().jwt(jwt -> jwt.subject("user-1"))))
                .andDo(print())
                .andDo(r -> System.out.println("===== END IT-MEF-13 ====="))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].usuarioCursoId").value(5))
                .andExpect(jsonPath("$[0].diaSemana").value(2));
    }

    @Test
    void upsertPreferenciasGuardaDatos() throws Exception {
        System.out.println("===== START IT-MEF-14: POST /api/me/hitos/preferencias =====");
        PreferenciasDiaRequest req = new PreferenciasDiaRequest(List.of(new PreferenciaDiaItemDto(6L, 1)));

        mockMvc.perform(post("/api/me/hitos/preferencias")
                        .with(jwt().jwt(jwt -> jwt.subject("user-1")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andDo(print())
                .andDo(r -> System.out.println("===== END IT-MEF-14 ====="))
                .andExpect(status().isOk());
    }
}
