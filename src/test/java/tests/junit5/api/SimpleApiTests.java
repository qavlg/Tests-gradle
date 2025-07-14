package tests.junit5.api;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import models.fakeapiuser.Address;
import models.fakeapiuser.Geolocation;
import models.fakeapiuser.Name;
import models.fakeapiuser.UserRoot;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class SimpleApiTests {

    @Test
    public void addProductTest(){
        Map<String, String> product = new HashMap<>();
        product.put("title","Android");
        product.put("brand","Android");
        product.put("model","OPPO");
        product.put("color","white");
        product.put("category","appliances");
        product.put("discount","2");

        given().body(product)
                .contentType(ContentType.JSON)
                .post("https://fakestoreapi.in/api/products")
                .then().log().all()
                .statusCode(200)
                .body("product.id", notNullValue());
    }


    @Test
    public void addNewUserTest(){
        Name name = new Name("Aleksei","Petrov");

        Geolocation geolocation = new Geolocation(7.543, 4.5334);

        Address address = new Address().builder()
                .city("Volg")
                .street("Local")
                .number("321")
                .zipcode("23442-2321")
                .geolocation(geolocation).build();

        UserRoot requestBody = new UserRoot().builder()
                .email("alekss@gmail.com")
                .username("Nik")
                .password("123Kjfjs")
                .name(name)
                .phone("7543434323")
                .address(address)
                .build();

        given().body(requestBody)
                .contentType("application/json")
                .post("https://fakestoreapi.in/api/users")
                .then().log().body()
                .statusCode(200)
                .body("user.id", notNullValue());
    }

    @Test
    public UserRoot getTestUser(){
        Name name = new Name("Aleksei","Petrov");

        Geolocation geolocation = new Geolocation(7.543, 4.5334);

        Address address = new Address().builder()
                .city("Volg")
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
    public void updateUserTest(){
        UserRoot user = getTestUser();
        String oldPassword = user.getPassword();

        user.setPassword("newpass231");
        given().body(user)
                .contentType("application/json")
                .put("https://fakestoreapi.in/api/users/" + user.getId())
                .then().log().all()
                .body("password", not(equalTo(oldPassword)));

    }

    @Test
    public void deleteUserTest(){
        UserRoot user = getTestUser();
        given().delete("https://fakestoreapi.in/api/users/" + user.getId())
                .then().log().all()
                .statusCode(200);

    }




    @Test
    public void getAllUsersTest() {
        given().get("https://fakestoreapi.com/users/")
                .then()
                .log().all()
                .statusCode(200);


    }

    @Test
    public void getSingleUserTest() {
        int userId = 4;
        given().pathParam("userId", userId)
                .get("https://fakestoreapi.com/users/{userId}")
                .then().log().all()
                .statusCode(200)
                .body("id", equalTo(userId))
                .body("address.zipcode", matchesPattern("\\d{5}-\\d{4}"));

    }

    @ParameterizedTest
    @ValueSource(ints = {3, 5, 10})
    public void getListProductsTest(int limitSize) {

        given().queryParam("limit", limitSize)
                .get("https://fakestoreapi.com/products")
                .then().log().all()
                .statusCode(200)
                .body("",hasSize(limitSize));
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 25})
    public void getListProductsErrorParamTest(int limitSize) {

        given().queryParam("limit", limitSize)
                .get("https://fakestoreapi.com/products")
                .then().log().all()
                .statusCode(200)
                .body("", not(equalTo(limitSize)));
    }

    @Test
    public void getAllProductsSortByDescTest(){
        String sort = "desc";
        Response sortedResponse = given().queryParam("sort", sort)
        .get("https://fakestoreapi.com/products")
                .then().log().all()
                .extract().response();

        Response notSortedResponse = given()
                .get("https://fakestoreapi.com/products")
                .then().log().all()
                .extract().response();

        List <Integer> sortedResponseIds = sortedResponse.jsonPath().getList("id");
        List <Integer> notSortedResponseIds = notSortedResponse.jsonPath().getList("id");

        List <Integer> sortedByCode = notSortedResponseIds
                .stream()
                .sorted(Comparator.reverseOrder())
                .toList();

        Assertions.assertNotEquals(sortedResponseIds,notSortedResponseIds,"Error");
        Assertions.assertEquals(sortedByCode,sortedResponseIds,"Error");

    }




}
