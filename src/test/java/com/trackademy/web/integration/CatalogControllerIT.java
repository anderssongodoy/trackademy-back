package com.trackademy.web.integration;

import com.trackademy.config.SecurityConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Import({SecurityConfig.class, IntegrationTestConfig.class})
class CatalogControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("GET /api/catalog/campus responde 200 con lista")
    void listCampus_ok() throws Exception {
        System.out.println("===== START IT-CAT-01: GET /api/catalog/campus =====");
        mockMvc.perform(get("/api/catalog/campus")
                        .with(jwt())
                        .param("universidadId", "1")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andDo(r -> System.out.println("===== END IT-CAT-01 ====="))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(3))
                .andExpect(jsonPath("$[0].nombre").value("Lima Centro"));
    }

    @Test
    @DisplayName("GET /api/catalog/periodos responde 200 con fechas")
    void listPeriodos_ok() throws Exception {
        System.out.println("===== START IT-CAT-02: GET /api/catalog/periodos =====");
        mockMvc.perform(get("/api/catalog/periodos")
                        .with(jwt())
                        .param("universidadId", "1"))
                .andDo(print())
                .andDo(r -> System.out.println("===== END IT-CAT-02 ====="))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(10))
                .andExpect(jsonPath("$[0].etiqueta").value("2025-1"))
                .andExpect(jsonPath("$[0].fechaInicio").value("2025-03-04"))
                .andExpect(jsonPath("$[0].fechaFin").value("2025-07-20"));
    }

    @Test
    @DisplayName("GET /api/catalog/carreras responde 200 con carreras")
    void listCarreras_ok() throws Exception {
        System.out.println("===== START IT-CAT-03: GET /api/catalog/carreras =====");
        mockMvc.perform(get("/api/catalog/carreras")
                        .with(jwt())
                        .param("universidadId", "5"))
                .andDo(print())
                .andDo(r -> System.out.println("===== END IT-CAT-03 ====="))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].nombre").value("Ingenieria de Sistemas"));
    }

    @Test
    @DisplayName("GET /api/catalog/cursos responde 200 con cursos")
    void listCursos_ok() throws Exception {
        System.out.println("===== START IT-CAT-04: GET /api/catalog/cursos =====");
        mockMvc.perform(get("/api/catalog/cursos")
                        .with(jwt())
                        .param("carreraId", "7"))
                .andDo(print())
                .andDo(r -> System.out.println("===== END IT-CAT-04 ====="))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(100))
                .andExpect(jsonPath("$[0].codigo").value("MAT101"))
                .andExpect(jsonPath("$[0].nombre").value("Matematica I"));
    }

    @Test
    @DisplayName("GET /api/catalog/curso/{id} responde 200 con detalle")
    void getCurso_detail_ok() throws Exception {
        System.out.println("===== START IT-CAT-05: GET /api/catalog/curso/{id} =====");
        mockMvc.perform(get("/api/catalog/curso/{id}", 100L).with(jwt()))
                .andDo(print())
                .andDo(r -> System.out.println("===== END IT-CAT-05 ====="))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(100))
                .andExpect(jsonPath("$.codigo").value("MAT101"))
                .andExpect(jsonPath("$.nombre").value("Matematica I"))
                .andExpect(jsonPath("$.bibliografia[0]").value("Stewart"))
                .andExpect(jsonPath("$.competencias[0]").value("Pensamiento critico"))
                .andExpect(jsonPath("$.politicas[0].seccion").value("Calculo de nota"));
    }
}
