package ru.yandex.prakticum.tests;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.junit.Test;
import ru.yandex.prakticum.BaseScenario;

import static java.net.HttpURLConnection.HTTP_NOT_FOUND;
import static java.net.HttpURLConnection.HTTP_OK;
import static org.hamcrest.CoreMatchers.is;
import static ru.yandex.prakticum.steps.CourierSteps.createCourier;
import static ru.yandex.prakticum.steps.CourierSteps.deleteCourier;

public class DeleteCourierTests extends BaseScenario {

    @Test
    @DisplayName("Courier delete")
    @Description("Basic test for courier delete")
    public void checkCourierDelete() {
        createCourier(TEST_LOGIN, TEST_PASSWORD, TEST_FIRST_NAME);
        deleteCourier(TEST_LOGIN, TEST_PASSWORD)
                .then()
                .assertThat()
                .statusCode(HTTP_OK)
                .assertThat()
                .body("ok", is(true));
    }

    @Test
    @DisplayName("Request with invalid id")
    @Description("Error must be returned if request has invalid id")
    public void checkCourierDeleteWithoutId() {
        deleteCourier(INVALID_COURIER_ID)
                .then()
                .assertThat()
                .statusCode(HTTP_NOT_FOUND)
                .assertThat()
                .body("message", is("Курьера с таким id нет."));
    }

}
