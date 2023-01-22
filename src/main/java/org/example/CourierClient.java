package org.example;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

public class CourierClient extends Client {

    private static final String PATH_CREATE = "/api/v1/courier";
    private static final String PATH_LOGIN = "/api/v1/courier/login";
    private static final String PATH_DELETE = "/api/v1/courier/:id";

    @Step("Создание курьера ")
    public ValidatableResponse create(Courier courier){
        return given()
                .spec(getSpec())
                .body(courier)
                .when()
                .post(PATH_CREATE)
                .then();
    }

    @Step("Авторизация курьера")
    public ValidatableResponse login(CourierCredentials credentials){
        return given()
                .spec(getSpec())
                .body(credentials)
                .when()
                .post(PATH_LOGIN)
                .then();
    }

    @Step("Удаление курьера")
    public ValidatableResponse delete(int id){
        return given()
                .spec(getSpec())
                .body(id)
                .when()
                .delete(PATH_DELETE)
                .then();
    }
}
