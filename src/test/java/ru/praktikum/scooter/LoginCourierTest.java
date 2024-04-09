package ru.praktikum.scooter;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.hamcrest.Matchers.notNullValue;

public class LoginCourierTest {
    Courier courierWithoutFirstName = new Courier("nickex", "nickex1234");
    Courier courierWithIncorrectPassword = new Courier("nickex", "nickex123", "Никита");
    Courier nonExistentCourier = new Courier("nickex", "nickex123");
    Courier courierWithoutLogin = new Courier("nickex123");


    @Before
    public void setUp(){
        RestAssured.baseURI = "https://a2f15697-c406-4150-9786-30e92eb696a2.serverhub.praktikum-services.ru";
    }
    @Test
    @DisplayName("Проверка статус кода")
    @Description("Проверка статус кода ручки POST /api/v1/courier/login")
    public void checkStatusCode(){
        CourierAPI createAndLogin = new CourierAPI();
        createAndLogin.createCourier(courierWithoutFirstName);
        createAndLogin.loginCourier(courierWithoutFirstName).then().statusCode(200);
    }
    @Step("Создать курьера")
    public void createCourierForCheckStatusCode(CourierAPI createAndLogin){
        createAndLogin.createCourier(courierWithoutFirstName);
    }
    @Step("Авторизовать курьера и проверить статус кода")
    public void loginCourierForCheckStatusCode(CourierAPI createAndLogin){
        createAndLogin.loginCourier(courierWithoutFirstName).then().statusCode(200);
    }

    @Test
    @DisplayName("Ввод некоректного пароля")
    @Description("Ввод некоректного пароля через ручку POST /api/v1/courier/login")
    public void enterIncorrectPassword(){
        CourierAPI createAndLogin = new CourierAPI();
        createAndLogin.createCourier(courierWithoutFirstName);
        createAndLogin.loginCourier(courierWithIncorrectPassword).then().statusCode(200);
    }
    @Step("Создать курьера")
    public void createCourierForEnterIncorrectPassword(CourierAPI createAndLogin){
        createAndLogin.createCourier(courierWithoutFirstName);
    }
    @Step("Авторизовать курьера с неверным паролем и проверить статус кода")
    public void loginCourierForEnterIncorrectPassword(CourierAPI createAndLogin){
        createAndLogin.loginCourier(courierWithoutFirstName).then().statusCode(200);
    }

    @Test
    @DisplayName("Авторизация курьера без ввода логина")
    @Description("Авторизация курьера без ввода логина через ручку POST /api/v1/courier/login")
    public void loginCourierWithoutLogin(){
        CourierAPI createAndLogin = new CourierAPI();
        createAndLogin.createCourier(courierWithoutFirstName);
        createAndLogin.createCourier(courierWithoutLogin).then().statusCode(400);
    }
    @Step("Создать курьера")
    public void createCourierForLoginCourierWithoutLogin(CourierAPI createAndLogin){
        createAndLogin.createCourier(courierWithoutFirstName);
    }
    @Step("Авторизовать курьера без ввода логина и проверить статус кода")
    public void loginCourierForLoginCourierWithoutLogin(CourierAPI createAndLogin){
        createAndLogin.createCourier(courierWithoutLogin).then().statusCode(400);
    }

    @Test
    @DisplayName("Авторизация несуществующего курьера")
    @Description("Авторизация несуществующего курьера через ручку POST /api/v1/courier/login")
    public void loginNonexistentCourier(){
        CourierAPI createAndLogin = new CourierAPI();
        createAndLogin.createCourier(nonExistentCourier).then().statusCode(404);;
    }

    @Test
    @DisplayName("Проверка тела ответа")
    @Description("Проверка наличия значения поля id в ответе ручки POST /api/v1/courier/login")
    public void checkBodyResponse(){
        CourierAPI createAndLogin = new CourierAPI();
        createAndLogin.createCourier(courierWithoutFirstName);
        createAndLogin.loginCourier(courierWithoutFirstName).then().assertThat().body("id", notNullValue());
    }
    @Step("Создать курьра")
    public void createCourierForCheckBodyResponse(CourierAPI createAndLogin){
        createAndLogin.createCourier(courierWithoutFirstName);
    }
    @Step("Авторизовать курьера и проверить наличие id в ответе ручки")
    public void loginCourierForCheckBodyResponse(CourierAPI createAndLogin){
        createAndLogin.loginCourier(courierWithoutFirstName).then().assertThat().body("id", notNullValue());
    }

    @After
    public void deleteCourier(){
        CourierAPI delete = new CourierAPI();
        int id = delete.loginCourier(courierWithoutFirstName).path("id");
        delete.deleteCourier(id);
    }

}
