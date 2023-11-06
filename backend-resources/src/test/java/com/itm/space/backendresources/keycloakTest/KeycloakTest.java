package com.itm.space.backendresources.keycloakTest;

import dasniko.testcontainers.keycloak.KeycloakContainer;
import org.keycloak.admin.client.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.*;
import org.testcontainers.junit.jupiter.*;

@SpringBootTest
@Testcontainers(disabledWithoutDocker = true)
public class KeycloakTest {

    @Container
    static KeycloakContainer keycloakContainer = new KeycloakContainer()
            .withRealmImportFile("/keycloak/ITM-realm.json");

    @DynamicPropertySource
    static void registerServerProperty(DynamicPropertyRegistry registry) {
        registry.add("spring.security.oauth2.resourceserver.jwt.issuer-uri",
                () -> keycloakContainer.getAuthServerUrl() + "realm/ITM");
        registry.add("keycloak.auth-server-url",
                () -> keycloakContainer.getAuthServerUrl());
    }

    protected String getToken() {
        try (Keycloak keycloakClient = KeycloakBuilder.builder()
                .serverUrl("http://backend-keycloak-auth:8080/auth")
                .grantType("password")
                .realm("ITM")
                .clientId("backend-gateway-client")
                .username("loan")
                .password("test")
                .clientSecret("3FfaoUTdiy7FNc7V4H1AZ2sXCIBuCL9R")
                .build()) {
            String access_token = keycloakClient.tokenManager().getAccessToken().getToken();
            return "Bearer " + access_token;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}