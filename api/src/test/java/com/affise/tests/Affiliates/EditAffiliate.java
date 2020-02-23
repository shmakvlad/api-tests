package com.affise.tests.Affiliates;

import com.affise.api.annotations.Negative;
import com.affise.api.annotations.Positive;
import com.affise.api.payloads.Affiliate;
import com.affise.api.payloads.User;
import com.affise.api.services.AffiliateApiService;
import com.affise.api.services.UserApiService;
import org.testng.annotations.Test;

import java.util.Map;

import static com.affise.api.conditions.Conditions.statusCode;
import static com.affise.api.constans.Constans.UserPermissions.*;
import static com.affise.api.constans.Constans.Data.*;
import static com.affise.api.constans.Constans.UserPermissionsLevel.*;
import static com.affise.api.generatedata.GenerateAffiliate.getNewAffiliate;
import static com.affise.api.generatedata.GenerateUser.changeUserPermException;
import static com.affise.api.generatedata.GenerateUser.getNewUser;
import static com.affise.api.generatedata.Generations.generateMap;


public class EditAffiliate {

    private final AffiliateApiService affiliateApiService = new AffiliateApiService();
    private final UserApiService userApiService = new UserApiService();
    private final Affiliate affiliate = getNewAffiliate();
    private final Map partner = generateMap(email, password, login);

    @Positive
    @Test(description = "User with (exception == write) can edit affiliate")
    public void writeException() {
        // Generate data
        User user = getNewUser();
        userApiService.updateUserPermissions(user.id(), changeUserPermException(ENTITY_AFFILIATE, DENY, WRITE, affiliate.id()));

        // Validation assert
        affiliateApiService.editAffiliate(partner, user.apiKey(), affiliate.id())
                .shouldHave(statusCode(200));
    }

    @Negative
    @Test(description = "User with (exception == read) can't edit affiliate")
    public void readException() {
        // Generate data
        User user = getNewUser();
        userApiService.updateUserPermissions(user.id(), changeUserPermException(ENTITY_AFFILIATE, DENY, READ, affiliate.id()));

        // Validation assert
        affiliateApiService.editAffiliate(partner, user.apiKey(), affiliate.id())
                .shouldHave(statusCode(403));
    }

    @Negative
    @Test(description = "User with (exception == deny) can't edit affiliate")
    public void denyException() {
        // Generate data
        User user = getNewUser();
        userApiService.updateUserPermissions(user.id(), changeUserPermException(ENTITY_AFFILIATE, READ, DENY, affiliate.id()));

        // Validation assert
        affiliateApiService.editAffiliate(partner, user.apiKey(), affiliate.id())
                .shouldHave(statusCode(403));
    }

}
