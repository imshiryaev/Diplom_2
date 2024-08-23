package praktikum;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.apache.commons.lang3.RandomStringUtils;
import org.example.model.User;
import org.example.steps.UserSteps;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class UserUpdateTest {
    private UserSteps userSteps = new UserSteps();
    private User user;
    private String accessToken;
    @Before
    public void setUp(){
        user = new User();
        user.setEmail(RandomStringUtils.randomAlphabetic(10) + "@mail.ru");
        user.setPassword(RandomStringUtils.randomAlphabetic(6));
        user.setName(RandomStringUtils.randomAlphabetic(8));
    }

    @After
    public void tearDown(){
        if (accessToken != null) userSteps.deleteUser(accessToken);
    }
    @Test
    @DisplayName("Проверка изменения email пользователя с авторизацией")
    public void userUpdateEmailWithAuth() {
        ValidatableResponse response = userSteps.createUser(user);
        accessToken = response.extract().path("accessToken");
        user.setEmail(RandomStringUtils.randomAlphabetic(7) + "@mail.ru");
        user.setPassword(null);
        user.setName(null);
        userSteps.changeUserData(user, accessToken).statusCode(200).body("success", Matchers.is(true));
    }

    @Test
    @DisplayName("Проверка изменения password пользователя с авторизацией")
    public void userUpdatePasswordWithAuth() {
        ValidatableResponse response = userSteps.createUser(user);
        accessToken = response.extract().path("accessToken");
        user.setEmail(null);
        user.setPassword(RandomStringUtils.randomAlphabetic(7));
        user.setName(null);
        userSteps.changeUserData(user, accessToken).statusCode(200).body("success", Matchers.is(true));
    }

    @Test
    @DisplayName("Проверка изменения name пользователя с авторизацией")
    public void userUpdateNameWithAuth() {
        ValidatableResponse response = userSteps.createUser(user);
        accessToken = response.extract().path("accessToken");
        user.setEmail(null);
        user.setPassword(null);
        user.setName(RandomStringUtils.randomAlphabetic(7));
        userSteps.changeUserData(user, accessToken).statusCode(200).body("success", Matchers.is(true));
    }

    @Test
    @DisplayName("Проверка изменения данных пользователя без авторизации")
    public void userChangeDataWithoutAuth() {
        ValidatableResponse response = userSteps.createUser(user);
        accessToken = response.extract().path("accessToken");
        user.setEmail(null);
        user.setPassword(RandomStringUtils.randomAlphabetic(7));
        user.setName(null);
        userSteps.changeUserDataWithoutAuth(user).statusCode(401).body("success", Matchers.is(false)).body("message",Matchers.is("You should be authorised"));
    }
}
