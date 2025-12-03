package com.trackademy.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.trackademy.config.SecurityConfig;
import com.trackademy.dto.*;
import com.trackademy.service.AccountService;
import com.trackademy.service.MeService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = MeFeaturesController.class)
@Import(SecurityConfig.class)
class MeFeaturesControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MeService meService;

    @MockBean
    private AccountService accountService;

    @MockBean
    private JwtDecoder jwtDecoder;

    @Test
    void cursosReturnsResumen() throws Exception {
        System.out.println("===== START MEF-01: GET /api/me/cursos =====");
        Mockito.when(meService.cursos("user-1"))
                .thenReturn(List.of(new UsuarioCursoResumenDto(10L, 5L, "Algebra", List.of())));

        mockMvc.perform(get("/api/me/cursos").with(jwt().jwt(jwt -> jwt.subject("user-1"))))
                .andDo(print())
                .andDo(r -> System.out.println("===== END MEF-01 ====="))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].usuarioCursoId").value(10))
                .andExpect(jsonPath("$[0].cursoNombre").value("Algebra"));
    }

    @Test
    void reemplazarCursosActualizaLista() throws Exception {
        System.out.println("===== START MEF-02: PUT /api/me/cursos =====");
        OnboardingRequest request = new OnboardingRequest(1L, 2L, 3L, List.of(4L, 5L));
        List<UsuarioCursoResumenDto> resultado = List.of(
                new UsuarioCursoResumenDto(20L, 4L, "Algoritmos", List.of())
        );
        Mockito.when(meService.reemplazarCursos("user-1", request)).thenReturn(resultado);

        mockMvc.perform(put("/api/me/cursos")
                        .with(jwt().jwt(jwt -> jwt.subject("user-1"))).contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andDo(r -> System.out.println("===== END MEF-02 ====="))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].usuarioCursoId").value(20));

        verify(meService).reemplazarCursos("user-1", request);
    }

    @Test
    void proximasEvaluacionesLista() throws Exception {
        System.out.println("===== START MEF-03: GET /api/me/evaluaciones =====");
        Mockito.when(meService.proximasEvaluaciones("user-1"))
                .thenReturn(List.of(new UsuarioEvaluacionDto(30L, "EV01", "Parcial", 5, new BigDecimal("30.0"),
                        LocalDate.of(2025, 2, 15), "15")));

        mockMvc.perform(get("/api/me/evaluaciones").with(jwt().jwt(jwt -> jwt.subject("user-1"))))
                .andDo(print())
                .andDo(r -> System.out.println("===== END MEF-03 ====="))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(30))
                .andExpect(jsonPath("$[0].codigo").value("EV01"))
                .andExpect(jsonPath("$[0].porcentaje").value(30.0))
                .andExpect(jsonPath("$[0].fechaEstimada").value("2025-02-15"));
    }

    @Test
    void registrarNotaLlamaServicio() throws Exception {
        System.out.println("===== START MEF-04: POST /api/me/evaluaciones/{id}/nota =====");
        NotaRequest body = new NotaRequest("18");
        mockMvc.perform(post("/api/me/evaluaciones/{id}/nota", 30L)
                        .with(jwt().jwt(jwt -> jwt.subject("user-1")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andDo(print())
                .andDo(r -> System.out.println("===== END MEF-04 ====="))
                .andExpect(status().isOk());

        verify(meService).registrarNota("user-1", 30L, "18");
    }
    @Test
    void upsertHorarioGuardaEntradas() throws Exception {
        System.out.println("===== START MEF-06: POST /api/me/horario =====");
        List<HorarioEntryDto> entries = List.of(new HorarioEntryDto(10L, 2, "08:00", 90));

        mockMvc.perform(post("/api/me/horario")
                        .with(jwt().jwt(jwt -> jwt.subject("user-1")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(entries)))
                .andDo(print())
                .andDo(r -> System.out.println("===== END MEF-06 ====="))
                .andExpect(status().isNoContent());

        verify(meService).upsertHorario("user-1", entries);
    }

    @Test
    void listarHorarioDevuelveEntradas() throws Exception {
        System.out.println("===== START MEF-07: GET /api/me/horario =====");
        List<HorarioEntryDto> entries = List.of(new HorarioEntryDto(11L, 3, "09:30", 120));
        Mockito.when(meService.listarHorario("user-1", null)).thenReturn(entries);

        mockMvc.perform(get("/api/me/horario").with(jwt().jwt(jwt -> jwt.subject("user-1"))))
                .andDo(print())
                .andDo(r -> System.out.println("===== END MEF-07 ====="))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].usuarioCursoId").value(11))
                .andExpect(jsonPath("$[0].horaInicio").value("09:30"))
                .andExpect(jsonPath("$[0].duracionMin").value(120));
    }

    @Test
    void recomendacionesDevueltas() throws Exception {
        System.out.println("===== START MEF-10: GET /api/me/recomendaciones =====");
        Mockito.when(meService.recomendaciones("user-1"))
                .thenReturn(List.of("Descansa mas", "Organiza tu semana"));

        mockMvc.perform(get("/api/me/recomendaciones").with(jwt().jwt(jwt -> jwt.subject("user-1"))))
                .andDo(print())
                .andDo(r -> System.out.println("===== END MEF-10 ====="))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").value("Descansa mas"))
                .andExpect(jsonPath("$[1]").value("Organiza tu semana"));
    }
}
