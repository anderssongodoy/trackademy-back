package com.trackademy.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.trackademy.config.SecurityConfig;
import com.trackademy.dto.OnboardingRequest;
import com.trackademy.dto.UsuarioCursoResumenDto;
import com.trackademy.dto.UsuarioEvaluacionDto;
import com.trackademy.service.OnboardingService;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = OnboardingController.class)
@Import(SecurityConfig.class)
class OnboardingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private OnboardingService onboardingService;

    @MockBean
    private JwtDecoder jwtDecoder;

    @Test
    void onboardingUsesJwtAndHeaders() throws Exception {
        System.out.println("===== START ONBOARD-01: POST /api/onboarding =====");
        OnboardingRequest req = new OnboardingRequest(1L, 2L, 3L, List.of(4L, 5L));
        List<UsuarioCursoResumenDto> response = List.of(
                new UsuarioCursoResumenDto(10L, 4L, "Algoritmos", List.of(
                        new UsuarioEvaluacionDto(20L, "EV1", "Parcial 1", 3, new BigDecimal("20.0"), LocalDate.of(2025, 3, 5), null)
                ))
        );
        Mockito.when(onboardingService.onboard(anyString(), anyString(), anyString(), anyString(), any())).thenReturn(response);

        mockMvc.perform(post("/api/onboarding")
                        .with(jwt().jwt(jwt -> jwt
                                .subject("user-1")
                                .claim("preferred_username", "john@example.com")
                                .claim("name", "John Doe")
                                .claim("picture", "https://cdn/pic.png")))
                        .header("X-User-Avatar", "https://cdn/avatar.png")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andDo(print())
                .andDo(r -> System.out.println("===== END ONBOARD-01 ====="))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].usuarioCursoId").value(10))
                .andExpect(jsonPath("$[0].evaluaciones[0].codigo").value("EV1"));

        verify(onboardingService).onboard("user-1", "john@example.com", "John Doe", "https://cdn/avatar.png", req);
    }
}
