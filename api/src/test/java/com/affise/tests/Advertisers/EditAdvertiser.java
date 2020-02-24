package com.affise.tests.Advertisers;

import com.affise.api.annotations.Negative;
import com.affise.api.annotations.Positive;
import com.affise.api.payloads.Advertiser;
import com.affise.api.payloads.User;
import com.affise.api.services.AdvertiserApiService;
import com.affise.api.services.UserApiService;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

import static com.affise.api.conditions.Conditions.statusCode;
import static com.affise.api.constans.Constans.Data.*;
import static com.affise.api.constans.Constans.UserPermissions.ENTITY_ADVERTISER;
import static com.affise.api.constans.Constans.UserPermissionsLevel.*;
import static com.affise.api.constans.Constans.UserType.*;
import static com.affise.api.generatedata.GenerateAdvertiser.getNewAdvertiser;
import static com.affise.api.generatedata.GenerateUser.*;
import static com.affise.api.generatedata.Generations.generateMap;

public class EditAdvertiser {

    private final AdvertiserApiService advertiserApiService = new AdvertiserApiService();
    private final UserApiService userApiService = new UserApiService();
    private final Advertiser advertiser1 = getNewAdvertiser();
    private final Advertiser advertiser2 = getNewAdvertiser();
    private final Map advertBody = generateMap(email, url, skype);
    private final User admin = getNewUser(ROLE_ADMIN);
    private final User affiliate = getNewUser(ROLE_MAN_AFFILIATE);
    private final User sales = getNewUser(ROLE_MAN_SALES);
    private final ArrayList<User> users = new ArrayList<User>(Arrays.asList(admin,affiliate,sales));


    @Positive
    @Test(description = "User with (level == write) can edit advertiser")
    public void writeAdvert() {
        for (User user : users) {
            userApiService.updateUserPermissions(user.id(), changeUserPermission(ENTITY_ADVERTISER, WRITE));
            advertiserApiService.editAdvertiser(advertBody, user.apiKey(), advertiser1.id())
                    .shouldHave(statusCode(200));
        }
    }

    @Test(description = "User with (exception == write) can edit advertiser")
    public void writeException() {
        User user = getNewUser();
        userApiService.updateUserPermissions(user.id(), changeUserPermException(ENTITY_ADVERTISER, DENY, WRITE, advertiser2.id()));
        advertiserApiService.editAdvertiser(advertBody, user.apiKey(), advertiser2.id())
                .shouldHave(statusCode(200));
    }



    @Negative
    @Test(description = "User with (level == deny) can't edit advertiser")
    public void denyAdvert() {
        for (User user : users) {
            userApiService.updateUserPermissions(user.id(), changeUserPermission(ENTITY_ADVERTISER, DENY));
            advertiserApiService.editAdvertiser(advertBody, user.apiKey(), advertiser1.id())
                    .shouldHave(statusCode(403));
        }
    }

    @Test(description = "User with (level == read) can't edit advertiser")
    public void readAdvert() {
        for (User user : users) {
            userApiService.updateUserPermissions(user.id(), changeUserPermission(ENTITY_ADVERTISER, READ));
            advertiserApiService.editAdvertiser(advertBody, user.apiKey(), advertiser1.id())
                    .shouldHave(statusCode(403));
        }
    }

}
