package org.quizzcloud.backend.auth.controller;

import jakarta.validation.Valid;
import org.quizzcloud.backend.auth.dto.LoginRequest;
import org.quizzcloud.backend.auth.dto.LoginResponse;
import org.quizzcloud.backend.auth.service.AuthService;
import org.quizzcloud.backend.user.model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest request) {
        return ResponseEntity.ok(authService.login(request.getEmail(), request.getPassword()));
    }

    @GetMapping("/me")
    public ResponseEntity<User> getAuthenticatedUser(@AuthenticationPrincipal User user) {
//        MeResponse meResponse = MeResponse.builder()
//                .id(user.getId())
//                .email(user.getEmail())
//                .role(user.getRole().name())
//                .firstLogin(user.isFirstLogin())
//                .build();

        return ResponseEntity.ok(user);
    }
}
