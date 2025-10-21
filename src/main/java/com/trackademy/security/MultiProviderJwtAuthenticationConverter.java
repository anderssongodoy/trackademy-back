package com.trackademy.security;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.lang.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.trackademy.entity.User;
import com.trackademy.service.UserService;
import java.util.List;

public class MultiProviderJwtAuthenticationConverter implements Converter<Jwt, AbstractAuthenticationToken> {
    
    private static final Logger logger = LoggerFactory.getLogger(MultiProviderJwtAuthenticationConverter.class);
    private final UserService userService;
    
    public MultiProviderJwtAuthenticationConverter(UserService userService) {
        this.userService = userService;
    }
    
    @Override
    public AbstractAuthenticationToken convert(@NonNull Jwt jwt) {
        String issuer = jwt.getIssuer() != null ? jwt.getIssuer().toString() : "";
        String provider;
        
        if (issuer.startsWith("https://accounts.google.com")) {
            provider = "google";
        } else if (issuer.contains("login.microsoftonline.com")) {
            provider = "microsoft";
        } else {
            provider = "unknown";
        }
        
        logger.debug("Processing JWT from provider: {}", provider);
        
        String sub = jwt.getSubject();
        String email = extractEmail(jwt, provider);
        String name = extractName(jwt, provider);
        String picture = extractPicture(jwt, provider);
        
        if (email == null || email.isBlank()) {
            logger.warn("JWT without usable email (provider={}, sub={}, issuer={})", provider, sub, issuer);
            throw new IllegalArgumentException("Email claim missing for provider: " + provider);
        }
        
        logger.info("Authenticating user: email={}, provider={}", email, provider);
        User user = userService.upsertFromExternal(provider, sub, email, name, picture);
        
        return new JwtUserAuthentication(jwt, user, 
            List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole())));
    }
    
    private String extractEmail(Jwt jwt, String provider) {
        if ("google".equals(provider)) {
            return jwt.getClaim("email");
        } else if ("microsoft".equals(provider)) {
            return firstNonBlank(
                jwt.getClaim("preferred_username"),
                jwt.getClaim("email"),
                jwt.getClaim("upn")
            );
        }
        return null;
    }
    
    private String extractName(Jwt jwt, String provider) {
        if ("google".equals(provider)) {
            return jwt.getClaim("name");
        } else if ("microsoft".equals(provider)) {
            return firstNonBlank(
                jwt.getClaim("name"),
                buildName(
                    jwt.getClaim("given_name"),
                    jwt.getClaim("family_name")
                )
            );
        }
        return null;
    }
    
    private String extractPicture(Jwt jwt, String provider) {
        if ("google".equals(provider)) {
            return jwt.getClaim("picture");
        } else if ("microsoft".equals(provider)) {
            return jwt.getClaim("picture");
        }
        return null;
    }
    
    private String firstNonBlank(String... values) {
        if (values == null) return null;
        for (String v : values) {
            if (v != null && !v.isBlank()) return v;
        }
        return null;
    }
    
    private String buildName(String firstName, String lastName) {
        String first = firstName != null ? firstName : "";
        String last = lastName != null ? lastName : "";
        return (first + " " + last).trim();
    }
}
