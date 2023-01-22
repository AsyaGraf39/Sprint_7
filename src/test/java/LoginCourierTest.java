import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.example.Courier;
import org.example.CourierClient;
import org.example.CourierCredentials;
import org.example.CourierGenerator;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.*;
import static org.example.ConstantsErrorMessage.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@DisplayName("Логин курьера")
public class LoginCourierTest {

    private Courier courier;
    private CourierClient courierClient;
    private int id;

    @Before
    public void setUp(){
        courier = CourierGenerator.getRandomCourier();
        courierClient = new CourierClient();
    }

    @DisplayName("Проверка успешной авторизации курьера")
    @Test
    public void loginCourier(){
        courierClient.create(courier);
        ValidatableResponse response = courierClient.login(CourierCredentials.from(courier));
        int statusCode = response.extract().statusCode();
        id = response.extract().path("id");

        assertEquals(SC_OK, statusCode);
        assertNotNull(response.extract().path("id"));
        courierClient.delete(id);
    }

    @DisplayName("Проверка авторизации курьера без логина")
    @Test
    public void loginCourierNoLogin(){
        String login = courier.getLogin();
        courierClient.create(courier);
        courier.setLogin("");

        ValidatableResponse response = courierClient.login(CourierCredentials.from(courier));
        int statusCode = response.extract().statusCode();
        String errorMessage = response.extract().path("message");

        assertEquals(SC_BAD_REQUEST, statusCode);
        assertEquals(LOGIN_BAD_REQUEST, errorMessage);
        courier.setLogin(login);
        ValidatableResponse responseValidLogin = courierClient.login(CourierCredentials.from(courier));
        id = responseValidLogin.extract().path("id");
    }

    @DisplayName("Проверка авторизации курьера без пароля")
    @Test
    public void loginCourierNoPassword(){
        String password = courier.getPassword();
        courierClient.create(courier);
        courier.setPassword(null);
        ValidatableResponse response = courierClient.login(CourierCredentials.from(courier));
        int statusCode = response.extract().statusCode();
        String errorMessage = response.extract().path("message");

        assertEquals(SC_BAD_REQUEST, statusCode);
        assertEquals(LOGIN_BAD_REQUEST, errorMessage);
        courier.setPassword(password);
        ValidatableResponse responseValidLogin = courierClient.login(CourierCredentials.from(courier));
        id = responseValidLogin.extract().path("id");
    }

    @DisplayName("Проверка авторизации курьера с неизвестным логином")
    @Test
    public void loginCourierNotFound(){
        ValidatableResponse response = courierClient.login(CourierCredentials.from(courier));
        int statusCode = response.extract().statusCode();
        String errorMessage = response.extract().path("message");

        assertEquals(SC_NOT_FOUND, statusCode);
        assertEquals(LOGIN_NOT_FOUND, errorMessage);
    }

    @After
    public void cleanUp(){
        courierClient.delete(id);
    }

}
