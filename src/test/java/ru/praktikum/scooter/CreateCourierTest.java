package ru.praktikum.scooter;

import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import static org.hamcrest.CoreMatchers.equalTo;

public class CreateCourierTest {
    Courier courierWithFullData = new Courier("nickex", "nickex1234", "Никита");
    Courier courierWithoutLogin = new Courier("nickex1234");
    Courier courierWithoutFirstName = new Courier("nickex", "nickex1234");

    @Before
    public void setUp(){
        RestAssured.baseURI = "https://fceb4c18-0e7d-468c-89ba-6393b76ba729.serverhub.praktikum-services.ru";
    }

    @Test
    @DisplayName("Проверка статус кода")
    @Description("Проверка статус кода ручки POST /api/v1/courier")
    public void checkStatusCode(){
        CourierAPI create = new CourierAPI();
        create.createCourier(courierWithFullData).then().statusCode(201);
    }


    @Test
    @DisplayName("Проверка тела ответа")
    @Description("Проверка наличия значения true в ответе ручки POST /api/v1/courier")
    public void checkBodyResponse(){
        CourierAPI create = new CourierAPI();
        create.createCourier(courierWithFullData).then().assertThat().body("ok", equalTo(true));
    }


    @Test
    @DisplayName("Создание 2 одинаковых курьеров")
    @Description("Проверка невозможности создать 2 одинаковых курьеров в ручке POST /api/v1/courier")
    public void createDuplicateCourier(){
        CourierAPI create = new CourierAPI();
        create.createCourier(courierWithFullData);
        int statusCode = create.createCourier(courierWithFullData).statusCode();
        Assert.assertTrue(statusCode != 201);
    }
    @Step("Создание первого курьера")
    public void createFirstEqualCourier(){
        CourierAPI create = new CourierAPI();
        create.createCourier(courierWithFullData);
    }
    @Step("Создание второго курьера и получение статус кода")
    public int createSecondEqualCourier(){
        CourierAPI create = new CourierAPI();
        int statusCode = create.createCourier(courierWithFullData).statusCode();
        return statusCode;
    }
    @Step("Проверить, что второй курьер не создаётся")
    public void checkSecondCourierDoesNotCreate(int statusCode){
        Assert.assertTrue(statusCode != 201);
    }


    @Test
    @DisplayName("Создание курьера с уже существующем логином")
    @Description("Проверка невозможности создать 2 курьеров с одним и тем же логином в ручке POST /api/v1/courier")
    public void createCourierWithSameLogin(){
        CourierAPI create = new CourierAPI();
        create.createCourier(courierWithFullData);
        create.createCourier(courierWithFullData).then().statusCode(409);
    }
    @Step("Создание первого курьера")
    public void createFirstWithSameLoginCourier(){
        CourierAPI create = new CourierAPI();
        create.createCourier(courierWithFullData);
    }
    @Step("Создание второго курьера и проверка статус кода")
    public void createSecondWithSameLoginCourierAndCheckStatusCode(){
        CourierAPI create = new CourierAPI();
        create.createCourier(courierWithFullData).then().statusCode(409);
    }


    @Test
    @DisplayName("Создание курьера без логина")
    @Description("Создание курьера без логина в ручке POST /api/v1/courier")
    public void createCourierWithoutLogin(){
        CourierAPI create = new CourierAPI();
        create.createCourier(courierWithoutLogin)
                .then()
                .assertThat()
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @After
    public void deleteCourier(){
        CourierAPI loginAndDelete = new CourierAPI();
        int id = loginAndDelete.loginCourier(courierWithoutFirstName).path("id");
        loginAndDelete.deleteCourier(id);
    }
}
