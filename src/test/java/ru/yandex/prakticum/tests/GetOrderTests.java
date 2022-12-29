package ru.yandex.prakticum.tests;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import ru.yandex.prakticum.BaseScenario;
import ru.yandex.prakticum.model.Order;

import static java.net.HttpURLConnection.*;
import static org.hamcrest.CoreMatchers.is;
import static ru.yandex.prakticum.steps.OrderSteps.*;

public class GetOrderTests extends BaseScenario {

    private final Order order = new Order(TEST_FIRST_NAME, TEST_LAST_NAME, TEST_ADDRESS, TEST_METRO_STATION, TEST_PHONE, TEST_RENT_TIME, TEST_DELIVERY_DATE, TEST_COMMENT, TEST_COLORS);

    @Test
    @DisplayName("Get order")
    @Description("Positive receiving order by its track number")
    public void checkOrderAccept() throws JsonProcessingException {
        order.setTrack(createOrder(order).jsonPath().getString("track"));
        var serverOrder = getOrderByTrack(order.getTrack())
                .then()
                .assertThat()
                .statusCode(HTTP_OK)
                .extract()
                .body().asString().substring(9);
        ObjectMapper mapper = new ObjectMapper();
        Order readValue = mapper.readValue(serverOrder, Order.class);
        Assert.assertEquals(order, readValue);
    }

    @Test
    @DisplayName("Get order")
    @Description("Request without order number")
    public void orderAcceptWithoutTrack() {
        getOrderByTrack("")
                .then()
                .assertThat()
                .statusCode(HTTP_BAD_REQUEST)
                .assertThat()
                .body("message", is("Недостаточно данных для поиска"));
    }

    @Test
    @DisplayName("Get order")
    @Description("Request with invalid order number")
    public void orderAcceptWithInvalidTrack() {
        getOrderByTrack(INVALID_TRACK_ID)
                .then()
                .assertThat()
                .statusCode(HTTP_NOT_FOUND)
                .assertThat()
                .body("message", is("Заказ не найден"));
    }

    @After
    public void clearData() {
        cancelOrder(order.getTrack());
    }
}
