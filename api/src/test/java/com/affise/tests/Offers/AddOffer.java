package com.affise.tests.Offers;

import com.affise.api.annotations.Negative;
import com.affise.api.annotations.Positive;
import com.affise.api.database.ConnectToMongo;
import com.affise.api.payloads.Advertiser;
import com.affise.api.payloads.User;
import com.affise.api.services.AdvertiserApiService;
import com.affise.api.services.OfferApiService;
import com.affise.api.services.UserApiService;
import io.restassured.response.Response;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;

import java.util.Arrays;

import static com.affise.api.conditions.Conditions.bodyField;
import static com.affise.api.conditions.Conditions.statusCode;
import static com.affise.api.constans.Constans.PermissionsLevel.ENTITY_ADVERTISER_LEVEL;
import static com.affise.api.constans.Constans.UserPermissionsLevel.*;
import static com.affise.api.constans.Constans.UserType.*;
import static com.affise.api.generatedata.GenerateAdvertiser.getNewAdvertiser;
import static com.affise.api.generatedata.GenerateOffer.generateOfferWithReqFields;
import static com.affise.api.generatedata.GenerateUser.getNewUser;
import static org.hamcrest.Matchers.equalTo;

public class AddOffer {

    private final AdvertiserApiService advertiserApiService = new AdvertiserApiService();
    private final UserApiService userApiService = new UserApiService();
    private final OfferApiService offerApiService = new OfferApiService();
    private final ConnectToMongo connectToMongo = new ConnectToMongo();

    private final User adminUser = getNewUser(ROLE_ADMIN);
    private final User affiliateUser = getNewUser(ROLE_MAN_AFFILIATE);
    private final User salesUser = getNewUser(ROLE_MAN_SALES);

    private final Advertiser advertiser1 = getNewAdvertiser();
    private final Advertiser advertiser2 = getNewAdvertiser();



    @Positive
    @Test(description = "User with type Administrator and (level == WRITE) can create offer")
    public void adminWriteOffer() {
        connectToMongo
                .updateUserInCentralMongo("_id", adminUser.id() , ENTITY_ADVERTISER_LEVEL, WRITE);

        Response offer = offerApiService
                .createOffer(generateOfferWithReqFields(advertiser1.id()), adminUser.apiKey())
                    .shouldHave(statusCode(200))
                    .shouldHave(bodyField("offer.advertiser", equalTo(advertiser1.id())))
                    .getResponse();

        if (offer.getStatusCode() == 200){
            connectToMongo.removeObject("admin", "cpa_programs", "_id", offer.path("offer.offer_id"));
        }
    }


    @Test(description = "User with type Affiliate and (level == WRITE) can create offer")
    public void affiliateWriteOffer() {
        connectToMongo
                .updateUserInCentralMongo("_id", affiliateUser.id() , ENTITY_ADVERTISER_LEVEL, WRITE);

        Response offer = offerApiService
                .createOffer(generateOfferWithReqFields(advertiser1.id()), affiliateUser.apiKey())
                    .shouldHave(statusCode(200))
                    .shouldHave(bodyField("offer.advertiser", equalTo(advertiser1.id())))
                    .getResponse();

        if (offer.getStatusCode() == 200){
            connectToMongo.removeObject("admin", "cpa_programs", "_id", offer.path("offer.offer_id"));
        }
    }


    @Test(description = "User with type Sales and (level == WRITE) can create offer")
    public void salesWriteOffer() {
        connectToMongo
                .updateUserInCentralMongo("_id", salesUser.id() , ENTITY_ADVERTISER_LEVEL, WRITE);

        Response offer = offerApiService
                .createOffer(generateOfferWithReqFields(advertiser1.id()), salesUser.apiKey())
                    .shouldHave(statusCode(200))
                    .shouldHave(bodyField("offer.advertiser", equalTo(advertiser1.id())))
                    .getResponse();

        if (offer.getStatusCode() == 200){
            connectToMongo.removeObject("admin", "cpa_programs", "_id", offer.path("offer.offer_id"));
        }
    }


    @Test(description = "User with type Sales and (level == READ) can create offer with own advertiser")
    public void salesReadOfferOwn() {
    // Generate Data
        connectToMongo
                .updateAdvertiserInMongo("_id", advertiser2.id(), "manager", salesUser.id());
        connectToMongo
                .updateAdvertiserInCentralMongo("_id", advertiser2.id(), "manager_id", salesUser.id());
        connectToMongo
                .updateUserInCentralMongo("_id", salesUser.id() , ENTITY_ADVERTISER_LEVEL, READ);

    // Validation Assert
        Response offer = offerApiService
                .createOffer(generateOfferWithReqFields(advertiser2.id()), salesUser.apiKey())
                    .shouldHave(statusCode(200))
                    .shouldHave(bodyField("offer.advertiser", equalTo(advertiser2.id())))
                    .getResponse();

    // Clean data
        if (offer.getStatusCode() == 200){
            connectToMongo.removeObject("admin", "cpa_programs", "_id", offer.path("offer.offer_id"));
        }
    }


    @Test(description = "User with type Sales and (level == DENY) can create offer with own advertiser")
    public void salesDenyOfferOwn() {
    // Generate Data
        connectToMongo
                .updateAdvertiserInMongo("_id", advertiser2.id(), "manager", salesUser.id());
        connectToMongo
                .updateAdvertiserInCentralMongo("_id", advertiser2.id(), "manager_id", salesUser.id());
        connectToMongo
                .updateUserInCentralMongo("_id", salesUser.id() , ENTITY_ADVERTISER_LEVEL, DENY);

    // Validation Assert
        Response offer = offerApiService
                .createOffer(generateOfferWithReqFields(advertiser2.id()), salesUser.apiKey())
                    .shouldHave(statusCode(200))
                    .shouldHave(bodyField("offer.advertiser", equalTo(advertiser2.id())))
                    .getResponse();

    // Clean data
        if (offer.getStatusCode() == 200){
            connectToMongo.removeObject("admin", "cpa_programs", "_id", offer.path("offer.offer_id"));
        }
    }


    @Test(description = "User with type Sales and (level == DENY/READ) can create offer with advertiser in exception write")
    public void salesDenyReadOfferExceptionWrite() {
        for (String level : Arrays.asList(DENY, READ)){
    // Generate Data
        connectToMongo
                    .updateUserInCentralMongo("_id", salesUser.id(), ENTITY_ADVERTISER_LEVEL, level);
        connectToMongo
                .updateUserInCentralMongoAddToSet("_id", salesUser.id(), "scopes.users.entity-advertiser.exceptions.strings.write", advertiser1.id());

    // Validation Assert
        Response offer = offerApiService
                .createOffer(generateOfferWithReqFields(advertiser1.id()), salesUser.apiKey())
                    .shouldHave(statusCode(200))
                    .shouldHave(bodyField("offer.advertiser", equalTo(advertiser1.id())))
                    .getResponse();

    // Clean data
        connectToMongo
                .updateUserInCentralMongoUnset("_id", salesUser.id(), "scopes.users.entity-advertiser.exceptions.strings.write");

            if (offer.getStatusCode() == 200){
                connectToMongo.removeObject("admin", "cpa_programs", "_id", offer.path("offer.offer_id"));
            }
        }
    }



    @Negative
    @Test(description = "User with type Administrator and (level == READ) can't create offer")
    public void adminReadOffer() {
        connectToMongo
                .updateUserInCentralMongo("_id", adminUser.id() , ENTITY_ADVERTISER_LEVEL, READ);

        offerApiService
                .createOffer(generateOfferWithReqFields(advertiser1.id()), adminUser.apiKey())
                    .shouldHave(statusCode(403));
    }


    @Test(description = "User with type Administrator and (level == DENY) can't create offer")
    public void adminDenyOffer() {
        connectToMongo
                .updateUserInCentralMongo("_id", adminUser.id() , ENTITY_ADVERTISER_LEVEL, DENY);

        offerApiService
                .createOffer(generateOfferWithReqFields(advertiser1.id()), adminUser.apiKey())
                    .shouldHave(statusCode(403));
    }


    @Test(description = "User with type Affiliate and (level == READ) can't create offer")
    public void affiliateReadOffer() {
        connectToMongo
                .updateUserInCentralMongo("_id", affiliateUser.id() , ENTITY_ADVERTISER_LEVEL, READ);

        offerApiService
                .createOffer(generateOfferWithReqFields(advertiser1.id()), affiliateUser.apiKey())
                    .shouldHave(statusCode(403));
    }


    @Test(description = "User with type Affiliate and (level == WRITE) can't create offer")
    public void affiliateDenyOffer() {
        connectToMongo
                .updateUserInCentralMongo("_id", affiliateUser.id() , ENTITY_ADVERTISER_LEVEL, DENY);

        offerApiService
                .createOffer(generateOfferWithReqFields(advertiser1.id()), affiliateUser.apiKey())
                    .shouldHave(statusCode(403));
    }


    @Test(description = "User with type Sales and (level == READ) can't create offer")
    public void salesReadOffer() {
        connectToMongo
                .updateUserInCentralMongo("_id", salesUser.id() , ENTITY_ADVERTISER_LEVEL, READ);

        offerApiService
                .createOffer(generateOfferWithReqFields(advertiser1.id()), salesUser.apiKey())
                   .shouldHave(statusCode(403));
    }


    @Test(description = "User with type Sales and (level == DENY) can't create offer")
    public void salesDenyOffer() {
        connectToMongo
                .updateUserInCentralMongo("_id", salesUser.id() , ENTITY_ADVERTISER_LEVEL, DENY);

        offerApiService
                .createOffer(generateOfferWithReqFields(advertiser1.id()), salesUser.apiKey())
                  .shouldHave(statusCode(403));
    }


    @Test(description = "User with type Sales and (level == DENY) can't create offer with advertiser in exception read")
    public void salesDenyOfferExceptionRead() {
    // Generate Data
        connectToMongo
                .updateUserInCentralMongo("_id", salesUser.id(), ENTITY_ADVERTISER_LEVEL, DENY);
        connectToMongo
                .updateUserInCentralMongoAddToSet("_id", salesUser.id(), "scopes.users.entity-advertiser.exceptions.strings.read", advertiser1.id());

    // Validation Assert
        offerApiService
                .createOffer(generateOfferWithReqFields(advertiser1.id()), salesUser.apiKey())
                .shouldHave(statusCode(403));

    // Clean data
        connectToMongo
                .updateUserInCentralMongoUnset("_id", salesUser.id(), "scopes.users.entity-advertiser.exceptions.strings.read");
    }


    @Test(description = "User with type Sales and (level == READ) can't create offer with advertiser in exception deny")
    public void salesReadOfferExceptionDeny() {
    // Generate Data
        connectToMongo
                .updateUserInCentralMongo("_id", salesUser.id(), ENTITY_ADVERTISER_LEVEL, READ);
        connectToMongo
                .updateUserInCentralMongoAddToSet("_id", salesUser.id(), "scopes.users.entity-advertiser.exceptions.strings.deny", advertiser1.id());

    // Validation Assert
        offerApiService
                .createOffer(generateOfferWithReqFields(advertiser1.id()), salesUser.apiKey())
                .shouldHave(statusCode(403));

    // Clean data
        connectToMongo
                .updateUserInCentralMongoUnset("_id", salesUser.id(), "scopes.users.entity-advertiser.exceptions.strings.deny");
    }


    @Test(description = "User with type Admin/Affiliate and (level == DENY/READ) can't create offer with advertiser in exception write")
    public void adminAffiliateDenyReadOfferExceptionWrite() {
        for (User user : Arrays.asList(adminUser, affiliateUser)) {
            for (String level : Arrays.asList(DENY, READ)) {
    // Generate Data
                connectToMongo
                        .updateUserInCentralMongo("_id", user.id(), ENTITY_ADVERTISER_LEVEL, level);
                connectToMongo
                        .updateUserInCentralMongoAddToSet("_id", user.id(), "scopes.users.entity-advertiser.exceptions.strings.write", advertiser1.id());

    // Validation Assert
                offerApiService
                        .createOffer(generateOfferWithReqFields(advertiser1.id()), user.apiKey())
                        .shouldHave(statusCode(403));

    // Clean data
                connectToMongo
                        .updateUserInCentralMongoUnset("_id", user.id(), "scopes.users.entity-advertiser.exceptions.strings.write");
            }
        }
    }



    @AfterClass
    public void removeData(){
        connectToMongo.removeObject("admin", "users", "_id", adminUser.id());
        connectToMongo.removeObject("admin", "users", "_id", affiliateUser.id());
        connectToMongo.removeObject("admin", "users", "_id", salesUser.id());
        connectToMongo.removeObject("admin", "suppliers", "_id", advertiser1.id());
        connectToMongo.removeObject("admin", "suppliers", "_id", advertiser2.id());
        connectToMongo.closeConnection();
    }

}