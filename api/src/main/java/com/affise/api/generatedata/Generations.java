package com.affise.api.generatedata;

import com.github.javafaker.Faker;

import java.util.Locale;

public class Generations {

    private static final Faker faker = new Faker(new Locale("en"));

    public static String generateEmail() {
        return faker.internet().emailAddress();
    }

    public static String generatePassword() {
        return faker.internet().password(6, 12);
    }

}
