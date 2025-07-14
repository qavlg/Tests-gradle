package tests.swaggertests;

import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.response.Response;
import models.UserInfoResponse;
import models.swagger.FullUser;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import services.UserService;

import static assertions.Conditions.hasMessage;
import static assertions.Conditions.hasStatusCode;
import static utils.RandomTestData.*;

public class UserNewTest {
    private static UserService userService;

    @BeforeAll
    public static void setUp() {
        RestAssured.baseURI = "http://85.192.34.140:8080/";
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
        userService = new UserService();
    }

    @Test
    public void positiveRegisterTest() {
        FullUser user = getRandomUser();
        userService.register(user)
                .should(hasStatusCode(201))
                .should(hasMessage("User created"));
    }

    @Test
    public void positiveRegisterWithGamesTest() {
        FullUser user = getRandomUserWithGames();
        Response response = userService.register(user)
                //.should(hasStatusCode(201))
                //.should(hasMessage("User created"))
                .asResponse();
        UserInfoResponse info = response.jsonPath().getObject("info", UserInfoResponse.class);

        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(info.getMessage()).as("Сообщение об ошибке не верно")
                .isEqualTo("User created");
        softAssertions.assertThat(response.statusCode()).as("Статус код не 201")
                .isEqualTo(201);
        softAssertions.assertAll();

    }

    @Test
    public void negativeRegisterLoginExistsTest() {
        FullUser user = getRandomUser();
        userService.register(user);
        userService.register(user)
                .should(hasStatusCode(400))
                .should(hasMessage("Login already exist"));
    }

    @Test
    public void negativeRegisterNoPasswordTest() {
        FullUser user = getRandomUser();
        user.setPass(null);

        userService.register(user)
                .should(hasStatusCode(400))
                .should(hasMessage("Missing login or password"));
        ;
    }

    @Test
    public void positiveAdminAuthTest() {
        FullUser user = getAdminUser();

        String token = userService.auth(user)
                .should(hasStatusCode(200))
                .asJwt();

        Assertions.assertNotNull(token);
    }

    @Test
    public void positiveNewUserAuthTest() {
        FullUser user = getRandomUser();
        userService.register(user);
        userService.auth(user);
        String token = userService.auth(user)
                .should(hasStatusCode(200)).asJwt();

        Assertions.assertNotNull(token);
    }

    @Test
    public void negativeAuthTest() {
        FullUser user = getRandomUser();
        userService.auth(user).should(hasStatusCode(401));
    }

    @Test
    public void positiveGetUserInfo() {
        FullUser user = getAdminUser();
        String token = userService.auth(user).asJwt();
        userService.getUserInfo(token)
                .should(hasStatusCode(200));
    }

    @Test
    public void negativeGetUserInfoInvalidJwtTest() {
        userService.getUserInfo("fake jwt").should(hasStatusCode(401));

    }

    @Test
    public void negativeGetUserInfoWithoutJwtTest() {
        userService.getUserInfo().should(hasStatusCode(401));
    }

    @Test
    public void positiveChangeUserPasswordTest() {
        FullUser user = getRandomUser();
        String oldPass = user.getPass();
        userService.register(user);
        String token = userService.auth(user).asJwt();
        String updatedPassValue = "newPassss";
        userService.updatePass(updatedPassValue, token)
                .should(hasStatusCode(200))
                .should(hasMessage("User password successfully changed"));

        user.setPass(updatedPassValue);
        token = userService.auth(user).asJwt();

        FullUser updatedUser = userService.getUserInfo(token).as(FullUser.class);

         Assertions.assertNotEquals(oldPass, updatedUser.getPass());
    }

}

