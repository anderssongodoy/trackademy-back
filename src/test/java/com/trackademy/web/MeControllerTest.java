package com.trackademy.web;

import com.trackademy.config.SecurityConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = MeController.class)
@Import(SecurityConfig.class)
class MeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JwtDecoder jwtDecoder;

    @Test
    void meReturnsJwtData() throws Exception {
        System.out.println("===== START ME-01: GET /api/me =====");
        mockMvc.perform(get("/api/me")
                        .with(jwt().jwt(jwt -> jwt
                                .subject("sub-123")
                                .issuer("https://issuer.test")
                                .audience(List.of("api://aud"))
                                .claim("preferred_username", "me@test.com")
                                .claim("name", "Tester")
                                .claim("picture", "https://cdn/me.png")
                                .claim("custom", "v1"))))
                .andDo(print())
                .andDo(r -> System.out.println("===== END ME-01 ====="))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sub").value("sub-123"))
                .andExpect(jsonPath("$.issuer").value("https://issuer.test"))
                .andExpect(jsonPath("$.aud[0]").value("api://aud"))
                .andExpect(jsonPath("$.claims.preferred_username").value("me@test.com"))
                .andExpect(jsonPath("$.claims.custom").value("v1"));
    }
}
