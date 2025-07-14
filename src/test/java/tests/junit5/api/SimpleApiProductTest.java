package tests.junit5.api;

import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import models.fakeapiproduct.Product;
import models.fakeapiproduct.RootProduct;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;

public class SimpleApiProductTest {

    @BeforeAll
    public static void setUp() {
        RestAssured.baseURI = "https://fakestoreapi.in/api";
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
    }

    @Test
    public void addProductTest() {
        Product product = new Product().builder()
                .title("Sell")
                .brand("Android")
                .model("OPPO")
                .color("White")
                .category("Ye")
                .discount("1").build();
        RootProduct requestBody = new RootProduct().builder()
                .product(product).build();


        given().body(requestBody)
                .contentType(ContentType.JSON)
                .body(requestBody)
                .post("/products")
                .then()
                .statusCode(200)
                .body("product.id", notNullValue());
    }
}
