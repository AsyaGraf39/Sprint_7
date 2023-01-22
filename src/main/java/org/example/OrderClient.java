package org.example;

import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

public class OrderClient  extends Client {

    private static final String PATH_ORDER = "/api/v1/orders";
    private static final String PATH_CANCEL_ORDER = "/api/v1/orders/cancel";

    @Step("Создание заказа")
    public ValidatableResponse create(Order order){
        return given()
                .spec(getSpec())
                .body(order)
                .when()
                .post(PATH_ORDER)
                .then();
    }

    @Step("Отправление запроса на получение списка заказов, проверка успешного ответа и десериализация ответа")
    public OrderList orderList (){
        return given()
                .spec(getSpec())
                .get(PATH_ORDER)
                .then()
                .statusCode(200)
                .extract().body().as(OrderList.class);
    }

    @Step("Отмена заказа")
    public ValidatableResponse cancelOrder(int track){
        return given()
                .spec(getSpec())
                .body(track)
                .when()
                .post(PATH_CANCEL_ORDER)
                .then();
    }
}
