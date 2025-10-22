package com.trackademy.controller;

import com.trackademy.dto.IdTokenRequest;
import com.trackademy.entity.User;
import com.trackademy.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth/entra")
public class AuthController {

    private final JwtDecoder jwtDecoder;
    private final UserRepository userRepository;

    public AuthController(JwtDecoder jwtDecoder, UserRepository userRepository) {
        this.jwtDecoder = jwtDecoder;
        this.userRepository = userRepository;
    }

    @PostMapping("/callback")
    public ResponseEntity<?> callback(@RequestBody IdTokenRequest body) {
        if (body == null || body.getIdToken() == null || body.getIdToken().isBlank()) {
            return ResponseEntity.badRequest().build();
        }
        Jwt jwt = jwtDecoder.decode(body.getIdToken());

        String email = jwt.getClaim("email");
        if (email == null) {
            email = jwt.getClaim("preferred_username");
        }

        String name = jwt.getClaim("name");
        String subject = jwt.getSubject();
        String externalId = jwt.getClaim("oid");
        if (externalId == null) externalId = subject;

        User user = null;
        if (email != null) {
            user = userRepository.findByEmail(email).orElse(null);
        }
        if (user == null) {
            user = userRepository.findByProviderAndProviderSubject("entra", subject).orElse(null);
        }
        if (user == null) {
            user = User.builder()
                    .email(email != null ? email : (externalId + "@entra"))
                    .name(name != null ? name : "User")
                    .provider("entra")
                    .externalId(externalId)
                    .providerSubject(subject)
                    .role("USER")
                    .active(true)
                    .createdAt(LocalDateTime.now())
                    .build();
        } else {
            user.setEmail(email != null ? email : user.getEmail());
            if (name != null) user.setName(name);
            user.setProvider("entra");
            user.setExternalId(externalId);
            user.setProviderSubject(subject);
            user.setUpdatedAt(LocalDateTime.now());
        }
        User saved = userRepository.save(user);

        Map<String, Object> resp = new HashMap<>();
        resp.put("userId", saved.getId());
        resp.put("email", saved.getEmail());
        return ResponseEntity.ok(resp);
    }
}
