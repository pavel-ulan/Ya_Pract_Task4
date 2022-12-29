package ru.yandex.prakticum.steps;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import ru.yandex.prakticum.model.Courier;
import ru.yandex.prakticum.model.Order;

import static io.restassured.RestAssured.given;
import static ru.yandex.prakticum.constant.Endpoints.*;

public class OrderSteps {

    @Step("Create order")
    public static Response createOrder(Order order) {
        return given()
                .body(order)
                .when()
                .post(ORDERS)
                .then()
                .extract()
                .response();
    }

    @Step("Get orders list")
    public static Response getOrders() {
        return given()
                .when()
                .get(ORDERS)
                .then()
                .extract()
                .response();
    }

    @Step("Accept order")
    public static Response acceptOrder(Order order, Courier courier) {
        return given()
                .queryParam("courierId", courier.getId())
                .when()
                .put(String.format(ACCEPT_ORDER, order.getTrack()))
                .then()
                .extract()
                .response();
    }

    @Step("Accept order")
    public static Response getOrderByTrack(String track) {
        return given()
                .queryParam("t", track)
                .when()
                .get(GET_ORDER)
                .then()
                .extract()
                .response();
    }

    @Step("Cancel order")
    public static void cancelOrder(String track) {
        given()
                .queryParam("track", track)
                .when()
                .put(CANCEL_ORDER);
    }
}
