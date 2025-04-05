package org.quizzcloud.backend.acceptance.auth;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.quizzcloud.backend.auth.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class LoginUserTest extends AcceptanceBaseTest {


    @Autowired
    private UserRepository userRepository;

    @Test
    void whenAppInitialize_thenTwoUsersExists() {
        Assertions.assertEquals(2, userRepository.count());
    }

    @Test
    void whenUserAdminLogins_thenReturnsAToken() throws Exception {
        String loginRequestJson = """
                {
                    "email": "%s",
                    "password": "%s"
                }
                """.formatted(adminName, adminPassword);

        mockMvc.perform(post("/auth/login")
                        .contentType("application/json")
                        .content(loginRequestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").isString())
                .andExpect(jsonPath("$.email").value(adminName))
                .andExpect(jsonPath("$.role").value("ADMIN"));
    }
}
