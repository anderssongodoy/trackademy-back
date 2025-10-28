package com.trackademy.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.JwtTimestampValidator;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Value("${spring.security.oauth2.resourceserver.jwt.jwk-set-uri}")
    private String jwkSetUri;

    @Value("${trackademy.security.microsoft.audience:}")
    private String expectedAudience;

    @Value("${trackademy.security.accepted-issuers:https://login.microsoftonline.com/}")
    private String acceptedIssuers;

    @Value("${trackademy.cors.allowed-origins:http://localhost:3000,https://trackademy.trinitylabs.app}")
    private String allowedOrigins;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .cors(Customizer.withDefaults())
            .csrf(csrf -> csrf.disable())
            .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/health", "/actuator/health").permitAll()
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .anyRequest().authenticated()
            )
            .oauth2ResourceServer(oauth2 -> oauth2
                .jwt(jwt -> jwt.decoder(jwtDecoder()))
            );

        return http.build();
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        NimbusJwtDecoder decoder = NimbusJwtDecoder.withJwkSetUri(jwkSetUri).build();

        OAuth2TokenValidator<Jwt> withTimestamp = new JwtTimestampValidatorWithClockSkew();

        OAuth2TokenValidator<Jwt> issuerValidator = token -> {
            String iss = token.getIssuer() != null ? token.getIssuer().toString() : null;
            if (iss == null) {
                return OAuth2TokenValidatorResult.failure(new OAuth2Error("invalid_token", "Missing issuer (iss)", null));
            }
            List<String> prefixes = Arrays.stream(acceptedIssuers.split(","))
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .collect(Collectors.toList());
            boolean ok = prefixes.stream().anyMatch(iss::startsWith);
            return ok ? OAuth2TokenValidatorResult.success() :
                    OAuth2TokenValidatorResult.failure(new OAuth2Error("invalid_token", "Unaccepted issuer: " + iss, null));
        };

        OAuth2TokenValidator<Jwt> audienceValidator = token -> {
            if (expectedAudience == null || expectedAudience.isBlank()) {
                return OAuth2TokenValidatorResult.success();
            }
            List<String> aud = token.getAudience();
            boolean ok = aud != null && aud.contains(expectedAudience);
            return ok ? OAuth2TokenValidatorResult.success() :
                    OAuth2TokenValidatorResult.failure(new OAuth2Error("invalid_token", "Invalid audience", null));
        };

        OAuth2TokenValidator<Jwt> combinedValidator = new org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator<>(withTimestamp, issuerValidator, audienceValidator);
        decoder.setJwtValidator(combinedValidator);

        return decoder;
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration cfg = new CorsConfiguration();
        cfg.setAllowedOrigins(Arrays.stream(allowedOrigins.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList()));
        cfg.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        cfg.setAllowedHeaders(List.of("Authorization", "Content-Type", "X-Requested-With"));
        cfg.setExposedHeaders(List.of("Location"));
        cfg.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", cfg);
        return source;
    }

    static class JwtTimestampValidatorWithClockSkew implements OAuth2TokenValidator<Jwt> {
        private final org.springframework.security.oauth2.jwt.JwtTimestampValidator delegate;
        JwtTimestampValidatorWithClockSkew() {
            this.delegate = new org.springframework.security.oauth2.jwt.JwtTimestampValidator(java.time.Duration.ofMinutes(2));
        }
        @Override
        public OAuth2TokenValidatorResult validate(Jwt token) {
            return delegate.validate(token);
        }
    }
}

