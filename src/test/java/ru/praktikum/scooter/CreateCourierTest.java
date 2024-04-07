package ru.praktikum.scooter;

import io.restassured.RestAssured;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import io.restassured.response.Response;

import static org.hamcrest.CoreMatchers.equalTo;

public class CreateCourierTest {
    Courier courierWithFullData = new Courier("nickex", "nickex1234", "Никита");
    Courier courierWithoutLogin = new Courier("nickex1234");

    @Before
    public void setUpForCheckStatusCode(){
        RestAssured.baseURI = "https://7e4cc509-0e10-49a8-a069-5525b46ad4a3.serverhub.praktikum-services.ru";
    }

    @Test
    public void checkStatusCode(){
        given()
                .header("Content-type","application/json")
                .body(courierWithFullData)
                .post("/api/v1/courier")
                .then().statusCode(201);
    }

    @After
    public void deleteCourierCreatedInCheckStatusCode(){
        Response response = given()
                .header("Content-type","application-json")
                .body(courierWithFullData)
                .when()
                .post("/api/v1/courier/login");
        int id = response.path("id");
        given()
                .header("Content-type","application-json")
                .body(String.format("{\"id\":%d}", id))
                .when()
                .delete("/api/v1/courier/{id}");
    }
    @Before
    public void setUpForCheckBodyResponse(){
        RestAssured.baseURI = "https://7e4cc509-0e10-49a8-a069-5525b46ad4a3.serverhub.praktikum-services.ru";
    }
    @Test
    public void checkBodyResponse(){
        given()
                .header("Content-type","application/json")
                .body(courierWithFullData)
                .when()
                .post("/api/v1/courier")
                .then().assertThat().body("ok", equalTo(true));
    }

    @After
    public void deleteCourierCreatedInCheckBodyResponse(){
        Response response = given()
                .header("Content-type","application-json")
                .body(courierWithFullData)
                .when()
                .post("/api/v1/courier/login");
        int id = response.path("id");
        given()
                .header("Content-type","application-json")
                .body(String.format("{\"id\":%d}", id))
                .when()
                .delete("/api/v1/courier/{id}");
    }

    @Before
    public void setUpForCreateDuplicateCourier(){
        RestAssured.baseURI = "https://7e4cc509-0e10-49a8-a069-5525b46ad4a3.serverhub.praktikum-services.ru";
    }
    @Test
    public void createDuplicateCourier(){
        given()
                .header("Content-type","application/json")
                .body(courierWithFullData)
                .when()
                .post("/api/v1/courier");
        int statusCode = given()
                .header("Content-type","application/json")
                .body(courierWithFullData)
                .when()
                .post("/api/v1/courier")
                .statusCode();
        Assert.assertTrue(statusCode != 201);
    }
    @After
    public void deleteCourierCreatedInCreateDuplicateCourier(){
        Response response = given()
                .header("Content-type","application-json")
                .body(courierWithFullData)
                .when()
                .post("/api/v1/courier/login");
        int id = response.path("id");
        given()
                .header("Content-type","application-json")
                .body(String.format("{\"id\":%d}", id))
                .when()
                .delete("/api/v1/courier/{id}");
    }

    @Before
    public void setUpForCreateCourierWithSameLogin(){
        RestAssured.baseURI = "https://7e4cc509-0e10-49a8-a069-5525b46ad4a3.serverhub.praktikum-services.ru";
    }
    @Test
    public void createCourierWithSameLogin(){
        given()
                .header("Content-type","application/json")
                .body(courierWithFullData)
                .when()
                .post("/api/v1/courier");
        given()
                .header("Content-type","application/json")
                .body(courierWithFullData)
                .when()
                .post("/api/v1/courier")
                .then().statusCode(409);
    }
    @After
    public void deleteCourierCreatedInCreateCourierWithSameLogin(){
        Response response = given()
                .header("Content-type","application-json")
                .body(courierWithFullData)
                .when()
                .post("/api/v1/courier/login");
        int id = response.path("id");
        given()
                .header("Content-type","application-json")
                .body(String.format("{\"id\":%d}", id))
                .when()
                .delete("/api/v1/courier/{id}");
    }

    @Before
    public void setUpForCreateCourierWithoutLogin(){
        RestAssured.baseURI = "https://7e4cc509-0e10-49a8-a069-5525b46ad4a3.serverhub.praktikum-services.ru";
    }
    @Test
    public void createCourierWithoutLogin(){
        given()
                .header("Content-type","application/json")
                .body(courierWithoutLogin)
                .when()
                .post("/api/v1/courier")
                .then().assertThat().body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }
    @After
    public void deleteCourierCreatedInCreateCourierWithoutLogin(){
        Response response = given()
                .header("Content-type","application-json")
                .body(courierWithFullData)
                .when()
                .post("/api/v1/courier/login");
        int id = response.path("id");
        given()
                .header("Content-type","application-json")
                .body(String.format("{\"id\":%d}", id))
                .when()
                .delete("/api/v1/courier/{id}");
    }
}
