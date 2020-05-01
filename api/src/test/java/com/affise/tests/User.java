package com.affise.tests;

import com.affise.api.annotations.Positive;
import com.affise.api.services.AffiliateApiService;
import com.affise.api.services.UserApiService;
import org.testng.annotations.Test;

import java.util.Map;

import static com.affise.api.conditions.Conditions.bodyField;
import static com.affise.api.conditions.Conditions.statusCode;
import static com.affise.api.constans.Constans.User.ADMIN;
import static com.affise.api.constans.Constans.UserType.ROLE_ADMIN;
import static com.affise.api.generatedata.GenerateUser.generateUserWithReqFields;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.emptyOrNullString;
import static org.hamcrest.Matchers.not;

public class User {

    private final UserApiService userApiService = new UserApiService();
    private final AffiliateApiService affiliateApiService = new AffiliateApiService();

    @Positive
    @Test(description = "Can create user with required fields")
    public void createUser() {
        // Data generation
        Map user = generateUserWithReqFields(ROLE_ADMIN);

        // Validation response
        userApiService.createUser(user, ADMIN)
                .shouldHave(statusCode(200))
                .shouldHave(bodyField("user.id", not(emptyOrNullString())))
                .shouldHave(bodyField("user.email", equalTo(user.get("email"))))
                .shouldHave(bodyField("user.first_name", equalTo(user.get("first_name"))))
                .shouldHave(bodyField("user.last_name", equalTo(user.get("last_name"))))
                .shouldHave(bodyField("user.roles[0]", equalTo(user.get("roles[]"))));
    }

}
