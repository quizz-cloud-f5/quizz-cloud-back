package org.quizzcloud.backend.acceptance.auth;

import com.jayway.jsonpath.JsonPath;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureMockMvc
public abstract class AcceptanceBaseTest {

    @Value("${initial.admin.username}")
    protected String adminName;

    @Value("${initial.admin.password}")
    protected String adminPassword;

    @Value("${initial.user.username}")
    protected String userName;

    @Value("${initial.user.password}")
    protected String userPassword;

    @Autowired
    protected MockMvc mockMvc;

    protected String getAdminToken() throws Exception {
        return obtainToken(adminName, adminPassword);
    }

    protected String getUserToken() throws Exception {
        return obtainToken(userName, userPassword);
    }

    protected String obtainToken(String email, String password) throws Exception {
        String payload = """
                {
                  "email": "%s",
                  "password": "%s"
                }
                """.formatted(email, password);

        MvcResult result = mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isOk())
                .andReturn();

        return JsonPath.read(result.getResponse().getContentAsString(), "$.token");
    }

}
