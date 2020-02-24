package com.affise.api.generatedata;

import com.affise.api.payloads.User;
import com.affise.api.services.UserApiService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;

import java.util.HashMap;
import java.util.Map;

import static com.affise.api.constans.Constans.UserType.ROLE_ADMIN;
import static com.affise.api.generatedata.Generations.*;

public class GenerateUser {
    private final static UserApiService userApiService = new UserApiService();

    public static Map generateUserWithReqFields(){
        Map<String, Object> user = new HashMap<>();
            user.put("email", generateEmail());
            user.put("password", generatePassword());
            user.put("first_name", generateFirstName());
            user.put("last_name", generateLastName());
            user.put("roles[]", ROLE_ADMIN);
        return user;
    }

    public static Map generateUserWithReqFields(String usertype){
        Map<String, Object> user = new HashMap<>();
            user.put("email", generateEmail());
            user.put("password", generatePassword());
            user.put("first_name", generateFirstName());
            user.put("last_name", generateLastName());
            user.put("roles[]", usertype);
        return user;
    }

    @SneakyThrows
    public static User getNewUser(String usertype) {
            String json = userApiService.createUser(generateUserWithReqFields(usertype)).asString();
            User user = new ObjectMapper().readValue(jsonNode(json, "user"), User.class);
        return user;
    }

    @SneakyThrows
    public static User getNewUser() {
            String json = userApiService.createUser(generateUserWithReqFields(ROLE_ADMIN)).asString();
            User user = new ObjectMapper().readValue(jsonNode(json, "user"), User.class);
        return user;
    }

    public static String changeUserPermission(String permission, String level){
        return "{\"permissions\":{\"users\":{\"" + permission + "\":{\"level\":\"" + level + "\",\"exceptions\":{}}}}}";
    }

    public static String changeUserException(String level, Integer id){
        return "{\"permissions\":{\"users\":{\"entity-affiliate\":{\"level\":\"deny\",\"exceptions\":{\"ints\":{\"" + level+ "\":[" + id + "]}}}}}}";
    }

    public static String changeUserPermException(String permission, String level, String exceptionLevel, Integer id){
        return "{\"permissions\":{\"users\":{\""
                + permission + "\":{\"level\":\""
                + level + "\",\"exceptions\":{\"ints\":{\""
                + exceptionLevel+ "\":["
                + id +
                "]}}}}}}";
    }

    public static String changeUserPermException(String permission, String level, String exceptionLevel, String id){
        return "{\"permissions\":{\"users\":{\""
                + permission + "\":{\"level\":\""
                + level + "\",\"exceptions\":{\"ints\":{\""
                + exceptionLevel+ "\":["
                + id +
                "]}}}}}}";
    }
}
