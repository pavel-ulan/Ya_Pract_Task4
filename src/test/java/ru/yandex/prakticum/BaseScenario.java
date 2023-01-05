package ru.yandex.prakticum;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.HttpClientConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.junit.Before;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

public class BaseScenario {
    protected static final String TEST_LOGIN = "CustomLogin";
    protected static final String TEST_PASSWORD = "123456";
    protected static final String TEST_FIRST_NAME = "CustomFirstName";
    protected static final String TEST_LAST_NAME = "CustomLastName";
    protected static final String TEST_ADDRESS = "CustomAddress";
    protected static final String TEST_METRO_STATION = "Черкизовская";
    protected static final String TEST_PHONE = "+123456789";
    protected static final int TEST_RENT_TIME = 1;
    protected static final String TEST_DELIVERY_DATE = LocalDate.now().plusDays(1).toString();
    protected static final String TEST_COMMENT = "CustomComment";
    protected static final List<String> TEST_COLOR_BLACK = List.of("BLACK");
    protected static final List<String> TEST_COLOR_GREY = List.of("GREY");
    protected static final List<String> TEST_COLORS = Arrays.asList("BLACK", "GREY");
    protected static final String INVALID_COURIER_ID = "0";
    protected static final String INVALID_TRACK_ID = "0";
    private static final String BASE_URI = "https://qa-scooter.praktikum-services.ru/";

    @Before
    public void setUp() {
        RestAssuredConfig.config().httpClient(HttpClientConfig.httpClientConfig().httpClientFactory(() -> {
            var cm = new PoolingHttpClientConnectionManager();
            cm.setMaxTotal(50);
            cm.setDefaultMaxPerRoute(20);
            return HttpClients.custom()
                    .setConnectionManager(cm)
                    .build();
        }).and().dontReuseHttpClientInstance());
        RestAssured.replaceFiltersWith(new AllureRestAssured());
        RestAssured.requestSpecification = new RequestSpecBuilder()
                .setBaseUri(BASE_URI)
                .addHeader("Content-type", "application/json")
                .setAccept(ContentType.JSON)
                .log(LogDetail.ALL)
                .build();
        RestAssured.filters(new ResponseLoggingFilter());
    }
}
