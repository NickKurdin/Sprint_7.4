package ru.praktikum.scooter;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.hamcrest.Matchers.*;

@RunWith(Parameterized.class)
public class CreateOrderTest {
    private final String firstName;
    private final String lastName;
    private final String address;
    private final int metroStation;
    private final String phone;
    private final int rentTime;
    private final String deliveryDate;
    private final String comment;
    private final String[] color;

    public CreateOrderTest(String firstName, String lastName, String address,
                           int metroStation, String phone, int rentTime,
                           String deliveryDate, String comment, String[] color){
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.metroStation = metroStation;
        this.phone = phone;
        this.rentTime = rentTime;
        this.deliveryDate = deliveryDate;
        this.comment = comment;
        this.color = color;
    }

    @Parameterized.Parameters()
    public static Object[] getTestData() {
        return new Object[][]{
                {"Naruto", "Uchiha", "Konoha, 142 apt.", 4, "+7 800 355 35 35", 5, "2020-06-06", "Saske, come back to Konoha", new String[]{"BLACK"}},
                {"Naruto", "Uchiha", "Konoha, 142 apt.", 4, "+7 800 355 35 35", 5, "2020-06-06", "Saske, come back to Konoha", new String[]{"BLACK", "GREY"}}
        };
    }

    public int track;

    @Before
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru";
    }

    @Test
    @DisplayName("Создание заказа с цветом(ами)")
    @Description("Создать заказ с цветом через ручку POST /api/v1/orders")
    public void createOrderWithColor() {
        Order order = new Order(firstName, lastName, address,
                metroStation, phone, rentTime, deliveryDate, comment, color);
        OrderAPI create = new OrderAPI();
        Response response = create.createOrder(order);
        track = response.path("track");
        response.then().statusCode(201);
    }

    @Test
    @DisplayName("Создать заказ без цвета")
    @Description("Создать заказ без цвета через ручку POST /api/v1/orders")
    public void createOrderWithoutColor() {
        Order order = new Order(
                "Naruto", "Uchiha", "Konoha, 142 apt.",
                4, "+7 800 355 35 35", 5,
                "2020-06-06", "Saske, come back to Konoha");
        OrderAPI create = new OrderAPI();
        create.createOrder(order).then().statusCode(201);
    }

    @Test
    @DisplayName("Проверка тела ответа")
    @Description("Проверка наличия значения track в теле ответа ручки POST /api/v1/orders")
    public void checkBodyResponse() {
        Order order = new Order(
                "Naruto", "Uchiha", "Konoha, 142 apt.",
                4, "+7 800 355 35 35", 5,
                "2020-06-06", "Saske, come back to Konoha");
        OrderAPI create = new OrderAPI();
        create.createOrder(order).then().assertThat().body("track", notNullValue());
    }

    @Test
    @DisplayName("Проверка наличия списка заказов")
    @Description("Проверка наличия списка заказов через ручку GET /api/v1/orders")
    public void checkGettingListOrders(){
        Order order = new Order(
                "Naruto", "Uchiha", "Konoha, 142 apt.",
                4, "+7 800 355 35 35", 5,
                "2020-06-06", "Saske, come back to Konoha");
        OrderAPI create = new OrderAPI();
        create.createOrder(order);
        create.getListOrders().then().body("orders", hasSize(greaterThan(0)));
    }

    @After
    public void cancelOrder(){
        OrderAPI cancel = new OrderAPI();
        cancel.cancelOrder(track);
    }
}
