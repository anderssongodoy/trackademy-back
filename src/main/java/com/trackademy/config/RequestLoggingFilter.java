package com.trackademy.config;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class RequestLoggingFilter implements Filter {
    
    private static final Logger logger = LoggerFactory.getLogger(RequestLoggingFilter.class);
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String authHeader = httpRequest.getHeader("Authorization");
        
        logger.info("Request: {} {} - Auth header present: {}", 
                httpRequest.getMethod(), 
                httpRequest.getRequestURI(),
                authHeader != null);
        
        if (authHeader != null) {
            logger.debug("Authorization header: {}", authHeader.substring(0, Math.min(50, authHeader.length())) + "...");
        }
        
        chain.doFilter(request, response);
    }
}
