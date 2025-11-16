package com.trackademy.web;

import com.trackademy.dto.*;
import com.trackademy.service.CatalogService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = CatalogController.class)
class CatalogControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CatalogService catalogService;

    @Test
    @DisplayName("GET /api/catalog/campus requiere JWT y devuelve lista")
    void listCampus_ok() throws Exception {
        System.out.println("===== START P-01: GET /api/catalog/campus =====");
        Mockito.when(catalogService.listarCampus(1L))
                .thenReturn(List.of(new CampusDto(3L, "Lima Centro")));

        mockMvc.perform(get("/api/catalog/campus")
                        .with(jwt())
                        .param("universidadId", "1")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andDo(result -> {
                    System.out.println("INPUT: GET /api/catalog/campus?universidadId=1 (with JWT)");
                    System.out.println("OUTPUT: status=" + result.getResponse().getStatus() +
                            ", body=" + result.getResponse().getContentAsString());
                })
                .andDo(r -> System.out.println("===== END P-01 ====="))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(3))
                .andExpect(jsonPath("$[0].nombre").value("Lima Centro"));
    }

    @Test
    @DisplayName("GET /api/catalog/periodos devuelve periodos con fechas")
    void listPeriodos_ok() throws Exception {
        System.out.println("===== START P-02: GET /api/catalog/periodos =====");
        Mockito.when(catalogService.listarPeriodos(1L))
                .thenReturn(List.of(new PeriodoDto(10L, "2025-1", LocalDate.of(2025,3,4), LocalDate.of(2025,7,20))));

        mockMvc.perform(get("/api/catalog/periodos")
                        .with(jwt())
                        .param("universidadId", "1"))
                .andDo(print())
                .andDo(result -> {
                    System.out.println("INPUT: GET /api/catalog/periodos?universidadId=1 (with JWT)");
                    System.out.println("OUTPUT: status=" + result.getResponse().getStatus() +
                            ", body=" + result.getResponse().getContentAsString());
                })
                .andDo(r -> System.out.println("===== END P-02 ====="))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(10))
                .andExpect(jsonPath("$[0].etiqueta").value("2025-1"))
                .andExpect(jsonPath("$[0].fechaInicio").value("2025-03-04"))
                .andExpect(jsonPath("$[0].fechaFin").value("2025-07-20"));
    }

    @Test
    @DisplayName("GET /api/catalog/carreras devuelve lista por universidad")
    void listCarreras_ok() throws Exception {
        System.out.println("===== START P-03: GET /api/catalog/carreras =====");
        Mockito.when(catalogService.listarCarreras(5L))
                .thenReturn(List.of(new CarreraDto(1L, "Ingeniería de Sistemas")));

        mockMvc.perform(get("/api/catalog/carreras")
                        .with(jwt())
                        .param("universidadId", "5"))
                .andDo(print())
                .andDo(result -> {
                    System.out.println("INPUT: GET /api/catalog/carreras?universidadId=5 (with JWT)");
                    System.out.println("OUTPUT: status=" + result.getResponse().getStatus() +
                            ", body=" + result.getResponse().getContentAsString());
                })
                .andDo(r -> System.out.println("===== END P-03 ====="))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].nombre").value("Ingeniería de Sistemas"));
    }

    @Test
    @DisplayName("GET /api/catalog/cursos devuelve lista por carrera")
    void listCursos_ok() throws Exception {
        System.out.println("===== START P-04: GET /api/catalog/cursos =====");
        Mockito.when(catalogService.listarCursosPorCarrera(7L))
                .thenReturn(List.of(new CursoDto(100L, "MAT101", "Matemática I")));

        mockMvc.perform(get("/api/catalog/cursos")
                        .with(jwt())
                        .param("carreraId", "7"))
                .andDo(print())
                .andDo(result -> {
                    System.out.println("INPUT: GET /api/catalog/cursos?carreraId=7 (with JWT)");
                    System.out.println("OUTPUT: status=" + result.getResponse().getStatus() +
                            ", body=" + result.getResponse().getContentAsString());
                })
                .andDo(r -> System.out.println("===== END P-04 ====="))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(100))
                .andExpect(jsonPath("$[0].codigo").value("MAT101"))
                .andExpect(jsonPath("$[0].nombre").value("Matemática I"));
    }

    @Test
    @DisplayName("GET /api/catalog/curso/{id} devuelve detalle con secciones vacías")
    void getCurso_detail_ok() throws Exception {
        System.out.println("===== START P-05: GET /api/catalog/curso/{id} =====");
        CursoDetailDto detail = new CursoDetailDto(
                100L, "MAT101", "Matemática I", 4,
                "Sumilla del curso",
                List.of(),
                List.of(),
                List.of(),
                List.of("Stewart"),
                List.of("Pensamiento crítico"),
                List.of(new NotaPoliticaDto("Cálculo de nota", "Se promedian"))
        );
        Mockito.when(catalogService.obtenerCursoDetalle(100L)).thenReturn(detail);

        mockMvc.perform(get("/api/catalog/curso/{id}", 100L).with(jwt()))
                .andDo(print())
                .andDo(result -> {
                    System.out.println("INPUT: GET /api/catalog/curso/100 (with JWT)");
                    System.out.println("OUTPUT: status=" + result.getResponse().getStatus() +
                            ", body=" + result.getResponse().getContentAsString());
                })
                .andDo(r -> System.out.println("===== END P-05 ====="))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(100))
                .andExpect(jsonPath("$.codigo").value("MAT101"))
                .andExpect(jsonPath("$.nombre").value("Matemática I"))
                .andExpect(jsonPath("$.bibliografia[0]").value("Stewart"))
                .andExpect(jsonPath("$.competencias[0]").value("Pensamiento crítico"))
                .andExpect(jsonPath("$.politicas[0].seccion").value("Cálculo de nota"));
    }

    @Test
    @DisplayName("GET /api/catalog/carreras requiere JWT (401 si falta)")
    void listCarreras_unauthorized_sinJwt() throws Exception {
        System.out.println("===== START P-06: GET /api/catalog/carreras (sin JWT) =====");
        Mockito.when(catalogService.listarCarreras(anyLong())).thenReturn(List.of());
        mockMvc.perform(get("/api/catalog/carreras").param("universidadId", "1"))
                .andDo(print())
                .andDo(result -> {
                    System.out.println("INPUT: GET /api/catalog/carreras?universidadId=1 (sin JWT)");
                    System.out.println("OUTPUT: status=" + result.getResponse().getStatus());
                })
                .andDo(r -> System.out.println("===== END P-06 ====="))
                .andExpect(status().isUnauthorized());
    }
}
