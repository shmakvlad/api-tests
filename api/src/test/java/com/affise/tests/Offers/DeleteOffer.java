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

import static com.affise.api.conditions.Conditions.statusCode;
import static com.affise.api.constans.Constans.PermissionsLevel.ENTITY_ADVERTISER_LEVEL;
import static com.affise.api.constans.Constans.UserPermissionsLevel.*;
import static com.affise.api.constans.Constans.UserType.*;
import static com.affise.api.generatedata.GenerateAdvertiser.getNewAdvertiser;
import static com.affise.api.generatedata.GenerateOffer.getNewOffer;
import static com.affise.api.generatedata.GenerateUser.getNewUser;

public class DeleteOffer {

    private final AdvertiserApiService advertiserApiService = new AdvertiserApiService();
    private final UserApiService userApiService = new UserApiService();
    private final OfferApiService offerApiService = new OfferApiService();
    private final ConnectToMongo connectToMongo = new ConnectToMongo();
    private final DockerClient dockerClient = DockerClientBuilder.getInstance().build();

    private final User adminUser = getNewUser(ROLE_ADMIN);
    private final User affiliateUser = getNewUser(ROLE_MAN_AFFILIATE);
    private final User salesUser = getNewUser(ROLE_MAN_SALES);

    private final Advertiser advertiser1 = getNewAdvertiser(adminUser.id());
    private final Advertiser advertiser2 = getNewAdvertiser(affiliateUser.id());
    private final Advertiser advertiser3 = getNewAdvertiser(salesUser.id());
    private final Advertiser advertiser4 = getNewAdvertiser();



    @Positive
    @Test(description = "User with level == WRITE can delete offer")
    public void userWriteDeleteOffer() {
        for (User user : Arrays.asList(adminUser, affiliateUser, salesUser)) {

            Offer offer = getNewOffer(advertiser4.id());
            connectToMongo
                    .updateUserInCentralMongo("_id", user.id(), ENTITY_ADVERTISER_LEVEL, WRITE);
            Integer statusCode = offerApiService
                    .deleteOffer(offer.id(), user.apiKey())
                    .shouldHave(statusCode(200)).getStatusCode();

            if (statusCode != 200) {
                connectToMongo
                        .removeObject("admin", "cpa_programs", "_id", offer.offerId());
            }
        }
        dockerClient.restartContainerCmd("affisedev-memcached").exec();
    }


    @Test(description = "Sales with level == DENY/READ can delete own offer")
    public void salesDeleteOwnOffer() {
        for (String level : Arrays.asList(READ,DENY)) {

            Offer offer = getNewOffer(advertiser3.id());
            connectToMongo
                    .updateUserInCentralMongo("_id", salesUser.id(), ENTITY_ADVERTISER_LEVEL, level);
            Integer statusCode = offerApiService
                    .deleteOffer(offer.id(), salesUser.apiKey())
                    .shouldHave(statusCode(200)).getStatusCode();

            if (statusCode != 200) {
                connectToMongo
                        .removeObject("admin", "cpa_programs", "_id", offer.offerId());
            }
        }
        dockerClient.restartContainerCmd("affisedev-memcached").exec();
    }


    @Test(description = "User with level == DENY and exception == WRITE can delete offer")
    public void userDenyExceptionWrite() {
        for (User user : Arrays.asList(adminUser, affiliateUser, salesUser)) {

        // Generate Data
            Offer offer  = getNewOffer(advertiser4.id());
            connectToMongo
                    .updateUserInCentralMongo("_id", user.id(), ENTITY_ADVERTISER_LEVEL, DENY);
            connectToMongo
                    .updateUserInCentralMongoAddToSet("_id", user.id(), "scopes.users.entity-advertiser.exceptions.strings.write", advertiser4.id());

        // Validation Assert
            Integer statusCode = offerApiService
                    .deleteOffer(offer.id(), user.apiKey())
                    .shouldHave(statusCode(200)).getStatusCode();

        // Clean data
            connectToMongo
                    .updateUserInCentralMongoUnset("_id", user.id(), "scopes.users.entity-advertiser.exceptions.strings.write");

            if (statusCode != 200) {
                connectToMongo
                        .removeObject("admin", "cpa_programs", "_id", offer.offerId());
            }
        }
        dockerClient.restartContainerCmd("affisedev-memcached").exec();
    }



    @Negative
    @Test(description = "User with level == READ can't delete offer")
    public void userReadDeleteOffer() {
        for (User user : Arrays.asList(adminUser, affiliateUser, salesUser)) {

            Offer offer = getNewOffer(advertiser4.id());
            connectToMongo
                    .updateUserInCentralMongo("_id", user.id(), ENTITY_ADVERTISER_LEVEL, READ);
            offerApiService
                    .deleteOffer(offer.id(), user.apiKey())
                    .shouldHave(statusCode(403));
            connectToMongo
                    .removeObject("admin", "cpa_programs", "_id", offer.offerId());
        }
        dockerClient.restartContainerCmd("affisedev-memcached").exec();
    }


    @Test(description = "User with level == DENY can't delete offer")
    public void userDenyDeleteOffer() {
        for (User user : Arrays.asList(adminUser, affiliateUser, salesUser)) {

            Offer offer = getNewOffer(advertiser4.id());
            connectToMongo
                    .updateUserInCentralMongo("_id", user.id(), ENTITY_ADVERTISER_LEVEL, DENY);
            offerApiService
                    .deleteOffer(offer.id(), user.apiKey())
                    .shouldHave(statusCode(403));
            connectToMongo
                    .removeObject("admin", "cpa_programs", "_id", offer.offerId());
        }
        dockerClient.restartContainerCmd("affisedev-memcached").exec();
    }


    @Test(description = "Affiliate with level == DENY/READ can't delete own offer")
    public void affiliateDeleteOwnOffer() {
        for (String level : Arrays.asList(READ,DENY)) {

            Offer offer = getNewOffer(advertiser2.id());
            connectToMongo
                    .updateUserInCentralMongo("_id", affiliateUser.id(), ENTITY_ADVERTISER_LEVEL, level);
            offerApiService
                    .deleteOffer(offer.id(), affiliateUser.apiKey())
                    .shouldHave(statusCode(403));
            connectToMongo
                    .removeObject("admin", "cpa_programs", "_id", offer.offerId());
        }
        dockerClient.restartContainerCmd("affisedev-memcached").exec();
    }


    @Test(description = "Admin with level == DENY/READ can't delete own offer")
    public void adminDeleteOwnOffer() {
        for (String level : Arrays.asList(READ,DENY)) {

            Offer offer = getNewOffer(advertiser1.id());
            connectToMongo
                    .updateUserInCentralMongo("_id", adminUser.id(), ENTITY_ADVERTISER_LEVEL, level);
            offerApiService
                    .deleteOffer(offer.id(), adminUser.apiKey())
                    .shouldHave(statusCode(403));
            connectToMongo
                    .removeObject("admin", "cpa_programs", "_id", offer.offerId());
        }
        dockerClient.restartContainerCmd("affisedev-memcached").exec();
    }


    @Test(description = "User with level == WRITE and exception == DENY can't delete offer")
    public void userWriteExceptionDeny() {
        for (User user : Arrays.asList(adminUser, affiliateUser, salesUser)) {

        // Generate Data
            Offer offer = getNewOffer(advertiser4.id());
            connectToMongo
                    .updateUserInCentralMongo("_id", user.id(), ENTITY_ADVERTISER_LEVEL, WRITE);
            connectToMongo
                    .updateUserInCentralMongoAddToSet("_id", user.id(), "scopes.users.entity-advertiser.exceptions.strings.deny", advertiser4.id());

        // Validation Assert
            offerApiService
                    .deleteOffer(offer.id(), user.apiKey())
                    .shouldHave(statusCode(403));

        // Clean data
            connectToMongo
                    .updateUserInCentralMongoUnset("_id", user.id(), "scopes.users.entity-advertiser.exceptions.strings.deny");
            connectToMongo
                    .removeObject("admin", "cpa_programs", "_id", offer.offerId());
        }
        dockerClient.restartContainerCmd("affisedev-memcached").exec();
    }


    @Test(description = "User with level == WRITE and exception == READ can't delete offer")
    public void userWriteExceptionRead() {
        for (User user : Arrays.asList(adminUser, affiliateUser, salesUser)) {

        // Generate Data
            Offer offer = getNewOffer(advertiser4.id());
            connectToMongo
                    .updateUserInCentralMongo("_id", user.id(), ENTITY_ADVERTISER_LEVEL, WRITE);
            connectToMongo
                    .updateUserInCentralMongoAddToSet("_id", user.id(), "scopes.users.entity-advertiser.exceptions.strings.read", advertiser4.id());

        // Validation Assert
            offerApiService
                    .deleteOffer(offer.id(), user.apiKey())
                    .shouldHave(statusCode(403));

        // Clean data
            connectToMongo
                    .updateUserInCentralMongoUnset("_id", user.id(), "scopes.users.entity-advertiser.exceptions.strings.read");
            connectToMongo
                    .removeObject("admin", "cpa_programs", "_id", offer.offerId());
        }
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

        connectToMongo.closeConnection();
    }

}
