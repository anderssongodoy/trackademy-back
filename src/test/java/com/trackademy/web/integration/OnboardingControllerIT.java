package com.trackademy.web.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.trackademy.config.SecurityConfig;
import com.trackademy.dto.OnboardingRequest;
import com.trackademy.dto.UsuarioCursoResumenDto;
import com.trackademy.dto.UsuarioEvaluacionDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Import({SecurityConfig.class, IntegrationTestConfig.class})
class OnboardingControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void onboardingUsesJwtAndHeaders() throws Exception {
        System.out.println("===== START IT-ONBOARD-01: POST /api/onboarding =====");
        OnboardingRequest req = new OnboardingRequest(1L, 2L, 3L, List.of(4L, 5L));
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
                .andDo(r -> System.out.println("===== END IT-ONBOARD-01 ====="))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].usuarioCursoId").value(10))
                .andExpect(jsonPath("$[0].evaluaciones[0].codigo").value("EV1"));
    }
}
