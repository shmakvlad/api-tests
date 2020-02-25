package com.affise.api.generatedata;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;

import java.io.IOException;
import java.util.*;

import static com.affise.api.constans.Constans.Data.*;

public class Generations {

    private static final Faker faker = new Faker(new Locale("en"));
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static String generateEmail() {
        return faker.internet().emailAddress();
    }

    public static String generatePassword() {
        return faker.internet().password(6, 12);
    }

    public static String generateFirstName() {
        return faker.name().firstName();
    }

    public static String generateLastName() {
        return faker.name().lastName();
    }

    public static String generateFullName() {
        return faker.name().fullName();
    }

    public static String generateUrl() {
        return faker.internet().url();
    }

    public static String jsonNode(String tree, String jsonPath) throws IOException {
        return objectMapper.readTree(tree).get(jsonPath).toString();
    }

    public static Map generateMap(String... field) {
        Map<String, Object> map = new HashMap<>();
        ArrayList<String> list = new ArrayList<String>(Arrays.asList(field));
        for (String key : list) {
            if (key.equals(email)) map.put(key, generateEmail());
            else if (key.equals(password)) map.put(key, generatePassword());
            else if (key.equals(url)) map.put(key, "http://offers.dev.com");
            else if (key.equals(skype)) map.put(key, generateLastName());
            else map.put(key, generateFirstName());
        }
        return map;
    }

}
