package org.quizzcloud.backend.auth.config;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.quizzcloud.backend.auth.exceptions.CustomJwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;

import java.io.IOException;

/**
 * Handles JWT authentication errors and ensures a standardized response.
 */
@Component
public class JwtAuthEntryPoint implements AuthenticationEntryPoint {

    private static final Logger LOGGER = LoggerFactory.getLogger(JwtAuthEntryPoint.class);

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException {

        Throwable cause = authException.getCause();
        CustomJwtException jwtException;

        if (cause instanceof ExpiredJwtException) {
            jwtException = new CustomJwtException("Token has expired. Please log in again.", HttpStatus.UNAUTHORIZED);
        } else if (cause instanceof MalformedJwtException) {
            jwtException = new CustomJwtException("Token is malformed. Please provide a valid token.", HttpStatus.UNAUTHORIZED);
        } else {
            jwtException = new CustomJwtException("Invalid or expired token.", HttpStatus.UNAUTHORIZED);
        }

        LOGGER.warn("Unauthorized request: {}", jwtException.getMessage());

        response.setContentType("application/json");
        response.setStatus(jwtException.getStatus().value());
        response.getWriter().write(
                String.format("{\"timestamp\": %d, \"status\": %d, \"error\": \"Unauthorized\", \"message\": \"%s\", \"path\": \"%s\"}",
                        System.currentTimeMillis(),
                        jwtException.getStatus().value(),
                        jwtException.getMessage(),
                        request.getRequestURI())
        );
    }
}

