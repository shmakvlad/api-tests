package com.affise.api.services;

import com.affise.api.assertions.AssertableResponse;
import io.qameta.allure.Step;

import java.util.Map;

import static com.affise.api.constans.Constans.Headers.API_KEY;
import static com.affise.api.constans.Constans.User.ADMIN;

public class UserApiService extends PhpApiService {

    public AssertableResponse createUser(Map user) {
        return new AssertableResponse(setUp()
                .header(API_KEY, ADMIN)
                .formParams(user)
                .when().post("/admin/user"));
    }

    @Step
    public AssertableResponse createUser(Map user, String usertype) {
        return new AssertableResponse(setUp()
                .header(API_KEY, usertype)
                .formParams(user)
                .when().post("/admin/user"));
    }

    public AssertableResponse updateUserPermissions(String object_id, String body) {
        return new AssertableResponse(setUpNewApi()
                .header(API_KEY, ADMIN)
                .body(body)
                .when().post("/user/{object_id}/permissions", object_id));
    }

}
