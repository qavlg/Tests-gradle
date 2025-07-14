package tests.junit5.api;

import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import models.fakeapiuser.Address;
import models.fakeapiuser.Geolocation;
import models.fakeapiuser.Name;
import models.fakeapiuser.UserRoot;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static io.restassured.RestAssured.given;

public class SimpleApiRefactoredTest {

    @BeforeAll
    public static void setUp() {
        RestAssured.baseURI = "https://fakestoreapi.in/api";
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
    }

    @Test
    public void getAllUsersTest() {
        given().get("/users")
                .then()
                .statusCode(200);
    }

    @Test
    public void getSingleUserTest() {
        int userId = 2;
        UserRoot response = given().pathParam("userId", userId)
                .get("/users/{userId}")
                .then()
                .statusCode(200)
                .extract().as(UserRoot.class);

        Assertions.assertEquals(userId, response.getId());
        Assertions.assertTrue(response.getAddress().getZipcode().matches("\\d{5}-\\d{4}"));
    }

    @Test
    public void addNewUserTest() {
        UserRoot user = getTestUser();

        Integer userId = given().body(user)
                .contentType("application/json")
                .post("/users")
                .then()
                .statusCode(200)
                .extract().jsonPath().getInt("user.id");

        Assertions.assertNotNull(userId);
    }

    @Test
    public UserRoot getTestUser() {
        Random random = new Random();
        String[] cities = {"Москва", "Санкт-Петербург", "Новосибирск"};
        int index = random.nextInt(cities.length);

        Name name = new Name("Aleksei", "Petrov");

        Geolocation geolocation = new Geolocation(7.543, 4.5334);

        Address address = new Address().builder()
                .city(cities[index])
                .street("Local")
                .number("321")
                .zipcode("2344")
                .geolocation(geolocation).build();

        return new UserRoot().builder()
                .email("alekss@gmail.com")
                .username("Nik")
                .password("123Kjfjs")
                .name(name)
                .phone("7543434323")
                .address(address)
                .build();
    }

    @Test
    public void updateUserTest() {
        UserRoot user = getTestUser();
        String oldPassword = user.getPassword();

        user.setPassword("newpass231");

        UserRoot updatedUser = given().body(user)
                .contentType("application/json")
                .pathParam("userId", user.getId())
                .put("/users/{userId}")
                .then().log().all()
                .extract().as(UserRoot.class);

        Assertions.assertNotEquals(updatedUser.getPassword(), oldPassword);

    }

}
