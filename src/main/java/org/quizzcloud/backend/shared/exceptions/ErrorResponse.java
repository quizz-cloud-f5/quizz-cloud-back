package org.quizzcloud.backend.shared.exceptions;


import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Instant;
import java.util.Map;

@Getter
@AllArgsConstructor
public class ErrorResponse {
    private Instant timestamp;
    private int status;
    private String message;
    private Map<String, String> errors; // Para validaciones específicas
}

