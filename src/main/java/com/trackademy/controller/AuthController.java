package com.trackademy.controller;

import com.trackademy.dto.IdTokenRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth/entra")
public class AuthController {

    private final JwtDecoder jwtDecoder;

    public AuthController(JwtDecoder jwtDecoder) {
        this.jwtDecoder = jwtDecoder;
    }

    @PostMapping("/callback")
    public ResponseEntity<?> callback(@RequestBody IdTokenRequest body) {
        if (body == null || body.getIdToken() == null || body.getIdToken().isBlank()) {
            return ResponseEntity.badRequest().build();
        }
        Jwt jwt = jwtDecoder.decode(body.getIdToken());
        Map<String, Object> resp = new HashMap<>();
        resp.put("sub", jwt.getClaim("sub"));
        resp.put("name", jwt.getClaim("name"));
        resp.put("preferred_username", jwt.getClaim("preferred_username"));
        resp.put("email", jwt.getClaim("email"));
        return ResponseEntity.ok(resp);
    }
}
