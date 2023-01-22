package org.example;

import io.qameta.allure.Step;
import org.apache.commons.lang3.RandomStringUtils;

public class CourierGenerator {

    @Step("Рандомное заполнение полей логина, пароля и имени курьера")
    public static Courier getRandomCourier(){
        String login = RandomStringUtils.randomAlphanumeric(8);
        String password = RandomStringUtils.randomAlphanumeric(8);
        String firstname = RandomStringUtils.randomAlphabetic(8);

        return new Courier(login, password, firstname);
    }
}
