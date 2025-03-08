package org.quizzcloud.backend.shared.exceptions;


import io.jsonwebtoken.JwtException;
import org.springframework.http.HttpStatus;

/**
 * Custom exception for JWT errors to standardize response messages.
 */
public class CustomJwtException extends JwtException {
    private final HttpStatus status;

    public CustomJwtException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
