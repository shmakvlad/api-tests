package com.affise.tests.Offers;

import com.affise.api.annotations.Negative;
import com.affise.api.annotations.Positive;
import com.affise.api.database.ConnectToMongo;
import com.affise.api.payloads.Php.Advertiser;
import com.affise.api.payloads.Php.Offers.Offer;
import com.affise.api.payloads.Php.User;
import com.affise.api.services.AdvertiserApiService;
import com.affise.api.services.OfferApiService;
import com.affise.api.services.UserApiService;
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.core.DockerClientBuilder;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;

import java.util.Arrays;

import static com.affise.api.conditions.Conditions.bodyField;
import static com.affise.api.conditions.Conditions.statusCode;
import static com.affise.api.constans.Constans.PermissionsLevel.ENTITY_ADVERTISER_LEVEL;
import static com.affise.api.constans.Constans.UserPermissionsLevel.*;
import static com.affise.api.constans.Constans.UserType.*;
import static com.affise.api.generatedata.GenerateAdvertiser.getNewAdvertiser;
import static com.affise.api.generatedata.GenerateOffer.getNewOffer;
import static com.affise.api.generatedata.GenerateUser.getNewUser;
import static org.hamcrest.Matchers.*;

public class GetOffer {

    private final AdvertiserApiService advertiserApiService = new AdvertiserApiService();
    private final UserApiService userApiService = new UserApiService();
    private final ConnectToMongo connectToMongo = new ConnectToMongo();
    private final OfferApiService offerApiService = new OfferApiService();
    private final DockerClient dockerClient = DockerClientBuilder.getInstance().build();

    private final User adminUser = getNewUser(ROLE_ADMIN);
    private final User affiliateUser = getNewUser(ROLE_MAN_AFFILIATE);
    private final User salesUser = getNewUser(ROLE_MAN_SALES);

    private final Advertiser advertiser1 = getNewAdvertiser(adminUser.id());
    private final Advertiser advertiser2 = getNewAdvertiser(affiliateUser.id());
    private final Advertiser advertiser3 = getNewAdvertiser(salesUser.id());
    private final Advertiser advertiser4 = getNewAdvertiser();

    private final Offer offer1 = getNewOffer(advertiser1.id());
    private final Offer offer2 = getNewOffer(advertiser2.id());
    private final Offer offer3 = getNewOffer(advertiser3.id());
    private final Offer offer4 = getNewOffer(advertiser4.id());



    @Positive
    @Test(description = "User with level == WRITE can get offer list")
    public void userWriteGetOfferList() {
        for (User user : Arrays.asList(adminUser, affiliateUser, salesUser)) {
            connectToMongo
                    .updateUserInCentralMongo("_id", user.id(), ENTITY_ADVERTISER_LEVEL, WRITE);

            offerApiService
                    .getListOffers(user.apiKey())
                    .shouldHave(statusCode(200))
                    .shouldHave(bodyField("offers.id", hasItems(offer1.id(), offer2.id(), offer3.id(), offer4.id())));
        }
        dockerClient.restartContainerCmd("affisedev-memcached").exec();
    }


    @Test(description = "User with level == READ can get offer list")
    public void userReadGetOfferList() {
        for (User user : Arrays.asList(adminUser, affiliateUser, salesUser)) {
            connectToMongo
                    .updateUserInCentralMongo("_id", user.id(), ENTITY_ADVERTISER_LEVEL, READ);

            offerApiService
                    .getListOffers(user.apiKey())
                    .shouldHave(statusCode(200))
                    .shouldHave(bodyField("offers.id", hasItems(offer1.id(), offer2.id(), offer3.id(), offer4.id())));
        }
        dockerClient.restartContainerCmd("affisedev-memcached").exec();
    }

    // Bug
    @Test(description = "[bug] Sales with level == DENY and without own advertiser get empty array in offers list")
    public void salesDenyGetOfferListEmpty(){
        User sales = getNewUser(ROLE_MAN_SALES);

        connectToMongo
                .updateUserInCentralMongo("_id", sales.id(), ENTITY_ADVERTISER_LEVEL, DENY);

        offerApiService
                .getListOffers(sales.apiKey())
                .shouldHave(statusCode(200))
                .shouldHave(bodyField("offers.id", not(hasItems(offer1.id(), offer2.id(), offer3.id(), offer4.id()))));

        connectToMongo.removeObject("admin", "users", "_id", sales.id());
        dockerClient.restartContainerCmd("affisedev-memcached").exec();
    }


    @Test(description = "Sales with level == DENY can get own offers in list")
    public void salesDenyGetOfferList(){
        connectToMongo
                .updateUserInCentralMongo("_id", salesUser.id(), ENTITY_ADVERTISER_LEVEL, DENY);

        offerApiService
                .getListOffers(salesUser.apiKey())
                .shouldHave(statusCode(200))
                .shouldHave(bodyField("offers.id", hasItems(offer3.id())))
                .shouldHave(bodyField("offers.id", not(hasItems(offer1.id(), offer2.id(), offer4.id()))));

        dockerClient.restartContainerCmd("affisedev-memcached").exec();
    }


    @Test(description = "User with level == READ and exception == DENY can't get offer in list")
    public void userReadExceptionDeny() {
        for (User user : Arrays.asList(adminUser, affiliateUser, salesUser)) {
        // Generate Data
            connectToMongo
                    .updateUserInCentralMongo("_id", user.id(), ENTITY_ADVERTISER_LEVEL, READ);
            connectToMongo
                    .updateUserInCentralMongoAddToSet("_id", user.id(), "scopes.users.entity-advertiser.exceptions.strings.deny", advertiser1.id());

        // Validation Assert
            offerApiService
                        .getListOffers(user.apiKey())
                        .shouldHave(statusCode(200))
                        .shouldHave(bodyField("offers.id", hasItems(offer2.id(), offer3.id(), offer4.id())))
                        .shouldHave(bodyField("offers.id", not(hasItems(offer1.id()))));

        // Clean data
            connectToMongo
                    .updateUserInCentralMongoUnset("_id", user.id(), "scopes.users.entity-advertiser.exceptions.strings.deny");
        }
        dockerClient.restartContainerCmd("affisedev-memcached").exec();
    }


    @Test(description = "User with level == WRITE and exception == DENY can't get offer in list")
    public void userWriteExceptionDeny() {
        for (User user : Arrays.asList(adminUser, affiliateUser, salesUser)) {
        // Generate Data
            connectToMongo
                    .updateUserInCentralMongo("_id", user.id(), ENTITY_ADVERTISER_LEVEL, WRITE);
            connectToMongo
                    .updateUserInCentralMongoAddToSet("_id", user.id(), "scopes.users.entity-advertiser.exceptions.strings.deny", advertiser1.id());

        // Validation Assert
            offerApiService
                    .getListOffers(user.apiKey())
                    .shouldHave(statusCode(200))
                    .shouldHave(bodyField("offers.id", hasItems(offer2.id(), offer3.id(), offer4.id())))
                    .shouldHave(bodyField("offers.id", not(hasItems(offer1.id()))));

        // Clean data
            connectToMongo
                    .updateUserInCentralMongoUnset("_id", user.id(), "scopes.users.entity-advertiser.exceptions.strings.deny");
        }
        dockerClient.restartContainerCmd("affisedev-memcached").exec();
    }


    @Test(description = "User with level == DENY and exception == READ/WRITE can get offer in list")
    public void userDenyExceptionReadWrite() {
        for (User user : Arrays.asList(adminUser, affiliateUser, salesUser)) {
        // Generate Data
            connectToMongo
                    .updateUserInCentralMongo("_id", user.id(), ENTITY_ADVERTISER_LEVEL, DENY);
            connectToMongo
                    .updateUserInCentralMongoAddToSet("_id", user.id(),
                            "scopes.users.entity-advertiser.exceptions.strings.read", advertiser1.id());
            connectToMongo
                    .updateUserInCentralMongoAddToSet("_id", user.id(),
                            "scopes.users.entity-advertiser.exceptions.strings.write", advertiser2.id());

        // Validation Assert
            offerApiService
                    .getListOffers(user.apiKey())
                    .shouldHave(statusCode(200))
                    .shouldHave(bodyField("offers.id", hasItems(offer2.id(), offer1.id())))
                    .shouldHave(bodyField("offers.id", not(hasItems(offer4.id()))));

        // Clean data
            connectToMongo
                    .updateUserInCentralMongoUnset("_id", user.id(),
                            "scopes.users.entity-advertiser.exceptions.strings.read");
            connectToMongo
                    .updateUserInCentralMongoUnset("_id", user.id(),
                            "scopes.users.entity-advertiser.exceptions.strings.write");
        }
        dockerClient.restartContainerCmd("affisedev-memcached").exec();
    }



    @Negative
    @Test(description = "Admin with level == DENY can't see offers ( also can't get own offers in list )")
    public void adminDenyGetOfferList(){
        connectToMongo
                .updateUserInCentralMongo("_id", adminUser.id(), ENTITY_ADVERTISER_LEVEL, DENY);

        offerApiService
                .getListOffers(adminUser.apiKey())
                .shouldHave(statusCode(403));

        dockerClient.restartContainerCmd("affisedev-memcached").exec();
    }


    @Test(description = "Affiliate with level == DENY can't see offers ( also can't get own offers in list )")
    public void affiliateDenyGetOfferList(){
        connectToMongo
                .updateUserInCentralMongo("_id", affiliateUser.id(), ENTITY_ADVERTISER_LEVEL, DENY);

        offerApiService
                .getListOffers(affiliateUser.apiKey())
                .shouldHave(statusCode(403));

        dockerClient.restartContainerCmd("affisedev-memcached").exec();
    }



    @AfterClass
    public void removeData(){
        connectToMongo.removeObject("admin", "users", "_id", adminUser.id());
        connectToMongo.removeObject("admin", "users", "_id", affiliateUser.id());
        connectToMongo.removeObject("admin", "users", "_id", salesUser.id());

        connectToMongo.removeObject("admin", "suppliers", "_id", advertiser1.id());
        connectToMongo.removeObject("admin", "suppliers", "_id", advertiser2.id());
        connectToMongo.removeObject("admin", "suppliers", "_id", advertiser3.id());
        connectToMongo.removeObject("admin", "suppliers", "_id", advertiser4.id());

        connectToMongo.removeObject("admin", "cpa_programs", "_id", offer1.offerId());
        connectToMongo.removeObject("admin", "cpa_programs", "_id", offer2.offerId());
        connectToMongo.removeObject("admin", "cpa_programs", "_id", offer3.offerId());
        connectToMongo.removeObject("admin", "cpa_programs", "_id", offer4.offerId());

        connectToMongo.closeConnection();
    }

}
