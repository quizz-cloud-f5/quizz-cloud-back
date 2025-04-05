package org.quizzcloud.backend.auth.dto;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class LoginResponse {
    private String token;
    private String email;
    private String role;
}
