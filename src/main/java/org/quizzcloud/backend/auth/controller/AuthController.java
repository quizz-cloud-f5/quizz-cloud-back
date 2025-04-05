package org.quizzcloud.backend.auth.controller;

import jakarta.validation.Valid;
import org.quizzcloud.backend.auth.dto.LoginRequest;
import org.quizzcloud.backend.auth.dto.LoginResponse;
import org.quizzcloud.backend.auth.service.LoginService;
import org.quizzcloud.backend.auth.model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final LoginService loginService;

    public AuthController(LoginService loginService) {
        this.loginService = loginService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest request) {
        return ResponseEntity.ok(loginService.login(request.email(), request.password()));
    }

    @GetMapping("/me")
    public ResponseEntity<User> getAuthenticatedUser(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(user);
    }
}
