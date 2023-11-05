package com.itm.space.backendresources;

import dasniko.testcontainers.keycloak.KeycloakContainer;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import javax.annotation.PostConstruct;

import java.util.Map;

import static io.restassured.RestAssured.given;

@Testcontainers(disabledWithoutDocker = true)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class KeycloakTest {
    @LocalServerPort
    private int port;

    @Container
    static KeycloakContainer keycloakContainer = new KeycloakContainer()
            .withRealmImportFile("/keycloak/ITM-realm.json");

    @DynamicPropertySource
    static void registerServerProperty(DynamicPropertyRegistry registry) {
        registry.add("spring.security.oauth2.resourceserver.jwt.issuer-uri",
                () -> keycloakContainer.getAuthServerUrl() + "/realm/ITM");
//        registry.add("keycloak.auth-server-url",
//                () -> keycloakContainer.getAuthServerUrl());
    }

    @BeforeEach
    protected void setUp(){
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
    }

    protected String getToken() {
        try (Keycloak keycloakClient = KeycloakBuilder.builder()
                .serverUrl(keycloakContainer.getAuthServerUrl())
                .grantType("password")
                .realm("ITM")
                .clientId("backend-gateway-client")
                .username("loan")
                .password("test")
                .clientSecret("Buos4oTBPvebndrM9ulksl9x4HaiL88e")
                .build()) {
            String access_token = keycloakClient.tokenManager().getAccessToken().getToken();
            return "Bearer " + access_token;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

//        protected String getToken() {
//        String token = given().contentType("application/x-www-form-urlencoded")
//                .formParams(Map.of(
//                        "username", "loan",
//                        "password", "test",
//                        "grant_type", "password",
//                        "client_id", "backend-gateway-client",
//                        "client_secret", "Buos4oTBPvebndrM9ulksl9x4HaiL88e"
//                ))
//                .post( "http://backend-keycloak-auth:8080/auth/realms/ITM/protocol/openid-connect/token")
//                .then().assertThat().statusCode(200).extract()
//                .path("access_token");
//        return "Bearer " + token;
//    }
}