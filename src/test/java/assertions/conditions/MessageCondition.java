package assertions.conditions;

import assertions.Condition;
import io.restassured.response.ValidatableResponse;
import lombok.RequiredArgsConstructor;
import models.UserInfoResponse;
import org.junit.jupiter.api.Assertions;

@RequiredArgsConstructor
public class MessageCondition implements Condition {

    private final String expectedMessage;
    @Override
    public void check(ValidatableResponse response) {
        UserInfoResponse userInfoResponse = response.extract().jsonPath().getObject("info", UserInfoResponse.class);
        Assertions.assertEquals(expectedMessage, userInfoResponse.getMessage());
    }
}
