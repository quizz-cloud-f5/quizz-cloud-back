package org.quizzcloud.backend.auth.dto;

public record UserRegisterRequest(
        String role,
        String email
) {
}
