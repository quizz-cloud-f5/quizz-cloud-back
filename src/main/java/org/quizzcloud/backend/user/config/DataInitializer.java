package org.quizzcloud.backend.user.config;



import org.quizzcloud.backend.user.model.Role;
import org.quizzcloud.backend.user.model.User;
import org.quizzcloud.backend.user.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        createUserIfNotExists("admin@example.com", "admin123", Role.ADMIN);
        createUserIfNotExists("user@example.com", "user123", Role.USER);
    }

    private void createUserIfNotExists(String email, String password, Role role) {
        if (userRepository.findByEmail(email).isEmpty()) {
            User user = User.builder()
                    .email(email)
                    .password(passwordEncoder.encode(password))
                    .role(role)
                    .firstLogin(false)
                    .build();
            userRepository.save(user);
            System.out.println("Usuario creado: " + email + " / " + password);
        }
    }
}

