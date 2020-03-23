package com.affise.tests.Offers;

import com.affise.api.annotations.Negative;
import com.affise.api.annotations.Positive;
import com.affise.api.database.ConnectToMongo;
import com.affise.api.payloads.Advertiser;
import com.affise.api.payloads.Offers.Offer;
import com.affise.api.payloads.User;
import com.affise.api.services.AdvertiserApiService;
import com.affise.api.services.OfferApiService;
import com.affise.api.services.UserApiService;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;

import java.util.Arrays;

import static com.affise.api.conditions.Conditions.bodyField;
import static com.affise.api.conditions.Conditions.statusCode;
import static com.affise.api.constans.Constans.PermissionsLevel.ENTITY_ADVERTISER_LEVEL;
import static com.affise.api.constans.Constans.UserPermissionsLevel.*;
import static com.affise.api.constans.Constans.UserType.*;
import static com.affise.api.generatedata.GenerateAdvertiser.getNewAdvertiser;
import static com.affise.api.generatedata.GenerateOffer.*;
import static com.affise.api.generatedata.GenerateUser.getNewUser;
import static org.hamcrest.Matchers.equalTo;

public class EditOffer {

    private final AdvertiserApiService advertiserApiService = new AdvertiserApiService();
    private final UserApiService userApiService = new UserApiService();
    private final ConnectToMongo connectToMongo = new ConnectToMongo();
    private final OfferApiService offerApiService = new OfferApiService();

    private final User adminUser = getNewUser(ROLE_ADMIN);
    private final User affiliateUser = getNewUser(ROLE_MAN_AFFILIATE);
    private final User salesUser = getNewUser(ROLE_MAN_SALES);

    private final Advertiser advertiser1 = getNewAdvertiser();
    private final Advertiser advertiser2 = getNewAdvertiser();
    private final Advertiser advertiser3 = getNewAdvertiser();

    private final Offer offer1 = getNewOffer(advertiser1.id());
    private final Offer offer2 = getNewOffer(advertiser2.id());



    @Positive
    @Test(description = "User with level == WRITE can edit offer")
    public void userWriteEditOffer() {
        for (User user : Arrays.asList(adminUser, affiliateUser, salesUser)) {
            connectToMongo
                    .updateUserInCentralMongo("_id", user.id(), ENTITY_ADVERTISER_LEVEL, WRITE);

            offerApiService
                    .editOffer(generateOfferWithReqFields(advertiser1.id()), user.apiKey(), offer1.id())
                    .shouldHave(statusCode(200))
                    .shouldHave(bodyField("offer.advertiser", equalTo(advertiser1.id())));
        }
    }


    @Test(description = "Sales with level == DENY can edit offer with own advertiser")
    public void salesDenyEditOffer() {
    // Generate Data
        connectToMongo
                .updateUserInCentralMongo("_id", salesUser.id(), ENTITY_ADVERTISER_LEVEL, DENY);
        connectToMongo
                .updateAdvertiserInMongo("_id", advertiser2.id(), "manager", salesUser.id());
        connectToMongo
                .updateAdvertiserInCentralMongo("_id", advertiser2.id(), "manager_id", salesUser.id());

    // Validation Assert
        offerApiService
                .editOffer(generateOfferWithoutReqFields(), salesUser.apiKey(), offer2.id())
                .shouldHave(statusCode(200))
                .shouldHave(bodyField("offer.advertiser", equalTo(advertiser2.id())));
    }


    @Test(description = "Sales with level == READ can edit offer with own advertiser")
    public void salesReadEditOffer() {
    // Generate Data
        connectToMongo
                .updateUserInCentralMongo("_id", salesUser.id(), ENTITY_ADVERTISER_LEVEL, READ);
        connectToMongo
                .updateAdvertiserInMongo("_id", advertiser2.id(), "manager", salesUser.id());
        connectToMongo
                .updateAdvertiserInCentralMongo("_id", advertiser2.id(), "manager_id", salesUser.id());

    // Validation Assert
        offerApiService
                .editOffer(generateOfferWithoutReqFields(), salesUser.apiKey(), offer2.id())
                .shouldHave(statusCode(200))
                .shouldHave(bodyField("offer.advertiser", equalTo(advertiser2.id())));
    }


    @Test(description = "Sales with level == DENY can change manager on own advertiser")
    public void salesChangeOwnManagerDeny() {
        Advertiser advertiser4 = getNewAdvertiser();

    // Generate Data
        connectToMongo.updateUserInCentralMongo("_id", salesUser.id(), ENTITY_ADVERTISER_LEVEL, DENY);
        connectToMongo.updateAdvertiserInMongo("_id", advertiser2.id(), "manager", salesUser.id());
        connectToMongo.updateAdvertiserInCentralMongo("_id", advertiser2.id(), "manager_id", salesUser.id());
        connectToMongo.updateAdvertiserInMongo("_id", advertiser4.id(), "manager", salesUser.id());
        connectToMongo.updateAdvertiserInCentralMongo("_id", advertiser4.id(), "manager_id", salesUser.id());

    // Validation Assert
        offerApiService
                .editOffer(generateOfferWithReqFields(advertiser4.id()), salesUser.apiKey(), offer2.id())
                .shouldHave(statusCode(200))
                .shouldHave(bodyField("offer.advertiser", equalTo(advertiser4.id())));

    // Clean data
        connectToMongo.removeObject("admin", "suppliers", "_id", advertiser4.id());
    }


    @Test(description = "Sales with level == READ can change manager on own advertiser")
    public void salesChangeOwnManagerRead() {
        Advertiser advertiser4 = getNewAdvertiser();

    // Generate Data
        connectToMongo.updateUserInCentralMongo("_id", salesUser.id(), ENTITY_ADVERTISER_LEVEL, READ);
        connectToMongo.updateAdvertiserInMongo("_id", advertiser2.id(), "manager", salesUser.id());
        connectToMongo.updateAdvertiserInCentralMongo("_id", advertiser2.id(), "manager_id", salesUser.id());
        connectToMongo.updateAdvertiserInMongo("_id", advertiser4.id(), "manager", salesUser.id());
        connectToMongo.updateAdvertiserInCentralMongo("_id", advertiser4.id(), "manager_id", salesUser.id());

    // Validation Assert
        offerApiService
                .editOffer(generateOfferWithReqFields(advertiser4.id()), salesUser.apiKey(), offer2.id())
                .shouldHave(statusCode(200))
                .shouldHave(bodyField("offer.advertiser", equalTo(advertiser4.id())));

    // Clean data
        connectToMongo.removeObject("admin", "suppliers", "_id", advertiser4.id());
    }


    @Test(description = "Write exception")
    public void writeException() {
    // Generate Data
        connectToMongo
                .updateUserInCentralMongo("_id", salesUser.id(), ENTITY_ADVERTISER_LEVEL, DENY);
        connectToMongo
                .updateUserInCentralMongoAddToSet("_id", salesUser.id(), "scopes.users.entity-advertiser.exceptions.strings.write", advertiser1.id());

    // Validation Assert
        offerApiService
                .editOffer(generateOfferWithReqFields(advertiser1.id()), salesUser.apiKey(), offer1.id())
                .shouldHave(statusCode(200));

    // Clean data
        connectToMongo
                .updateUserInCentralMongoUnset("_id", salesUser.id(), "scopes.users.entity-advertiser.exceptions.strings.write");
    }




    @Negative
    @Test(description = "Deny exception")
    public void denyException() {
    // Generate Data
        connectToMongo
                .updateUserInCentralMongo("_id", salesUser.id(), ENTITY_ADVERTISER_LEVEL, WRITE);
        connectToMongo
                .updateUserInCentralMongoAddToSet("_id", salesUser.id(), "scopes.users.entity-advertiser.exceptions.strings.deny", advertiser1.id());

    // Validation Assert
        offerApiService
                .editOffer(generateOfferWithReqFields(advertiser1.id()), salesUser.apiKey(), offer1.id())
                .shouldHave(statusCode(403));

    // Clean data
        connectToMongo
                .updateUserInCentralMongoUnset("_id", salesUser.id(), "scopes.users.entity-advertiser.exceptions.strings.deny");
    }


    @Test(description = "Read exception")
    public void readException() {
    // Generate Data
        connectToMongo
                .updateUserInCentralMongo("_id", salesUser.id(), ENTITY_ADVERTISER_LEVEL, WRITE);
        connectToMongo
                .updateUserInCentralMongoAddToSet("_id", salesUser.id(), "scopes.users.entity-advertiser.exceptions.strings.read", advertiser1.id());

    // Validation Assert
        offerApiService
                .editOffer(generateOfferWithReqFields(advertiser1.id()), salesUser.apiKey(), offer1.id())
                .shouldHave(statusCode(403));

    // Clean data
        connectToMongo
                .updateUserInCentralMongoUnset("_id", salesUser.id(), "scopes.users.entity-advertiser.exceptions.strings.read");
    }


    @Test(description = "User with level == DENY can't edit offer")
    public void userDenyEditOffer() {
        for (User user : Arrays.asList(adminUser, affiliateUser, salesUser)) {
            connectToMongo
                    .updateUserInCentralMongo("_id", user.id(), ENTITY_ADVERTISER_LEVEL, DENY);

            offerApiService
                    .editOffer(generateOfferWithReqFields(advertiser1.id()), user.apiKey(), offer1.id())
                    .shouldHave(statusCode(403));
        }
    }


    @Test(description = "User with level == READ can't edit offer")
    public void userReadEditOffer() {
        for (User user : Arrays.asList(adminUser, affiliateUser, salesUser)) {
            connectToMongo
                    .updateUserInCentralMongo("_id", user.id(), ENTITY_ADVERTISER_LEVEL, READ);

            offerApiService
                    .editOffer(generateOfferWithReqFields(advertiser1.id()), user.apiKey(), offer1.id())
                    .shouldHave(statusCode(403));
        }
    }


    @Test(description = "Sales with level == DENY can't choose alien advertiser")
    public void salesChangeAlienManagerDeny() {
    // Generate Data
        connectToMongo
                .updateUserInCentralMongo("_id", salesUser.id(), ENTITY_ADVERTISER_LEVEL, DENY);
        connectToMongo
                .updateAdvertiserInMongo("_id", advertiser2.id(), "manager", salesUser.id());
        connectToMongo
                .updateAdvertiserInCentralMongo("_id", advertiser2.id(), "manager_id", salesUser.id());

    // Validation Assert
        offerApiService
                .editOffer(generateOfferWithReqFields(advertiser3.id()), salesUser.apiKey(), offer2.id())
                .shouldHave(statusCode(403));
    }


    @Test(description = "Sales with level == READ can't choose alien advertiser")
    public void salesChangeAlienManagerRead() {
    // Generate Data
        connectToMongo
                .updateUserInCentralMongo("_id", salesUser.id(), ENTITY_ADVERTISER_LEVEL, READ);
        connectToMongo
                .updateAdvertiserInMongo("_id", advertiser2.id(), "manager", salesUser.id());
        connectToMongo
                .updateAdvertiserInCentralMongo("_id", advertiser2.id(), "manager_id", salesUser.id());

    // Validation Assert
        offerApiService
                .editOffer(generateOfferWithReqFields(advertiser3.id()), salesUser.apiKey(), offer2.id())
                .shouldHave(statusCode(403));
    }


    @Test(description = "Admin with level == READ can't edit offer with own advertiser")
    public void adminReadEditOffer() {
    // Generate Data
        connectToMongo
                .updateUserInCentralMongo("_id", adminUser.id(), ENTITY_ADVERTISER_LEVEL, READ);
        connectToMongo
                .updateAdvertiserInMongo("_id", advertiser2.id(), "manager", adminUser.id());
        connectToMongo
                .updateAdvertiserInCentralMongo("_id", advertiser2.id(), "manager_id", adminUser.id());

    // Validation Assert
        offerApiService
                .editOffer(generateOfferWithoutReqFields(), adminUser.apiKey(), offer2.id())
                .shouldHave(statusCode(403));
    }


    @Test(description = "Admin with level == DENY can't edit offer with own advertiser")
    public void adminDenyEditOffer() {
    // Generate Data
        connectToMongo
                .updateUserInCentralMongo("_id", adminUser.id(), ENTITY_ADVERTISER_LEVEL, DENY);
        connectToMongo
                .updateAdvertiserInMongo("_id", advertiser2.id(), "manager", adminUser.id());
        connectToMongo
                .updateAdvertiserInCentralMongo("_id", advertiser2.id(), "manager_id", adminUser.id());

    // Validation Assert
        offerApiService
                .editOffer(generateOfferWithoutReqFields(), adminUser.apiKey(), offer2.id())
                .shouldHave(statusCode(403));
    }



    @AfterClass
    public void removeData(){
        connectToMongo.removeObject("admin", "users", "_id", adminUser.id());
        connectToMongo.removeObject("admin", "users", "_id", affiliateUser.id());
        connectToMongo.removeObject("admin", "users", "_id", salesUser.id());

        connectToMongo.removeObject("admin", "suppliers", "_id", advertiser1.id());
        connectToMongo.removeObject("admin", "suppliers", "_id", advertiser2.id());

        connectToMongo.removeObject("admin", "cpa_programs", "_id", offer1.offerId());
        connectToMongo.removeObject("admin", "cpa_programs", "_id", offer2.offerId());

        connectToMongo.closeConnection();
    }

}