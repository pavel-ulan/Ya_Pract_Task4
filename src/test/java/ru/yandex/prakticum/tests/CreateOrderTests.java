package ru.yandex.prakticum.tests;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ru.yandex.prakticum.BaseScenario;
import ru.yandex.prakticum.model.Order;

import java.util.List;

import static java.net.HttpURLConnection.HTTP_CREATED;
import static org.hamcrest.Matchers.notNullValue;
import static ru.yandex.prakticum.steps.OrderSteps.cancelOrder;
import static ru.yandex.prakticum.steps.OrderSteps.createOrder;

@RunWith(Parameterized.class)
public class CreateOrderTests extends BaseScenario {

    private static final List<Order> ORDERS = List
            .of(new Order(TEST_FIRST_NAME, TEST_LAST_NAME, TEST_ADDRESS, TEST_METRO_STATION, TEST_PHONE, TEST_RENT_TIME, TEST_DELIVERY_DATE, TEST_COMMENT, TEST_COLOR_BLACK),
                    new Order(TEST_FIRST_NAME, TEST_LAST_NAME, TEST_ADDRESS, TEST_METRO_STATION, TEST_PHONE, TEST_RENT_TIME, TEST_DELIVERY_DATE, TEST_COMMENT, TEST_COLOR_GREY),
                    new Order(TEST_FIRST_NAME, TEST_LAST_NAME, TEST_ADDRESS, TEST_METRO_STATION, TEST_PHONE, TEST_RENT_TIME, TEST_DELIVERY_DATE, TEST_COMMENT, TEST_COLORS),
                    new Order(TEST_FIRST_NAME, TEST_LAST_NAME, TEST_ADDRESS, TEST_METRO_STATION, TEST_PHONE, TEST_RENT_TIME, TEST_DELIVERY_DATE, TEST_COMMENT));
    private final Order order;


    public CreateOrderTests(Order order) {
        this.order = order;
    }

    @Parameterized.Parameters
    public static List<Order> getOrderCreationTestData() {
        return ORDERS;
    }

    @Test
    @DisplayName("Create order")
    @Description("Positive order creation")
    public void checkOrderCreation() {
        var response = createOrder(order);
        response.then()
                .assertThat()
                .body("track", notNullValue())
                .and()
                .statusCode(HTTP_CREATED);
        order.setTrack(response.jsonPath().getString("track"));
    }

    @After
    public void clearData() {
        cancelOrder(order.getTrack());
    }
}
