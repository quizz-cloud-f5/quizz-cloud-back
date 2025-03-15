package org.quizzcloud.backend.user.config;



import org.quizzcloud.backend.user.model.Role;
import org.quizzcloud.backend.user.model.User;
import org.quizzcloud.backend.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Value("${initial.admin.username}")
    private String adminName;
    @Value("${initial.admin.password}")
    private String adminPassword;

    @Value("${initial.user.username}")
    private String userName;
    @Value("${initial.user.password}")
    private String userPassword;

    public DataInitializer(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        createUserIfNotExists(adminName, adminPassword, Role.ADMIN);
        createUserIfNotExists(userName, userPassword, Role.USER);
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

