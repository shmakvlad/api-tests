package com.affise.tests.Affiliates;

import com.affise.api.annotations.Negative;
import com.affise.api.annotations.Positive;
import com.affise.api.payloads.User;
import com.affise.api.services.AffiliateApiService;
import com.affise.api.services.UserApiService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import static com.affise.api.conditions.Conditions.statusCode;
import static com.affise.api.constans.Constans.UserPermissions.ENTITY_AFFILIATE;
import static com.affise.api.constans.Constans.UserPermissionsLevel.*;
import static com.affise.api.constans.Constans.UserType.*;
import static com.affise.api.generatedata.GenerateAffiliate.generateAffiliateWithReqFields;
import static com.affise.api.generatedata.GenerateUser.*;
import static com.affise.api.generatedata.Generations.jsonNode;


public class AddAffiliate {

    private final AffiliateApiService affiliateApiService = new AffiliateApiService();
    private final UserApiService userApiService = new UserApiService();
    private final ObjectMapper om = new ObjectMapper();

    @Positive
    @Test(description = "User with write level can create affiliate")
    public void createAffiliate() throws IOException {
        ArrayList<String> users = new ArrayList<>(Arrays.asList(ROLE_ADMIN, ROLE_MAN_AFFILIATE, ROLE_MAN_SALES));
        for (String role : users) {
        // Generate data
            String json = userApiService.createUser(generateUserWithReqFields(role)).asString();
            User user = om.readValue(jsonNode(json, "user"), User.class);
            userApiService.updateUserPermissions(user.id(), changeUserPermission(ENTITY_AFFILIATE, WRITE));

        // Validation assert
            affiliateApiService.createAffiliate(generateAffiliateWithReqFields(), user.apiKey())
                    .shouldHave(statusCode(200));
        }
    }

    @Positive
    @Test(description = "Admin create affiliate")
    public void adminCreateAffiliate() throws IOException {
        // Generate data
            User user = getNewUser(ROLE_ADMIN);

        // 200 OK (level == write)
            userApiService.updateUserPermissions(user.id(), changeUserPermission(ENTITY_AFFILIATE, WRITE));
            affiliateApiService.createAffiliate(generateAffiliateWithReqFields(), user.apiKey())
                    .shouldHave(statusCode(200));

        // 403 Access denied (level == read)
            userApiService.updateUserPermissions(user.id(), changeUserPermission(ENTITY_AFFILIATE, READ));
            affiliateApiService.createAffiliate(generateAffiliateWithReqFields(), user.apiKey())
                        .shouldHave(statusCode(403));

        // 403 Access denied (level == deny)
            userApiService.updateUserPermissions(user.id(), changeUserPermission(ENTITY_AFFILIATE, DENY));
            affiliateApiService.createAffiliate(generateAffiliateWithReqFields(), user.apiKey())
                    .shouldHave(statusCode(403));
    }

    @Positive
    @Test(description = "User with type Administrator and (level == write) can create affiliate")
    public void adminWrite() throws IOException {
            User user = getNewUser(ROLE_ADMIN);

            userApiService.updateUserPermissions(user.id(), changeUserPermission(ENTITY_AFFILIATE, WRITE));
            affiliateApiService.createAffiliate(generateAffiliateWithReqFields(), user.apiKey())
                    .shouldHave(statusCode(200));
    }

    @Negative
    @Test(description = "User with type Administrator and (level != write) can not create affiliate")
    public void adminNoWrite() throws IOException {
        User user = getNewUser(ROLE_ADMIN);
        // level == read
            userApiService.updateUserPermissions(user.id(), changeUserPermission(ENTITY_AFFILIATE, READ));
            affiliateApiService.createAffiliate(generateAffiliateWithReqFields(), user.apiKey())
                    .shouldHave(statusCode(403));
        // level == deny
            userApiService.updateUserPermissions(user.id(), changeUserPermission(ENTITY_AFFILIATE, DENY));
            affiliateApiService.createAffiliate(generateAffiliateWithReqFields(), user.apiKey())
                    .shouldHave(statusCode(403));
    }



}
