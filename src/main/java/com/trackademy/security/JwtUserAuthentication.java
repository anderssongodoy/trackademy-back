package com.trackademy.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.oauth2.jwt.Jwt;
import com.trackademy.entity.User;
import lombok.Getter;

import java.util.Collection;
import org.springframework.security.core.GrantedAuthority;

@Getter
public class JwtUserAuthentication extends AbstractAuthenticationToken {
    
    private final Jwt jwt;
    private final User user;
    
    public JwtUserAuthentication(Jwt jwt, User user, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.jwt = jwt;
        this.user = user;
        setAuthenticated(true);
    }
    
    @Override
    public Object getCredentials() {
        return jwt.getTokenValue();
    }
    
    @Override
    public Object getPrincipal() {
        return user;
    }
}
