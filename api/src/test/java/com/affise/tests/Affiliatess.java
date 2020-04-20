package com.affise.tests;

import com.affise.api.annotations.*;
import com.affise.api.database.*;
import com.affise.api.payloads.*;
import com.affise.api.payloads.Affiliates.Affiliates;
import com.affise.api.payloads.Go.Offers.*;
import com.affise.api.services.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.core.DockerClientBuilder;
import io.restassured.response.Response;
import lombok.*;
import org.testng.annotations.*;
import java.util.*;

import static com.affise.api.conditions.Conditions.*;
import static com.affise.api.config.Config.getConfig;
import static com.affise.api.generatedata.Generations.*;
import static com.affise.api.payloads.AffiliateGo.showAllProps;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.emptyOrNullString;
import static org.hamcrest.Matchers.not;
import static org.testng.Assert.assertTrue;


public class Affiliatess {

    private final AffiliateApiService affiliateApiService = new AffiliateApiService();
    private final OfferApiService offerApiService = new OfferApiService();
    private final ConnectToMongo connectToMongo = setUp();
    private final ConnectToMySql connectToMySql = new ConnectToMySql();
    private final DockerClient dockerClient = DockerClientBuilder.getInstance().build();



    @BeforeClass
    public ConnectToMongo setUp(){
        return new ConnectToMongo();
    }


    @Positive @SneakyThrows
    @Test(description = "User can create affiliate with required fields", priority = 1)
    public void createAffiliatePhp() {

    // Generate data
        Map<String, Object> affiliate = new HashMap<>();
        affiliate.put("email", generateEmail());
        affiliate.put("password", generatePassword());
        affiliate.put("login", generateFirstName());
        affiliate.put("status", "active");
        affiliate.put("custom_fields[1]", "skype");

    // Validation assert
        Response partner = affiliateApiService.createAffiliate(affiliate)
                .shouldHave(statusCode(200))
                .shouldHave(bodyField("partner.id", not(emptyOrNullString())))
                .shouldHave(bodyField("partner.email", equalTo(affiliate.get("email"))))
                .shouldHave(bodyContainsAllFields("partner", affiliate)).getResponse();

        Affiliate affiliateObj = new ObjectMapper().readValue(jsonNode(partner.asString(), "partner"), Affiliate.class);

    // Clean data
        connectToMongo.removeAffiliateById(affiliateObj.id());
        connectToMySql.deleteAffiliateFromMySql(affiliateObj.id());
        dockerClient.restartContainerCmd("affisedev-memcached").exec();
    }


    @Positive @SneakyThrows
    @Test(description = "User can create affiliate with required fields")
    public void createAffiliateGo(){

        AffiliateGo request = new AffiliateGo()
                .email(generateEmail())
                .password(generatePassword())
                .name(generateFirstName())
                .affiliateManagerId("507f1f77bcf86cd799439013")
                .status("active");

        String json = new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(request);

        AffiliateGo response = affiliateApiService.createGoAffiliate(request, getConfig().token())
                .asPojo(AffiliateGo.class);

        assertTrue(showAllProps(request, response));
    }


    @Test
    public void createAffiliateAsPojo(){

    // Generate data
        Map<String, Object> affiliate = new HashMap<>();
        affiliate.put("email", generateEmail());
        affiliate.put("password", generatePassword());
        affiliate.put("login", generateFirstName());
        affiliate.put("status", "active");
        affiliate.put("custom_fields[1]", "skype");

    // Validation assert
        Affiliates partner = affiliateApiService.createAffiliate(affiliate)
                .shouldHave(statusCode(200))
                .shouldHave(bodyField("partner.id", not(emptyOrNullString())))
                .shouldHave(bodyField("partner.email", equalTo(affiliate.get("email"))))
                .shouldHave(bodyContainsAllFields("partner", affiliate)).asPojo(Affiliates.class);

    // Clean data
        connectToMongo.removeAffiliateById(partner.partner().id());
    }


    @Positive @SneakyThrows
    @Test(description = "User can create offer")
    public void createOfferGo(){

        OfferGo request = new OfferGo()
                .title(generateFirstName())
                .trackingUrl(generateUrl())
                .advertiserId("5e9587705a27262185c64cf5")
                .categories(Arrays.asList("health", "gambling", "marketplace"))
                .allowedIps(generateList("192.168.100.11", "216.58.215.110"))
                .description(new Description().en("description").ru("информация"))
                .landings(generateList(
                        new LandingsItem()
                                .previewUrl("https://google.com").title("Search").trackingUrl("dev.affise.com").type("landing"),
                        new LandingsItem()
                                .previewUrl("https://yandex.ru").title("Search").trackingUrl("dev.affise.com").type("prelanding")))
                .payouts(Arrays.asList(
                        new PayoutsItem()
                                .affiliates(Arrays.asList(3, 4)).goalTitle("hello").goalValue("world")
                                .currency("USD").paymentType("percent").total(12).payouts(3)))
                .caps(
                        new Caps()
                                .conversionStatus(Arrays.asList("confirmed", "pending", "declined", "hold", "not_found"))
                                .currency("USD").timezone("UTC")
                                .caps(Arrays.asList(
                                        new CapsItem()
                                                .affiliates(Collections.emptyList()).affiliateType("all").timeframe("all").type("conversions")
                                                .goalType("exact").goals(Arrays.asList("world")).resetToValue(0).value(100).isRemaining(false))));

        OfferGo response = offerApiService.createGoOffer(request, getConfig().token())
                .asPojo(OfferGo.class);
    }


    @Test
    public void connectMySql() {
    }


    @Test
    public void connectMongo(){
    }



    @AfterClass
    public void cleanUp(){
        connectToMongo.closeConnection();
        connectToMySql.closeConnection();
    }

}