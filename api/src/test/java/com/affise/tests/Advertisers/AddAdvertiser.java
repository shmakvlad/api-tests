package com.affise.tests.Advertisers;

import com.affise.api.annotations.Negative;
import com.affise.api.annotations.Positive;
import com.affise.api.payloads.Php.User;
import com.affise.api.database.ConnectToMongo;
import com.affise.api.services.AdvertiserApiService;
import com.affise.api.services.UserApiService;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;


import static com.affise.api.conditions.Conditions.bodyField;
import static com.affise.api.conditions.Conditions.statusCode;
import static com.affise.api.constans.Constans.UserPermissions.*;
import static com.affise.api.constans.Constans.UserPermissionsLevel.*;
import static com.affise.api.constans.Constans.UserType.*;
import static com.affise.api.generatedata.GenerateAdvertiser.generateAdvertiserWithReqFields;
import static com.affise.api.generatedata.GenerateUser.changeUserPermission;
import static com.affise.api.generatedata.GenerateUser.getNewUser;
import static org.hamcrest.Matchers.*;


public class AddAdvertiser {

    private final AdvertiserApiService advertiserApiService = new AdvertiserApiService();
    private final ConnectToMongo connectToMongo = new ConnectToMongo();
    private final UserApiService userApiService = new UserApiService();

    private final User adminUser = getNewUser(ROLE_ADMIN);
    private final User affiliateUser = getNewUser(ROLE_MAN_AFFILIATE);
    private final User salesUser = getNewUser(ROLE_MAN_SALES);


    @Positive
    @Test(description = "User with type Administrator and (level == write) can create advertiser")
        public void adminWriteAdvert() {
        userApiService.updateUserPermissions(adminUser.id(), changeUserPermission(ENTITY_ADVERTISER, WRITE));

        advertiserApiService.createAdvertiser(generateAdvertiserWithReqFields(), adminUser.apiKey())
               .shouldHave(statusCode(200))
               .shouldHave(bodyField("advertiser.manager", is(emptyOrNullString())));
    }

    @Test(description = "User with type Affiliate and (level == write) can create advertiser")
    public void affiliateWriteAdvert() {
        userApiService.updateUserPermissions(affiliateUser.id(), changeUserPermission(ENTITY_ADVERTISER, WRITE));

        advertiserApiService.createAdvertiser(generateAdvertiserWithReqFields(), affiliateUser.apiKey())
                .shouldHave(statusCode(200))
                .shouldHave(bodyField("advertiser.manager", is(emptyOrNullString())));
    }

    @Test(description = "User with type Account and (level == write) can create advertiser")
    public void salesWriteAdvert() {
        userApiService.updateUserPermissions(salesUser.id(), changeUserPermission(ENTITY_ADVERTISER, WRITE));

        advertiserApiService.createAdvertiser(generateAdvertiserWithReqFields(), salesUser.apiKey())
                .shouldHave(statusCode(200))
                .shouldHave(bodyField("advertiser.manager", is(emptyOrNullString())));
    }

    @Test(description = "User with type Account and (level == read) can create advertiser")
    public void salesReadAdvert() {
        userApiService.updateUserPermissions(salesUser.id(), changeUserPermission(ENTITY_ADVERTISER, READ));

        advertiserApiService.createAdvertiser(generateAdvertiserWithReqFields(), salesUser.apiKey())
                .shouldHave(statusCode(200))
                .shouldHave(bodyField("advertiser.manager", equalTo(salesUser.id())));
    }

    @Test(description = "User with type Account and (level == deny) can create advertiser")
    public void salesDenyAdvert() {
        userApiService.updateUserPermissions(salesUser.id(), changeUserPermission(ENTITY_ADVERTISER, DENY));

        advertiserApiService.createAdvertiser(generateAdvertiserWithReqFields(), salesUser.apiKey())
                .shouldHave(statusCode(200))
                .shouldHave(bodyField("advertiser.manager", equalTo(salesUser.id())));
    }


    @Negative
    @Test(description = "User with type Administrator and (level == read) can't create advertiser")
    public void adminReadAdvert() {
        userApiService.updateUserPermissions(adminUser.id(), changeUserPermission(ENTITY_ADVERTISER, READ));

        advertiserApiService.createAdvertiser(generateAdvertiserWithReqFields(), adminUser.apiKey())
               .shouldHave(statusCode(403));
    }

    @Test(description = "User with type Administrator and (level == deny) can't create advertiser")
    public void adminDenyAdvert() {
        userApiService.updateUserPermissions(adminUser.id(), changeUserPermission(ENTITY_ADVERTISER, DENY));

        advertiserApiService.createAdvertiser(generateAdvertiserWithReqFields(), adminUser.apiKey())
               .shouldHave(statusCode(403));
    }

    @Test(description = "User with type Affiliate and (level == read) can create advertiser")
    public void affiliateReadAdvert() {
        userApiService.updateUserPermissions(affiliateUser.id(), changeUserPermission(ENTITY_ADVERTISER, READ));

        advertiserApiService.createAdvertiser(generateAdvertiserWithReqFields(), affiliateUser.apiKey())
                .shouldHave(statusCode(403));
    }

    @Test(description = "User with type Affiliate and (level == deny) can create advertiser")
    public void affiliateDenyAdvert() {
        userApiService.updateUserPermissions(affiliateUser.id(), changeUserPermission(ENTITY_ADVERTISER, DENY));

        advertiserApiService.createAdvertiser(generateAdvertiserWithReqFields(), affiliateUser.apiKey())
                .shouldHave(statusCode(403));
    }

    @AfterClass
    public void removeData(){
        connectToMongo.removeObject("admin", "users", "_id", adminUser.id());
        connectToMongo.removeObject("admin", "users", "_id", affiliateUser.id());
        connectToMongo.removeObject("admin", "users", "_id", salesUser.id());
        connectToMongo.closeConnection();
    }

}
