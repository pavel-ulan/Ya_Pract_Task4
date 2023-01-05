package ru.yandex.prakticum.tests;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.prakticum.BaseScenario;

import static java.net.HttpURLConnection.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static ru.yandex.prakticum.steps.CourierSteps.*;

public class LoginCourierTests extends BaseScenario {
    @Before
    @Description("Test courier creation")
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
    @DisplayName("Courier creation")
    @Description("Basic test for positive courier authentication")
    public void checkCourierAuthenticated() {
        loginCourier(TEST_LOGIN, TEST_PASSWORD)
                .then()
                .assertThat()
                .body("id", notNullValue())
                .and()
                .statusCode(HTTP_OK);
    }

    @Test
    @DisplayName("Login without mandatory password")
    @Description("Error will be returned if try to login password = null")
    public void checkCourierCanNotLoginWithoutPassword() {
        loginCourier(TEST_LOGIN, null)
                .then()
                .assertThat()
                .body("message", equalTo("Учетная запись не найдена"))
                .and()
                .statusCode(HTTP_NOT_FOUND);
    }

    @Test
    @DisplayName("Login with incorrect password")
    @Description("Error will be returned if try to login with incorrect password")
    public void checkCourierCanNotLoginWithIncorrectPassword() {
        loginCourier(TEST_LOGIN, TEST_PASSWORD + "new")
                .then()
                .assertThat()
                .body("message", equalTo("Учетная запись не найдена"))
                .and()
                .statusCode(HTTP_NOT_FOUND);
    }

    @Test
    @DisplayName("Courier authentication without login field")
    @Description("Error must be returned if request body has no login field")
    public void checkErrorIfBodyHasNoMandatoryField() {
        loginCourier(null, TEST_PASSWORD)
                .then()
                .assertThat()
                .body("message", equalTo("Недостаточно данных для входа"))
                .and()
                .statusCode(HTTP_BAD_REQUEST);
    }

    @Test
    @DisplayName("Login with non existing user")
    @Description("Error must be returned if try to authenticate with non existing user")
    public void checkErrorForLoginNonExistingUser() {
        loginCourier(TEST_LOGIN + "new", TEST_PASSWORD)
                .then()
                .assertThat()
                .body("message", equalTo("Учетная запись не найдена"))
                .and()
                .statusCode(HTTP_NOT_FOUND);
    }

    @After
    public void clearData() {
        deleteCourier(TEST_LOGIN, TEST_PASSWORD);
    }
}
