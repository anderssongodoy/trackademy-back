package com.trackademy.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    private static final Logger log = LoggerFactory.getLogger(AdminController.class);

    @PostMapping("/ingesta/json")
    public ResponseEntity<Map<String, Object>> ingesta(@RequestBody Map<String, Object> payload) {
        log.info("Ingesta JSON recibida con keys: {}", payload.keySet());
        // TODO: persistir según extractor; idempotente via claves únicas
        return ResponseEntity.accepted().body(Map.of("status", "accepted"));
    }
}

