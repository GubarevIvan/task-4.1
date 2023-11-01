package com.itm.space.backendresources.controller;

import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;

import static io.restassured.RestAssured.given;
import static junit.framework.TestCase.assertEquals;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

public class UserControllerTest extends KeycloakTest{

       private String getToken() {
           try (Keycloak keycloakClient = KeycloakBuilder.builder()
                   .serverUrl(keycloakContainer.getAuthServerUrl())
                   .grantType("password")
                   .realm("ITM")
                   .clientId("backend-gateway-client")
                   .username("loan")
                   .password("test")
                   .clientSecret("4gKxjs5ECAqKL0KkPldoyeFfLUCZL3hg")
                   .build()) {
               String access_token = keycloakClient.tokenManager().getAccessToken().getToken();
               return "Bearer " + access_token;
           } catch (Exception e) {
               e.printStackTrace();
           }
           return null;
       }
    @Test
    void testMethod_hello() {
        Response response = given()
                .header(AUTHORIZATION, getToken())
                .when()
                .get( "/api/users/hello");
        response.then().statusCode(200);

        assertEquals("cfa709c8-3719-46aa-bfae-89e8d319b92f", response.getBody().asString());
        System.out.println("token = " + getToken());
    }

    @Test
    // По токену получаем данные пользователя
    void testUserInfo() {
        System.out.println(getToken());
        Response response = given().header(AUTHORIZATION, getToken()).when()
                .get("api/users/cfa709c8-3719-46aa-bfae-89e8d319b92f");
        response.then()
                .statusCode(200)
                .body("firstName", equalTo("Loan"))
                .body("lastName", equalTo("Gavrosh"))
                .body("email", equalTo("gavr@mail.ru"));

        System.out.println(response.getBody().asString());
    }
}