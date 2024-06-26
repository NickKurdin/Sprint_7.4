package ru.praktikum.scooter;

import io.restassured.response.Response;

import static io.restassured.RestAssured.given;
import io.restassured.RestAssured;

public class CourierAPI {
    public Response createCourier(Courier courier){
        return given()
                .header("Content-type","application/json")
                .contentType("application/json")
                .body(courier)
                .post("/api/v1/courier");
    }
    public Response loginCourier(Courier courier){
        return given()
                .header("Content-type","application/json")
                .contentType("application/json")
                .body(courier)
                .post("/api/v1/courier/login");
    }
    public void deleteCourier(int id){
        given()
                .pathParam("id", id)
                .header("Content-type","application-json")
                .contentType("application/json")
                .body(String.format("{\"id\":%d}", id))
                .when()
                .delete("/api/v1/courier/{id}");
    }
}
