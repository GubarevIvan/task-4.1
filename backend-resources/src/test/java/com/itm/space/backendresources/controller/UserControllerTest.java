package com.itm.space.backendresources.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itm.space.backendresources.keycloak.KeycloakTest;
import com.itm.space.backendresources.service.UserService;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

//@SpringBootTest
//@ActiveProfiles("test")
public class UserControllerTest extends KeycloakTest{

   private String getToken() {
       assertTrue(keycloakContainer.isRunning());
       String authServerUrl = keycloakContainer.getAuthServerUrl();
       System.out.println("authServerUrl = "+ authServerUrl);

       return given().contentType("application/x-www-form-urlencoded")
               .formParams(Map.of(
                       "username", "test",
                       "password", "test",
                       "grant_type", "password",
                       "client_id", "backend-gateway-client",
                       "client_secret", "secret"
               ))
               .post(keycloakContainer.getAuthServerUrl() + "/realms/ITM/protocol/openid-connect/token")
               .then().assertThat().statusCode(200).extract()
               .path("access_token");
   }


    @Test
    // По токену получаем данные пользователя
    void testUserInfo() {
        Response response = given().auth().oauth2(getToken()).when()
                .get("http://localhost:9191/api/users/d54a5ada-fbbd-4491-b198-3681ca070d5b");
        response.then()
                .statusCode(200)
                .body("firstName", equalTo("Loan"))
                .body("lastName", equalTo("Gavrosh"))
                .body("email", equalTo("gavr@mail.ru"));

        System.out.println(response.getBody().asString());

//        assertEquals("  ", response.getBody().asString());
    }

}