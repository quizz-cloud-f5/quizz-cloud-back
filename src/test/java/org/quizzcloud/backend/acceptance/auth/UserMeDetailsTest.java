package org.quizzcloud.backend.acceptance.auth;

import org.junit.jupiter.api.Test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UserMeDetailsTest extends AcceptanceBaseTest {

    @Test
    void authenticatedUser_canSeeOwnDetails() throws Exception {
        mockMvc.perform(get("/auth/me")
                        .header("Authorization", "Bearer " + getUserToken()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(userName))
                .andExpect(jsonPath("$.role").value("USER"));
    }

    @Test
    void authenticatedAdmin_canSeeOwnDetails() throws Exception {
        mockMvc.perform(get("/auth/me")
                        .header("Authorization", "Bearer " + getAdminToken()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(adminName))
                .andExpect(jsonPath("$.role").value("ADMIN"));
    }

}
