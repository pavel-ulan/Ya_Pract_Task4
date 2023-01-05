package ru.yandex.prakticum.tests;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Test;
import ru.yandex.prakticum.BaseScenario;

import static java.net.HttpURLConnection.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static ru.yandex.prakticum.steps.CourierSteps.createCourier;
import static ru.yandex.prakticum.steps.CourierSteps.deleteCourier;

public class CreateCourierTests extends BaseScenario {


    @Test
    @DisplayName("Courier creation")
    @Description("Basic test for positive courier creation")
    public void checkCourierCreation() {
        createCourier(TEST_LOGIN, TEST_PASSWORD, TEST_FIRST_NAME)
                .then()
                .assertThat()
                .statusCode(HTTP_CREATED)
                .and()
                .assertThat()
                .body("ok", is(true));
    }

    @Test
    @DisplayName("Can not create two the same couriers")
    @Description("Negative test for two the same couriers creation")
    public void checkCanNotCreateTwoTheSameCouriers() {
        createCourier(TEST_LOGIN, TEST_PASSWORD, TEST_FIRST_NAME)
                .then()
                .assertThat()
                .statusCode(HTTP_CREATED)
                .and()
                .assertThat()
                .body("ok", is(true));
        createCourier(TEST_LOGIN, TEST_PASSWORD, TEST_FIRST_NAME)
                .then()
                .assertThat()
                .statusCode(HTTP_CONFLICT)
                .and()
                .assertThat()
                .body("message", is("Этот логин уже используется. Попробуйте другой."));
    }

    @Test
    @DisplayName("Create courier with mandatory fields")
    @Description("Courier will be created if firstName is null")
    public void checkCourierCreatedWithMandatoryFields() {
        createCourier(TEST_LOGIN, TEST_PASSWORD, null)
                .then()
                .assertThat()
                .statusCode(HTTP_CREATED)
                .and()
                .assertThat()
                .body("ok", is(true));
    }

    @Test
    @DisplayName("Request body without a field")
    @Description("Error must be returned if request body has no password field")
    public void checkErrorIfBodyHasNoPssField() {
        createCourier(TEST_LOGIN, null, TEST_FIRST_NAME)
                .then()
                .assertThat()
                .statusCode(HTTP_BAD_REQUEST)
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Request body without a field")
    @Description("Error must be returned if request body has no login field")
    public void checkErrorIfBodyHasNoLoginField() {
        createCourier(null, TEST_PASSWORD, TEST_FIRST_NAME)
                .then()
                .assertThat()
                .statusCode(HTTP_BAD_REQUEST)
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Create courier with existing login")
    @Description("Error must be returned if try to create a courier with existing login")
    public void checkErrorForCourierCreationWithExistingLogin() {
        createCourier(TEST_LOGIN, TEST_PASSWORD, TEST_FIRST_NAME)
                .then()
                .assertThat()
                .statusCode(HTTP_CREATED)
                .and()
                .assertThat()
                .body("ok", is(true));
        createCourier(TEST_LOGIN, TEST_PASSWORD + "new", TEST_FIRST_NAME)
                .then()
                .assertThat()
                .statusCode(HTTP_CONFLICT)
                .and()
                .assertThat()
                .body("message", is("Этот логин уже используется. Попробуйте другой."));
    }

    @After
    public void clearData() {
        deleteCourier(TEST_LOGIN, TEST_PASSWORD);
    }
}
