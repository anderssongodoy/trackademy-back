package com.trackademy.security;

import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.util.StringUtils;

class JwtIssuerStartsWithValidator implements OAuth2TokenValidator<Jwt> {

    private final String requiredPrefix;

    JwtIssuerStartsWithValidator(String requiredPrefix) {
        this.requiredPrefix = requiredPrefix;
    }

    @Override
    public OAuth2TokenValidatorResult validate(Jwt token) {
        if (!StringUtils.hasText(requiredPrefix)) {
            return OAuth2TokenValidatorResult.success();
        }
        String iss = token.getIssuer() != null ? token.getIssuer().toString() : null;
        if (iss != null && iss.startsWith(requiredPrefix)) {
            return OAuth2TokenValidatorResult.success();
        }
        OAuth2Error error = new OAuth2Error("invalid_token", "The token issuer is not accepted", null);
        return OAuth2TokenValidatorResult.failure(error);
    }
}
