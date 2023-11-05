package com.itm.space.backendresources;

import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static javax.ws.rs.core.HttpHeaders.AUTHORIZATION;
import static junit.framework.TestCase.assertEquals;
import static org.hamcrest.Matchers.equalTo;

public class TestController extends KeycloakTest {
    @Test
    // роверяем пользователя по id
    void testMethodHello() {
        System.out.println("token = " + getToken());
        Response response = given()
                .header(AUTHORIZATION, getToken())
                .when()
                .get("/api/users/hello");
//                .get("http://localhost:9191/api/users/hello");
        response.then().statusCode(200);

        assertEquals("bcd4f19b-324b-4be8-8209-8b2d0ed80c30", response.getBody().asString());
    }

    @Test
    // Проверяем если пользователь не авторизован
    void testMethodAuth() {
        Response response = given()
                .header(AUTHORIZATION, "token")
                .when()
                .get("/api/users/hello");
//                .get("http://localhost:9191/api/users/hello");
        response.then().statusCode(401);
        System.out.println("У этого пльзователя нет прав Модератера");
    }

    @Test
    // Проверяем данные пользователя
    void testMethodDataUser() {
        Response response = given()
                .header(AUTHORIZATION, getToken())
                .when()
                .get("/api/users/bcd4f19b-324b-4be8-8209-8b2d0ed80c30");
//                .get("http://localhost:9191/api/users/bcd4f19b-324b-4be8-8209-8b2d0ed80c30");
        response.then()
                .statusCode(200)
                .body("firstName", equalTo("Loan"))
                .body("lastName", equalTo("Gavrosh"))
                .body("email", equalTo("gavr@mail.ru"));
        System.out.println(response.getBody().asString());
    }
}