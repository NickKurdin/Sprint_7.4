package ru.praktikum.scooter;

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
        RestAssured.baseURI = "https://672cd097-2fd3-43e0-bc7f-f46a8bbe0220.serverhub.praktikum-services.ru";
    }

    @Test
    public void checkStatusCode(){
        CourierAPI create = new CourierAPI();
        create.createCourier(courierWithFullData).then().statusCode(201);
    }


    @Test
    public void checkBodyResponse(){
        CourierAPI create = new CourierAPI();
        create.createCourier(courierWithFullData).then().assertThat().body("ok", equalTo(true));
    }


    @Test
    public void createDuplicateCourier(){
        CourierAPI create = new CourierAPI();
        create.createCourier(courierWithFullData);
        int statusCode = create.createCourier(courierWithFullData).statusCode();
        Assert.assertTrue(statusCode != 201);
    }

    @Test
    public void createCourierWithSameLogin(){
        CourierAPI create = new CourierAPI();
        create.createCourier(courierWithFullData);
        create.createCourier(courierWithFullData).then().statusCode(409);
    }

    @Test
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
