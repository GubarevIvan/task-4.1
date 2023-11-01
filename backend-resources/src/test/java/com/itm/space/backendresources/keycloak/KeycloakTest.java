package com.itm.space.backendresources.keycloak;

import dasniko.testcontainers.keycloak.KeycloakContainer;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.lifecycle.Startables;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
public class KeycloakTest {
    @Container
    public static KeycloakContainer keycloakContainer = new KeycloakContainer()
            .withRealmImportFile("/keycloak/ITM.json");


    @DynamicPropertySource
    public static void registerServerProperty(DynamicPropertyRegistry registry) {
        registry.add("spring.security.oauth2.resourceserver.jwt.issuer-uri",
                () -> keycloakContainer.getAuthServerUrl() + "/realm/ITM");
        registry.add("Keycloak.auth-server-url",
                () -> keycloakContainer.getAuthServerUrl());
    }
}