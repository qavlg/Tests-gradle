package tests.swaggertests;

import assertions.AssertableResponse;
import assertions.Condition;
import assertions.Conditions;
import assertions.GenericAssertableResponse;
import io.restassured.RestAssured;
import io.restassured.common.mapper.TypeRef;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import models.UserInfoResponse;
import models.swagger.FullUser;
import models.swagger.JwtAuthData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static assertions.Conditions.hasMessage;
import static assertions.Conditions.hasStatusCode;
import static io.restassured.RestAssured.given;

public class UserTests {

    private static Random random;

    @BeforeAll
    public static void setUp() {
        RestAssured.baseURI = "http://85.192.34.140:8080/";
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
        random = new Random();
    }

    @Test
    public void positiveRegisterTest() {
        int randomNumber = Math.abs(random.nextInt());

        FullUser user = FullUser.builder()
                .login("NewUser" + randomNumber)
                .pass("pass!=" + randomNumber)
                .build();

        UserInfoResponse infoResponse = given().contentType(ContentType.JSON)
                .body(user)
                .post("api/signup")
                .then()
                .statusCode(201)
                .extract().jsonPath().getObject("info", UserInfoResponse.class);

        Assertions.assertEquals("User created", infoResponse.getMessage());
    }

    @Test
    public void negativeRegisterLoginExistsTest() {
        int randomNumber = Math.abs(random.nextInt());

        FullUser user = FullUser.builder()
                .login("NewUser" + randomNumber)
                .pass("pass!=")
                .build();

        UserInfoResponse infoResponse = given().contentType(ContentType.JSON)
                .body(user)
                .post("api/signup")
                .then()
                .statusCode(201)
                .extract().jsonPath().getObject("info", UserInfoResponse.class);

        Assertions.assertEquals("User created", infoResponse.getMessage());


        UserInfoResponse errorInfoResponse = given().contentType(ContentType.JSON)
                .body(user)
                .post("api/signup")
                .then()
                .statusCode(400)
                .extract().jsonPath().getObject("info", UserInfoResponse.class);

        Assertions.assertEquals("Login already exist", errorInfoResponse.getMessage());
    }

    @Test
    public void negativeRegisterNoPasswordTest() {
        int randomNumber = Math.abs(random.nextInt());

        FullUser user = FullUser.builder()
                .login("NewUser" + randomNumber)
                .build();

        UserInfoResponse infoResponse = given().contentType(ContentType.JSON)
                .body(user)
                .post("api/signup")
                .then()
                .statusCode(400)
                .extract().jsonPath().getObject("info", UserInfoResponse.class);

        new AssertableResponse(given().contentType(ContentType.JSON)
                .body(user)
                .post("api/signup")
                .then())
                .should(hasMessage("Missing login or password"))
                .should(hasStatusCode(400));

        new GenericAssertableResponse<UserInfoResponse>(given().contentType(ContentType.JSON)
                .body(user)
                .post("api/signup")
                .then(), new TypeRef<UserInfoResponse>() {})
                .should(hasMessage("Missing login or password"))
                .should(hasStatusCode(400));

        Assertions.assertEquals("Missing login or password", infoResponse.getMessage());
    }

    @Test
    public void positiveAdminAuthTest() {
        JwtAuthData authData = new JwtAuthData("admin", "admin");

        String token = given().contentType(ContentType.JSON)
                .body(authData)
                .post("api/login")
                .then()
                .statusCode(200)
                .extract().jsonPath().getString("token");

        Assertions.assertNotNull(token);
    }

    @Test
    public void positiveNewUserAuthTest() {
        int randomNumber = Math.abs(random.nextInt());

        FullUser user = FullUser.builder()
                .login("NewUser" + randomNumber)
                .pass("pass!=")
                .build();

        UserInfoResponse infoResponse = given().contentType(ContentType.JSON)
                .body(user)
                .post("api/signup")
                .then()
                .statusCode(201)
                .extract().jsonPath().getObject("info", UserInfoResponse.class);

        Assertions.assertEquals("User created", infoResponse.getMessage());


        JwtAuthData authData = new JwtAuthData(user.getLogin(), user.getPass());

        String token = given().contentType(ContentType.JSON)
                .body(authData)
                .post("api/login")
                .then()
                .statusCode(200)
                .extract().jsonPath().getString("token");

        Assertions.assertNotNull(token);
    }

    @Test
    public void negativeAuthTest() {
        JwtAuthData authData = new JwtAuthData("1233sfsff", "21434dw");

        given().contentType(ContentType.JSON)
                .body(authData)
                .post("api/login")
                .then()
                .statusCode(401);
    }

    @Test
    public void positiveGetUserInfo() {
        int randomNumber = Math.abs(random.nextInt());

        FullUser user = FullUser.builder()
                .login("NewUser" + randomNumber)
                .pass("pass!=" + randomNumber)
                .build();

        UserInfoResponse infoResponse = given().contentType(ContentType.JSON)
                .body(user)
                .post("api/signup")
                .then()
                .statusCode(201)
                .extract().jsonPath().getObject("info", UserInfoResponse.class);

        Assertions.assertEquals("User created", infoResponse.getMessage());


        JwtAuthData authData = new JwtAuthData(user.getLogin(), user.getPass());

        String token = given().contentType(ContentType.JSON)
                .body(authData)
                .post("api/login")
                .then()
                .statusCode(200)
                .extract().jsonPath().getString("token");

        Assertions.assertNotNull(token);

        given().auth().oauth2(token)
                .get("api/user")
                .then()
                .statusCode(200);
    }

    @Test
    public void negativeGetUserInfoInvalidJwtTest() {
        given().auth().oauth2("some values")
                .get("api/user")
                .then()
                .statusCode(401);
    }

    @Test
    public void negativeGetUserInfoWithoutJwtTest() {
        given().get("api/user")
                .then()
                .statusCode(401);
    }

    @Test
    public void positiveChangeUserPasswordTest() {
        int randomNumber = Math.abs(random.nextInt());

        FullUser user = FullUser.builder()
                .login("NewUser" + randomNumber)
                .pass("pass!=")
                .build();

        UserInfoResponse infoResponse = given().contentType(ContentType.JSON)
                .body(user)
                .post("api/signup")
                .then()
                .statusCode(201)
                .extract().jsonPath().getObject("info", UserInfoResponse.class);

        Assertions.assertEquals("User created", infoResponse.getMessage());


        JwtAuthData authData = new JwtAuthData(user.getLogin(), user.getPass());

        String token = given().contentType(ContentType.JSON)
                .body(authData)
                .post("api/login")
                .then()
                .statusCode(200)
                .extract().jsonPath().getString("token");

        Assertions.assertNotNull(token);

        JwtAuthData changePass = JwtAuthData.builder()
                .password("newPass2")
                .build();

        UserInfoResponse updatedPass = given().auth().oauth2(token)
                .contentType(ContentType.JSON)
                .body(changePass)
                .put("api/user")
                .then()
                .extract().jsonPath().getObject("info", UserInfoResponse.class);

        Assertions.assertEquals("User password successfully changed", updatedPass.getMessage());

        JwtAuthData afterUpdatedAuthData = new JwtAuthData(user.getLogin(), changePass.getPassword());

        String afterUpdatedToken = given().contentType(ContentType.JSON)
                .body(afterUpdatedAuthData)
                .post("api/login")
                .then()
                .statusCode(200)
                .extract().jsonPath().getString("token");

        Assertions.assertNotNull(afterUpdatedToken);

         FullUser updatedUser = given().auth().oauth2(afterUpdatedToken)
                .get("api/user")
                .then()
                .statusCode(200)
                 .extract().as(FullUser.class);

         Assertions.assertNotEquals(user.getPass(), updatedUser.getPass());
    }

}
