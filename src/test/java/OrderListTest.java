import io.qameta.allure.junit4.DisplayName;
import org.example.*;
import org.hamcrest.MatcherAssert;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.notNullValue;

@DisplayName("Список заказов")
public class OrderListTest {

    private Client client;
    private OrderClient orderClient;

    @Before
    public void setUp(){
        client = new Client();
        orderClient = new OrderClient();
    }

    @DisplayName("Проверка получения списка заказов")
    @Test
    public void getFullListOrder(){

        OrderList orderList = orderClient.orderList();
        MatcherAssert.assertThat(orderList, notNullValue());
        System.out.println(orderList.getOrders());

    }
}
