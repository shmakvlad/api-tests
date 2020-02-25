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

import static com.affise.api.conditions.Conditions.bodyField;
import static com.affise.api.conditions.Conditions.statusCode;
import static com.affise.api.constans.Constans.Data.*;
import static com.affise.api.constans.Constans.User.ADMIN;
import static com.affise.api.constans.Constans.UserPermissions.ENTITY_ADVERTISER;
import static com.affise.api.constans.Constans.UserPermissionsLevel.*;
import static com.affise.api.constans.Constans.UserType.*;
import static com.affise.api.generatedata.GenerateAdvertiser.getNewAdvertiser;
import static com.affise.api.generatedata.GenerateUser.*;
import static com.affise.api.generatedata.Generations.generateMap;
import static org.hamcrest.Matchers.*;

public class EditAdvertiser {

    private final AdvertiserApiService advertiserApiService = new AdvertiserApiService();
    private final UserApiService userApiService = new UserApiService();
    private final Advertiser advertiser1 = getNewAdvertiser();
    private final Advertiser advertiser2 = getNewAdvertiser();
    private final Advertiser ownAdvertiser = getNewAdvertiser();
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
                    .shouldHave(statusCode(200))
                    .shouldHave(bodyField("advertiser.email", equalTo(advertBody.get(email))))
                    .shouldHave(bodyField("advertiser.url", equalTo(advertBody.get(url))))
                    .shouldHave(bodyField("advertiser.skype", equalTo(advertBody.get(skype))));
        }
    }

    @Test(description = "User with (exception == write) can edit advertiser")
    public void writeException() {
        for (User user : users) {
            userApiService.updateUserPermissions(user.id(), changeUserPermException(ENTITY_ADVERTISER, DENY, WRITE, advertiser2.id()))
                    .shouldHave(statusCode(200));
            advertiserApiService.editAdvertiser(advertBody, user.apiKey(), advertiser2.id())
                    .shouldHave(statusCode(200));
        }
    }

    @Test(description = "Sales can edit own advertiser")
    public void salesEditOwnAdvert() {
        advertiserApiService.editAdvertiser(manager, sales.id(), ADMIN, ownAdvertiser.id())
                .shouldHave(statusCode(200));

        userApiService.updateUserPermissions(sales.id(), changeUserPermission(ENTITY_ADVERTISER, DENY))
                .shouldHave(statusCode(200));

        advertiserApiService.editAdvertiser(advertBody, sales.apiKey(), ownAdvertiser.id())
                .shouldHave(statusCode(200));
    }

    @Test(description = "Sales with (level == deny) can't change manager at own advertiser")
    public void salesCanNotChangeManager() {
        advertiserApiService.editAdvertiser(manager, sales.id(), ADMIN, ownAdvertiser.id())
                .shouldHave(statusCode(200));

        userApiService.updateUserPermissions(sales.id(), changeUserPermission(ENTITY_ADVERTISER, DENY))
                .shouldHave(statusCode(200));

        advertiserApiService.editAdvertiser(manager, admin.id(), sales.apiKey(), ownAdvertiser.id())
                .shouldHave(statusCode(200))
                .shouldHave(bodyField("advertiser.manager", equalTo(sales.id())));
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

    @Test(description = "User with (exception == read) can't edit advertiser")
    public void readException() {
        User user = getNewUser();
        userApiService.updateUserPermissions(user.id(), changeUserPermException(ENTITY_ADVERTISER, DENY, READ, advertiser2.id()))
                .shouldHave(statusCode(200));
        advertiserApiService.editAdvertiser(advertBody, user.apiKey(), advertiser2.id())
                .shouldHave(statusCode(403));
    }

    @Test(description = "User with (exception == deny) can't edit advertiser")
    public void denyException() {
        User user = getNewUser();
        userApiService.updateUserPermissions(user.id(), changeUserPermException(ENTITY_ADVERTISER, WRITE, DENY, advertiser2.id()))
                .shouldHave(statusCode(200));
        advertiserApiService.editAdvertiser(advertBody, user.apiKey(), advertiser2.id())
                .shouldHave(statusCode(403));
    }

    @Test(description = "Administrator can't edit own advertiser")
    public void adminEditOwnAdvert() {
        advertiserApiService.editAdvertiser(manager, admin.id(), ADMIN, ownAdvertiser.id())
                .shouldHave(statusCode(200));

        userApiService.updateUserPermissions(admin.id(), changeUserPermission(ENTITY_ADVERTISER, DENY))
                .shouldHave(statusCode(200));

        advertiserApiService.editAdvertiser(advertBody, admin.apiKey(), ownAdvertiser.id())
                .shouldHave(statusCode(403));
    }

    @Test(description = "Affiliate can't edit own advertiser")
    public void affiliateEditOwnAdvert() {
        advertiserApiService.editAdvertiser(manager, affiliate.id(), ADMIN, ownAdvertiser.id())
                .shouldHave(statusCode(200));

        userApiService.updateUserPermissions(affiliate.id(), changeUserPermission(ENTITY_ADVERTISER, DENY))
                .shouldHave(statusCode(200));

        advertiserApiService.editAdvertiser(advertBody, affiliate.apiKey(), ownAdvertiser.id())
                .shouldHave(statusCode(403));
    }

}
