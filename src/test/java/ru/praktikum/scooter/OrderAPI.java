package ru.praktikum.scooter;

import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class OrderAPI {
    public Response createOrder(Order order){
        return given()
                .header("Content-type","application/json")
                .contentType("application/json")
                .body(order)
                .post("/api/v1/orders");
    }
    public void cancelOrder(int track){
        given()
                .header("Content-type","application/json")
                .contentType("application/json")
                .body(String.format("{track: %d}", track))
                .put("/api/v1/orders/cancel");
    }
    public Response getListOrders(){
        return given()
                .queryParam("page", 1)
                .header("Content-type","application/json")
                .contentType("application/json")
                .get("/api/v1/orders");
    }
}
