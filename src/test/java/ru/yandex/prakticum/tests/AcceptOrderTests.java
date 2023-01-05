package ru.yandex.prakticum.tests;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Test;
import ru.yandex.prakticum.BaseScenario;
import ru.yandex.prakticum.model.CourierModel;
import ru.yandex.prakticum.model.OrderModel;

import static java.net.HttpURLConnection.*;
import static org.hamcrest.CoreMatchers.is;
import static ru.yandex.prakticum.steps.CourierSteps.*;
import static ru.yandex.prakticum.steps.OrderSteps.*;

public class AcceptOrderTests extends BaseScenario {

    private final OrderModel order = new OrderModel(TEST_FIRST_NAME, TEST_LAST_NAME, TEST_ADDRESS, TEST_METRO_STATION, TEST_PHONE, TEST_RENT_TIME, TEST_DELIVERY_DATE, TEST_COMMENT, TEST_COLORS);
    private final CourierModel courier = new CourierModel(TEST_LOGIN, TEST_PASSWORD, TEST_FIRST_NAME);

    @Test
    @DisplayName("Accept order")
    @Description("Positive order accept")
    public void checkOrderAccept() {
        createCourier(courier);
        courier.setId(loginCourier(courier.getLogin(), courier.getPassword()).jsonPath().getString("id"));
        order.setTrack(createOrder(order).jsonPath().getString("track"));
        acceptOrder(order, courier)
                .then()
                .assertThat()
                .statusCode(HTTP_OK)
                .assertThat()
                .body("ok", is(true));
    }

    @Test
    @DisplayName("Accept order")
    @Description("Order accept without courier id")
    public void checkOrderAcceptWithoutId() {
        createCourier(courier);
        courier.setId("");
        order.setTrack(createOrder(order).jsonPath().getString("track"));
        acceptOrder(order, courier)
                .then()
                .assertThat()
                .statusCode(HTTP_BAD_REQUEST)
                .assertThat()
                .body("message", is("Недостаточно данных для поиска"));
    }

    @Test
    @DisplayName("Accept order")
    @Description("Order accept with invalid courier id")
    public void checkOrderAcceptWithInvalidCourierId() {
        createCourier(courier);
        courier.setId(INVALID_COURIER_ID);
        order.setTrack(createOrder(order).jsonPath().getString("track"));
        acceptOrder(order, courier)
                .then()
                .assertThat()
                .statusCode(HTTP_NOT_FOUND)
                .assertThat()
                .body("message", is("Курьера с таким id не существует"));
    }

    @Test
    @DisplayName("Accept order")
    @Description("Accept order without order track number")
    public void checkOrderAcceptWithoutTrack() {
        createCourier(courier);
        courier.setId(loginCourier(courier.getLogin(), courier.getPassword()).jsonPath().getString("id"));
        createOrder(order);
        order.setTrack("");
        acceptOrder(order, courier)
                .then()
                .assertThat()
                .statusCode(HTTP_BAD_REQUEST)
                .assertThat()
                .body("message", is("Недостаточно данных для поиска"));
    }

    @Test
    @DisplayName("Accept order")
    @Description("Accept order with invalid order track number")
    public void checkOrderAcceptWithInvalidTrack() {
        createCourier(courier);
        courier.setId(loginCourier(courier.getLogin(), courier.getPassword()).jsonPath().getString("id"));
        createOrder(order);
        order.setTrack(INVALID_TRACK_ID);
        acceptOrder(order, courier)
                .then()
                .assertThat()
                .statusCode(HTTP_NOT_FOUND)
                .assertThat()
                .body("message", is("Заказа с таким id не существует"));
    }

    @After
    public void clearData() {
        cancelOrder(order.getTrack());
        deleteCourier(TEST_LOGIN, TEST_PASSWORD);
    }
}
