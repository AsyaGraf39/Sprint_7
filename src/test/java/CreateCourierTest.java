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
import static org.example.ConstantsErrorMessage.COURIER_CONFLICT;
import static org.example.ConstantsErrorMessage.CREATE_COURIER_BAD_REQUEST;
import static org.junit.Assert.assertEquals;

@DisplayName("Создание курьера")
public class CreateCourierTest {

    private Courier courier;
    private CourierClient courierClient;
    private int id;

    @Before
    public void setUp(){
        courier = CourierGenerator.getRandomCourier();
        courierClient = new CourierClient();
    }

    @DisplayName("Проверка успешного создания курьера")
    @Test
    public void courierCanBeCreated(){
        ValidatableResponse response = courierClient.create(courier);
        ValidatableResponse loginResponse = courierClient.login(CourierCredentials.from(courier));
        int statusCode = response.extract().statusCode();
        boolean isCreated = true;


        id = loginResponse.extract().path("id");

        assertEquals(SC_CREATED, statusCode);
        assertEquals(isCreated, response.extract().path("ok"));
    }

    @DisplayName("Проверка создания курьера с повторяющимся логином")
    @Test
    public void courier409Conflict(){
        courierClient.create(courier);
        ValidatableResponse loginResponse = courierClient.login(CourierCredentials.from(courier));
        ValidatableResponse response = courierClient.create(courier);
        int statusCode = response.extract().statusCode();
        String errorMessage = response.extract().path("message");

        id = loginResponse.extract().path("id");

        assertEquals(SC_CONFLICT, statusCode);
        assertEquals(COURIER_CONFLICT, errorMessage);
    }

    @DisplayName("Проверка создания курьера без логина")
    @Test
    public void createCourierNoLogin(){
        courier.setLogin("");
        ValidatableResponse response = courierClient.create(courier);
        int statusCode = response.extract().statusCode();
        String errorMessage = response.extract().path("message");

        assertEquals(SC_BAD_REQUEST, statusCode);
        assertEquals(CREATE_COURIER_BAD_REQUEST, errorMessage);
    }

    @DisplayName("Проверка создания курьера без пароля")
    @Test
    public void createCourierNoPassword(){
        courier.setPassword(null);
        ValidatableResponse response = courierClient.create(courier);
        int statusCode = response.extract().statusCode();
        String errorMessage = response.extract().path("message");

        assertEquals(SC_BAD_REQUEST, statusCode);
        assertEquals(CREATE_COURIER_BAD_REQUEST, errorMessage);
    }

    @After
    public void cleanUp(){
        courierClient.delete(id);
    }
}
