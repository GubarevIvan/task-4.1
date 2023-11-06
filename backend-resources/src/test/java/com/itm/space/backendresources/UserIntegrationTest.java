package com.itm.space.backendresources;

import com.fasterxml.jackson.databind.*;
import com.itm.space.backendresources.api.request.UserRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.keycloak.admin.client.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.*;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.hamcrest.Matchers.equalTo;

@SpringBootTest
@AutoConfigureMockMvc
public class UserIntegrationTest extends BaseIntegrationTest{
    @Autowired
    private MockMvc mockMvc;
    private ObjectWriter objectMapper;

    @BeforeEach
    void setup() {
        objectMapper = new ObjectMapper().writer().withDefaultPrettyPrinter();
    }
    private String getToken() {
        try (Keycloak keycloakClient = KeycloakBuilder.builder()
                .serverUrl("http://backend-keycloak-auth:8080/auth")
                .realm("ITM")
                .clientId("backend-gateway-client")
                .username("loan")
                .password("test")
                .clientSecret("3FfaoUTdiy7FNc7V4H1AZ2sXCIBuCL9R")
                .build()) {
            return keycloakClient.tokenManager().getAccessToken().getToken();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    @Test
    public void testCreateUser() throws Exception {
        UserRequest userRequest = new UserRequest("testuser", "testuser@example.com",
                "testpassword", "TestName", "TestLastName");
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequest))
                        .header("Authorization", "Bearer " + getToken()))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testGetUserById() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/users/{id}", "9b2100fe-8ca7-43db-b6da-77b9d58ceba5")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + getToken()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName", equalTo("Loan")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName", equalTo("Gavrosh")));
    }

    @Test
    public void testHello() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/users/hello")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + getToken()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("9b2100fe-8ca7-43db-b6da-77b9d58ceba5"));
    }
}