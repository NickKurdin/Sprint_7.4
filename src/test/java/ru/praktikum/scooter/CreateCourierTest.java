package ru.praktikum.scooter;

import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import static org.hamcrest.CoreMatchers.equalTo;

public class CreateCourierTest {
    public Courier courier;

    @Before
    public void setUp(){
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru";
    }

    @Test
    @DisplayName("Проверка статус кода")
    @Description("Проверка статус кода ручки POST /api/v1/courier")
    public void checkStatusCode(){
        courier = new Courier("nick_assholeee", "nick", "Никита");
        CourierAPI create = new CourierAPI();
        create.createCourier(courier).then().statusCode(201);
    }


    @Test
    @DisplayName("Проверка тела ответа")
    @Description("Проверка наличия значения true в ответе ручки POST /api/v1/courier")
    public void checkBodyResponse(){
        courier = new Courier("nick_assholeeee", "nick", "Никита");
        CourierAPI create = new CourierAPI();
        create.createCourier(courier).then().assertThat().body("ok", equalTo(true));
    }


    @Test
    @DisplayName("Создание 2 одинаковых курьеров")
    @Description("Проверка невозможности создать 2 одинаковых курьеров в ручке POST /api/v1/courier")
    public void createDuplicateCourier(){
        CourierAPI create = new CourierAPI();
        createFirstEqualCourier(create);
        int statusCode = createSecondEqualCourier(create);
        checkSecondCourierDoesNotCreate(statusCode);
    }
    @Step("Создание первого курьера")
    public void createFirstEqualCourier(CourierAPI create){
        courier = new Courier("nick_assholeeeee", "nick", "Никита");
        create.createCourier(courier);
    }
    @Step("Создание второго курьера и получение статус кода")
    public int createSecondEqualCourier(CourierAPI create){
        courier = new Courier("nick_assholeeeee", "nick", "Никита");
        int statusCode = create.createCourier(courier).statusCode();
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
        createFirstWithSameLoginCourier(create);
        createSecondWithSameLoginCourierAndCheckStatusCode(create);
    }
    @Step("Создание первого курьера")
    public void createFirstWithSameLoginCourier(CourierAPI create){
        courier = new Courier("nick_assholeeeeeee", "nick", "Никита");
        create.createCourier(courier);
    }
    @Step("Создание второго курьера и проверка статус кода")
    public void createSecondWithSameLoginCourierAndCheckStatusCode(CourierAPI create){
        courier = new Courier("nick_assholeeeeeee", "nick", "Никита");
        create.createCourier(courier).then().statusCode(409);
    }


    @Test
    @DisplayName("Создание курьера без логина")
    @Description("Создание курьера без логина в ручке POST /api/v1/courier")
    public void createCourierWithoutLogin(){
        CourierAPI create = new CourierAPI();
        courier = new Courier("nick");
        create.createCourier(courier)
                .then()
                .assertThat()
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @After
    public void deleteCourier(){
        CourierAPI delete = new CourierAPI();
        Response response = delete.loginCourier(courier);
        int statusCode = response.statusCode();
        if(statusCode == 200){
            int id = response.path("id");
            delete.deleteCourier(id);
        }
    }
}
