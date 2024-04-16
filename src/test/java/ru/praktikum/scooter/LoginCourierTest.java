package ru.praktikum.scooter;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.hamcrest.Matchers.notNullValue;

public class LoginCourierTest {
    Courier courierWithIncorrectPassword = new Courier("nickex", "nickex123", "Никита");
    Courier nonExistentCourier = new Courier("nickexq", "nickex1210");
    Courier courierWithoutLogin = new Courier("nickex123");
    public Courier courier;

    @Before
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru";
    }

    @Test
    @DisplayName("Проверка статус кода")
    @Description("Проверка статус кода ручки POST /api/v1/courier/login")
    public void checkStatusCode() {
        CourierAPI createAndLogin = new CourierAPI();
        createCourierForCheckStatusCode(createAndLogin);
        loginCourierForCheckStatusCode(createAndLogin);
    }
    @Step("Создать курьера")
    public void createCourierForCheckStatusCode(CourierAPI createAndLogin) {
        courier = new Courier("nick_assholeeee", "nick");
        createAndLogin.createCourier(courier);
    }
    @Step("Авторизовать курьера и проверить статус кода")
    public void loginCourierForCheckStatusCode(CourierAPI createAndLogin) {
        courier = new Courier("nick_assholeeee", "nick");
        createAndLogin.loginCourier(courier).then().statusCode(200);
    }

    @Test
    @DisplayName("Ввод некоректного пароля")
    @Description("Ввод некоректного пароля через ручку POST /api/v1/courier/login")
    public void enterIncorrectPassword() {
        CourierAPI createAndLogin = new CourierAPI();
        createCourierForEnterIncorrectPassword(createAndLogin);
        loginCourierForEnterIncorrectPassword(createAndLogin);
    }
    @Step("Создать курьера")
    public void createCourierForEnterIncorrectPassword(CourierAPI createAndLogin) {
        courier = new Courier("nick_assholeeeeee", "nick");
        createAndLogin.createCourier(courier);
    }
    @Step("Авторизовать курьера с неверным паролем и проверить статус кода")
    public void loginCourierForEnterIncorrectPassword(CourierAPI createAndLogin) {
        createAndLogin.loginCourier(courierWithIncorrectPassword).then().statusCode(200);
    }

    @Test
    @DisplayName("Авторизация курьера без ввода логина")
    @Description("Авторизация курьера без ввода логина через ручку POST /api/v1/courier/login")
    public void loginCourierWithoutLogin() {
        CourierAPI createAndLogin = new CourierAPI();
        createCourierForLoginCourierWithoutLogin(createAndLogin);
        loginCourierForLoginCourierWithoutLogin(createAndLogin);
    }
    @Step("Создать курьера")
    public void createCourierForLoginCourierWithoutLogin(CourierAPI createAndLogin) {
        courier = new Courier("nick_assholeeeeeeee", "nick");
        createAndLogin.createCourier(courier);
    }
    @Step("Авторизовать курьера без ввода логина и проверить статус кода")
    public void loginCourierForLoginCourierWithoutLogin(CourierAPI createAndLogin) {
        createAndLogin.createCourier(courierWithoutLogin).then().statusCode(400);
    }

    @Test
    @DisplayName("Авторизация несуществующего курьера")
    @Description("Авторизация несуществующего курьера через ручку POST /api/v1/courier/login")
    public void loginNonexistentCourier() {
        CourierAPI createAndLogin = new CourierAPI();
        courier = new Courier("nick_assholeeeeeeeeee", "nick");
        createAndLogin.createCourier(courier);
        createAndLogin.loginCourier(nonExistentCourier).then().statusCode(404);
    }

    @Test
    @DisplayName("Проверка тела ответа")
    @Description("Проверка наличия значения поля id в ответе ручки POST /api/v1/courier/login")
    public void checkBodyResponse() {
        CourierAPI createAndLogin = new CourierAPI();
        createCourierForCheckBodyResponse(createAndLogin);
        loginCourierForCheckBodyResponse(createAndLogin);
    }
    @Step("Создать курьра")
    public void createCourierForCheckBodyResponse(CourierAPI createAndLogin) {
        courier = new Courier("nick_assholeeeeeeeeeeeeeee", "nick");
        createAndLogin.createCourier(courier);
    }
    @Step("Авторизовать курьера и проверить наличие id в ответе ручки")
    public void loginCourierForCheckBodyResponse(CourierAPI createAndLogin) {
        courier = new Courier("nick_assholeeeeeeeeeeeeeee", "nick");
        createAndLogin.loginCourier(courier).then().assertThat().body("id", notNullValue());
    }

    @After
    public void deleteCourier() {
        CourierAPI delete = new CourierAPI();
        Response response = delete.loginCourier(courier);
        int statusCode = response.statusCode();
        if (statusCode == 200) {
            int id = response.path("id");
            delete.deleteCourier(id);
        }
    }
}
