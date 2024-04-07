package ru.praktikum.scooter;
import io.restassured.RestAssured;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;


import io.restassured.response.Response;

public class LoginCourierTest {
    Courier courierWithoutFirstName = new Courier("nickex", "nickex1234");
    Courier courierWithIncorrectPassword = new Courier("nickex", "nickex123", "Никита");
    Courier nonExistentCourier = new Courier("kickex", "kickex123");


    @Before
    public void setUpForCheckStatusCode(){
        RestAssured.baseURI = "https://7e4cc509-0e10-49a8-a069-5525b46ad4a3.serverhub.praktikum-services.ru";
    }
    @Test
    public void checkStatusCode(){
        given()
                .header("Content-type","application/json")
                .body(courierWithoutFirstName)
                .post("/api/v1/courier");
        given()
                .header("Content-type","application-json")
                .body(courierWithoutFirstName)
                .when()
                .post("/api/v1/courier/login")
                .then().statusCode(200);
    }
    @After
    public void deleteCourierCreatedInCheckStatusCode(){
        Response response = given()
                .header("Content-type","application-json")
                .body(courierWithoutFirstName)
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
    public void setUpForIncorrectPassword(){
        RestAssured.baseURI = "https://7e4cc509-0e10-49a8-a069-5525b46ad4a3.serverhub.praktikum-services.ru";
    }
    @Test
    public void enterIncorrectPassword(){
        given()
                .header("Content-type","application/json")
                .body(courierWithoutFirstName)
                .post("/api/v1/courier");
        given()
                .header("Content-type","application-json")
                .body(courierWithIncorrectPassword)
                .when()
                .post("/api/v1/courier/login")
                .then().statusCode(404);
    }
    @After
    public void deleteCourierCreatedInEnterIncorrectPassword(){
        Response response = given()
                .header("Content-type","application-json")
                .body(courierWithoutFirstName)
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
    public void setUpForLoginCourierWithoutLogin(){
        RestAssured.baseURI = "https://7e4cc509-0e10-49a8-a069-5525b46ad4a3.serverhub.praktikum-services.ru";
    }
    @Test
    public void loginCourierWithoutLogin(){
        given()
                .header("Content-type","application/json")
                .body(courierWithoutFirstName)
                .post("/api/v1/courier");
        given()
                .header("Content-type","application-json")
                .body(courierWithIncorrectPassword)
                .when()
                .post("/api/v1/courier/login")
                .then().statusCode(400);
    }
    @After
    public void deleteCourierCreatedInLoginCourierWithoutLogin(){
        Response response = given()
                .header("Content-type","application-json")
                .body(courierWithoutFirstName)
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
    public void setUpForLoginNonexistentCourier(){
        RestAssured.baseURI = "https://7e4cc509-0e10-49a8-a069-5525b46ad4a3.serverhub.praktikum-services.ru";
    }
    @Test
    public void loginNonexistentCourier(){
        given()
                .header("Content-type","application-json")
                .body(nonExistentCourier)
                .when()
                .post("/api/v1/courier/login")
                .then().statusCode(404);;
    }

    @Before
    public void setUpForCheckBodyResponse(){
        RestAssured.baseURI = "https://7e4cc509-0e10-49a8-a069-5525b46ad4a3.serverhub.praktikum-services.ru";
    }
    @Test
    public void checkBodyResponse(){
        given()
                .header("Content-type","application/json")
                .body(courierWithoutFirstName)
                .when()
                .post("/api/v1/courier")
                .then().assertThat().body("id", notNullValue());
    }

    @After
    public void deleteCourierCreatedInCheckBodyResponse(){
        Response response = given()
                .header("Content-type","application-json")
                .body(courierWithoutFirstName)
                .when()
                .post("/api/v1/courier/login");
        int id = response.path("id");
        given()
                .header("Content-type","application-json")
                .body(String.format("{\"id\":%d}", id))
                .when()
                .delete("/api/v1/courier/{id}");//test comment
    }

}
