package com.itm.space.backendresources.keycloakTest;

import com.itm.space.backendresources.keycloakTest.KeycloakTest;
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
    Response response = given()
            .header(AUTHORIZATION, getToken())
            .when()
                .get("http://localhost:9191/api/users/hello");
    response.then().statusCode(200);

    assertEquals("9b2100fe-8ca7-43db-b6da-77b9d58ceba5", response.getBody().asString());
        System.out.println("У этого пользователя права Модератора");
}

    @Test
        // Проверяем если пользователь не авторизован
    void testMethodAuth() {
        Response response = given()
                .header(AUTHORIZATION, "token")
                .when()
                .get("http://localhost:9191/api/users/hello");
        response.then().statusCode(401);
        System.out.println("У этого пльзователя нет прав Модератера");
    }

    @Test
        // Проверяем данные пользователя
    void testMethodDataUser() {
        Response response = given()
                .header(AUTHORIZATION, getToken())
                .when()
                .get("http://localhost:9191/api/users/9b2100fe-8ca7-43db-b6da-77b9d58ceba5");
        response.then()
                .statusCode(200)
                .body("firstName", equalTo("Loan"))
                .body("lastName", equalTo("Gavrosh"))
                .body("email", equalTo("gavr@mail.ru"));
        System.out.println("Данные ползователя верны");
    }
}