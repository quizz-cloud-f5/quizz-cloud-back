package org.quizzcloud.backend.shared.exceptions;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

/**
 * Global exception handler for managing application-wide exceptions.
 * This ensures consistent error responses across the system.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * Handles `ResourceNotFoundException` when a requested resource is not found.
     *
     * @param ex The exception thrown when a resource is missing.
     * @return A `ResponseEntity` containing an error response with status 404 (NOT FOUND).
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFound(ResourceNotFoundException ex) {
        LOGGER.warn("Resource not found: {}", ex.getMessage());
        return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    /**
     * Handles `MethodArgumentNotValidException`, which occurs when request validation fails.
     *
     * @param ex The exception thrown due to validation errors.
     * @return A `ResponseEntity` containing detailed validation errors with status 400 (BAD REQUEST).
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationErrors(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }
        LOGGER.warn("Validation errors: {}", errors);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(Instant.now(), HttpStatus.BAD_REQUEST.value(), "Validation errors", errors));
    }

    /**
     * Handles authentication failures due to incorrect credentials.
     * @param ex The exception thrown by Spring Security when authentication fails.
     * @return A `401 Unauthorized` response.
     */
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentials(BadCredentialsException ex) {
        LOGGER.warn("Authentication failed: {}", ex.getMessage());
        return buildResponse(HttpStatus.UNAUTHORIZED, "Invalid email or password.");
    }


    /**
     * Handles general exceptions that are not explicitly caught by other handlers.
     *
     * @param ex The exception thrown during execution.
     * @return A `ResponseEntity` containing a generic error response with status 500 (INTERNAL SERVER ERROR).
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneralException(Exception ex) {
        LOGGER.error("Unexpected error: {}", ex.getMessage(), ex);
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred.");
    }

    /**
     * Utility method to create a standardized error response.
     *
     * @param status  The HTTP status to be returned.
     * @param message The error message to be included in the response.
     * @return A `ResponseEntity` containing an error response.
     */
    private ResponseEntity<ErrorResponse> buildResponse(HttpStatus status, String message) {
        return ResponseEntity.status(status)
                .body(new ErrorResponse(Instant.now(), status.value(), message, null));
    }
}
