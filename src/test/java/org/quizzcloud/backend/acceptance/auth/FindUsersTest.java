package org.quizzcloud.backend.acceptance.auth;

import org.junit.jupiter.api.Test;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

class FindUsersTest extends AcceptanceBaseTest {

    @Test
    void finsAllUsers() throws Exception {
        String adminToken = getAdminToken();

        mockMvc.perform(get("/api/users")
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk());
    }
}
