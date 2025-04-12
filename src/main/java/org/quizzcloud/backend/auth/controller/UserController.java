package org.quizzcloud.backend.auth.controller;

import lombok.RequiredArgsConstructor;
import org.quizzcloud.backend.auth.dto.UserRegisterRequest;
import org.quizzcloud.backend.auth.model.User;
import org.quizzcloud.backend.auth.service.FindUsersService;
import org.quizzcloud.backend.auth.service.RegisterUserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final RegisterUserService registerUserService;
    private final FindUsersService findUsersService;

    @PostMapping
    public ResponseEntity<User> registerNewUser(@RequestBody UserRegisterRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(registerUserService.registerUser(request.email(), request.role()));
    }

    @GetMapping
    public ResponseEntity<List<User>> findUsersList() {
        List<User> users = findUsersService.findAllUsers();
        return ResponseEntity.ok(users);
    }
}
