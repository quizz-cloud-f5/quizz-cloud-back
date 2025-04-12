package org.quizzcloud.backend.acceptance.auth;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.quizzcloud.backend.auth.model.Role;
import org.quizzcloud.backend.auth.model.User;
import org.quizzcloud.backend.auth.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class RegisterUserTest extends AcceptanceBaseTest {
    @Autowired
    private UserRepository userRepository;

    @Test
    void whenAdminRegisterNewUser_thenUserIsCreated() throws Exception {
        String adminToken = getAdminToken();
        String userEmail = "newUser@gmail.com";

        String registerRequestJson = """
                {
                    "email": "%s",
                    "role": "%s"
                }
                """.formatted(userEmail, "ADMIN");

        mockMvc.perform(post("/api/users")
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType("application/json")
                        .content(registerRequestJson))
                .andExpect(status().isCreated());

        // Verify that the user was created in the database
        User newUser = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new AssertionError("User should have been created: " + userEmail));

        assertAll(
                () -> assertEquals(userEmail, newUser.getEmail(), "Email does not match"),
                () -> assertEquals(Role.ADMIN, newUser.getRole(), "Role should be ADMIN")
        );
    }

    @Test
    void whenUserRegisterNewUser_thenReturnsNonAuthorizes() throws Exception {
        String userToken = getUserToken();
        String userEmail = "newUser@gmail.com";

        String registerRequestJson = """
                {
                    "email": "%s",
                    "role": "%s"
                }
                """.formatted(userEmail, "ADMIN");

        mockMvc.perform(post("/api/users")
                        .header("Authorization", "Bearer " + userToken)
                        .contentType("application/json")
                        .content(registerRequestJson))
                .andExpect(status().isForbidden());

        // Verify that the user was not created in the database
        boolean userExists = userRepository.existsByEmail(userEmail);
        Assertions.assertFalse(userExists, "User should not have been created");
    }
}
