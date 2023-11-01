package com.itm.space.backendresources.controller;

import dasniko.testcontainers.keycloak.KeycloakContainer;
import io.restassured.RestAssured;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import javax.annotation.PostConstruct;

@Testcontainers(disabledWithoutDocker = true)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class KeycloakTest {
    @LocalServerPort
    private int port;

    @PostConstruct
    public void setUp(){
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
    }
    @Container
    static KeycloakContainer keycloakContainer = new KeycloakContainer()
            .withRealmImportFile("/keycloak/ITM-realm.json");

    @DynamicPropertySource
    static void registerServerProperty(DynamicPropertyRegistry registry) {
        registry.add("spring.security.oauth2.resourceserver.jwt.issuer-uri",
                () -> keycloakContainer.getAuthServerUrl() + "realm/ITM");
        registry.add("Keycloak.auth-server-url",
                () -> keycloakContainer.getAuthServerUrl());
    }
}