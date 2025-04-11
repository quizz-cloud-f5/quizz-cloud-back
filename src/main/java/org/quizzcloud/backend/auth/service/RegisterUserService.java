package org.quizzcloud.backend.auth.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quizzcloud.backend.auth.model.Role;
import org.quizzcloud.backend.auth.model.User;
import org.quizzcloud.backend.auth.repository.UserRepository;
import org.quizzcloud.backend.shared.exceptions.ResourceAlreadyExistsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class RegisterUserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User registerUser(String email, String role) {
        if (userRepository.existsByEmail(email)) {
            throw new ResourceAlreadyExistsException("User already exists with email:" + email);
        }

        if (role == null || role.isEmpty()) {
            log.error("Role cannot be null or empty");
            throw new IllegalArgumentException("Role cannot be null or empty");

        }
        Role userRole = Role.valueOf(role.toUpperCase());

        String plainPassword = generateRandomPassword();
        String encodedPassword = encodePassword(plainPassword);

        User newUser = new User();
        newUser.setEmail(email);
        newUser.setRole(userRole);
        newUser.setPassword(encodedPassword);

        userRepository.save(newUser);
        log.info("User created: {} / {}", email, plainPassword);

        return newUser;
    }

    private String generateRandomPassword() {
        return PasswordGenerator.generateRandomPassword();
    }

    private String encodePassword(String createDefaultPassword) {
        return passwordEncoder.encode(createDefaultPassword);
    }
}
