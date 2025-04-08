package org.quizzcloud.backend.auth.controller;

import lombok.RequiredArgsConstructor;
import org.quizzcloud.backend.auth.dto.UserRegisterRequest;
import org.quizzcloud.backend.auth.model.User;
import org.quizzcloud.backend.auth.service.RegisterUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final RegisterUserService registerUserService;

    @PostMapping
    public ResponseEntity<User> registerNewUser(@RequestBody UserRegisterRequest request) {
        String email = request.email();
        return ResponseEntity.ok(registerUserService.registerUser(email, request.role()));
    }
}
