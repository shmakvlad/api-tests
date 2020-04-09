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

import static com.affise.api.conditions.Conditions.bodyField;
import static com.affise.api.conditions.Conditions.statusCode;
import static com.affise.api.constans.Constans.Data.manager;
import static com.affise.api.constans.Constans.User.ADMIN;
import static com.affise.api.constans.Constans.UserPermissions.ENTITY_ADVERTISER;
import static com.affise.api.constans.Constans.UserPermissionsLevel.*;
import static com.affise.api.constans.Constans.UserType.*;
import static com.affise.api.generatedata.GenerateAdvertiser.getNewAdvertiser;
import static com.affise.api.generatedata.GenerateUser.*;
import static org.hamcrest.Matchers.*;

public class GetAdvertiser {

    private final AdvertiserApiService advertiserApiService = new AdvertiserApiService();
    private final UserApiService userApiService = new UserApiService();
    private final Advertiser adv1 = getNewAdvertiser();
    private final Advertiser adv2 = getNewAdvertiser();
    private final Advertiser adv3 = getNewAdvertiser();
    private final User admin = getNewUser(ROLE_ADMIN);
    private final User affiliate = getNewUser(ROLE_MAN_AFFILIATE);
    private final User sales = getNewUser(ROLE_MAN_SALES);
    private final ArrayList<User> users = new ArrayList<User>(Arrays.asList(admin, affiliate, sales));


    @Positive
    @Test(description = "User with (level == write) can get advertiser list")
    public void writeGetAdvert() {
        for (User user : users) {
            userApiService.updateUserPermissions(user.id(), changeUserPermission(ENTITY_ADVERTISER, WRITE));
            advertiserApiService.getListAdvertisers(user.apiKey())
                    .shouldHave(statusCode(200))
                    .shouldHave(bodyField("advertisers.id", hasItems(adv1.id(), adv2.id(), adv3.id())));
        }
    }

    @Test(description = "User with (level == read) can get advertiser list")
    public void readGetAdvert() {
        for (User user : users) {
            userApiService.updateUserPermissions(user.id(), changeUserPermission(ENTITY_ADVERTISER, READ));
            advertiserApiService.getListAdvertisers(user.apiKey())
                    .shouldHave(statusCode(200))
                    .shouldHave(bodyField("advertisers.id", hasItems(adv1.id(), adv2.id(), adv3.id())));
        }
    }

    @Test(description = "Sales with (level == deny) can get advertiser list")
    public void salesDenyGetAdvert() {
        User user = getNewUser(ROLE_MAN_SALES);
        userApiService.updateUserPermissions(user.id(), changeUserPermission(ENTITY_ADVERTISER, DENY));
        advertiserApiService.editAdvertiser(manager, user.id(), ADMIN, adv3.id())
                .shouldHave(statusCode(200));
        advertiserApiService.getListAdvertisers(user.apiKey())
                .shouldHave(statusCode(200))
                .shouldHave(bodyField("advertisers.id", hasItems(adv3.id())))
                .shouldHave(bodyField("advertisers.id", not(hasItems(adv2.id(), adv1.id()))));
    }

    @Test(description = "User with (exception == write) can get advertiser")
    public void writeExceptGetAdvert() {
        User user = getNewUser();
        userApiService.updateUserPermissions(user.id(), changeUserPermException(ENTITY_ADVERTISER, DENY, WRITE, adv1.id()));
        advertiserApiService.getListAdvertisers(user.apiKey())
                .shouldHave(statusCode(200))
                .shouldHave(bodyField("advertisers.id", hasItems(adv1.id())))
                .shouldHave(bodyField("advertisers.id", not(hasItems(adv2.id(), adv3.id()))));
    }

    @Test(description = "User with (exception == read) can get advertiser")
    public void readExceptGetAdvert() {
        User user = getNewUser();
        userApiService.updateUserPermissions(user.id(), changeUserPermException(ENTITY_ADVERTISER, DENY, READ, adv1.id()));
        advertiserApiService.getListAdvertisers(user.apiKey())
                .shouldHave(statusCode(200))
                .shouldHave(bodyField("advertisers.id", hasItems(adv1.id())))
                .shouldHave(bodyField("advertisers.id", not(hasItems(adv2.id(), adv3.id()))));
    }


    @Negative
    @Test(description = "Affiliates with (level == deny) can't get advertiser list")
    public void affiliateDenyGetAdvert() {
        User user = getNewUser(ROLE_MAN_AFFILIATE);
        userApiService.updateUserPermissions(user.id(), changeUserPermission(ENTITY_ADVERTISER, DENY));
        advertiserApiService.getListAdvertisers(user.apiKey())
                .shouldHave(statusCode(403));
    }

    @Test(description = "Admin with (level == deny) can't get advertiser list")
    public void adminDenyGetAdvert() {
        User user = getNewUser(ROLE_ADMIN);
        userApiService.updateUserPermissions(user.id(), changeUserPermission(ENTITY_ADVERTISER, DENY));
        advertiserApiService.getListAdvertisers(user.apiKey())
                .shouldHave(statusCode(403));
    }

    @Test(description = "User with (exception == deny) can't get advertiser")
    public void denyExceptGetAdvert() {
        User user = getNewUser();
        userApiService.updateUserPermissions(user.id(), changeUserPermException(ENTITY_ADVERTISER, WRITE, DENY, adv1.id()));
        advertiserApiService.getListAdvertisers(user.apiKey())
                .shouldHave(statusCode(200))
                .shouldHave(bodyField("advertisers.id", not(hasItems(adv1.id()))))
                .shouldHave(bodyField("advertisers.id", hasItems(adv2.id(), adv3.id())));
    }

}
