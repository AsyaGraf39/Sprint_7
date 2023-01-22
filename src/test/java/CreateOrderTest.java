import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.example.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.apache.http.HttpStatus.SC_CREATED;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@DisplayName("Создание заказа")
@RunWith(Parameterized.class)
public class CreateOrderTest {

    private final String firstName;
    private final String lastName;
    private final String address;
    private final String metroStation;
    private final String phone;
    private final Number rentTime;
    private final String deliveryDate;
    private final String comment;
    private final String[] color;
    private OrderClient orderClient;
    private int track;


    public CreateOrderTest(String firstName, String lastName, String address, String metroStation, String phone,
                           Number rentTime, String date, String comment, String[] color) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.metroStation = metroStation;
        this.phone = phone;
        this.rentTime = rentTime;
        deliveryDate = date;
        this.comment = comment;

        this.color = color;
    }

    @DisplayName("Заполнение полей заказа с использование параметризации")
    @Step("заполение полей для заказа")
    @Parameterized.Parameters
    public static Object[][] getCredentials() {
        String[] oneColor = new String[] { "BLACK"};
        String[] twoColor = new String[] {"BLACK, GREY"};
        String[] noColor = new String[] {""};
        String[] nullColor = new String[] {null};

        return new Object[][] {
                { "Наруто", "Удзумаки", "Коноха", "8", "88005553535", 3, "2020-01-01", "111", oneColor},
                { "Наруто", "Удзумаки", "Коноха", "8", "88005553535", 3, "2020-01-01", "111", twoColor},
                { "Наруто", "Удзумаки", "Коноха", "8", "88005553535", 3, "2020-01-01", "111", noColor},
                { "Наруто", "Удзумаки", "Коноха", "8", "88005553535", 3, "2020-01-01", "111", nullColor},
                { "Наруто", "Удзумаки", "Коноха", "8", "88005553535", 3, "2020-01-01", "111", null},
        };
    }

    @Before
    public void setUp(){
        orderClient = new OrderClient();
    }

    @Description("Проверка создания заказа с указанием одного цвета, двух цветов и без указания цветов")
    @Test
    public void createOrder(){
        Order order = new Order(firstName, lastName, address, metroStation, phone, rentTime,deliveryDate, comment, color);
        ValidatableResponse response = orderClient.create(order);
        int statusCode = response.extract().statusCode();
        track = response.extract().path("track");

        assertEquals(SC_CREATED, statusCode);
        assertNotNull(track);
        System.out.println(track);
    }

    @After
    public void cleanUp(){
        orderClient.cancelOrder(track);
    }

}
