package com.affise.tests;

import com.affise.api.annotations.Positive;
import com.affise.api.database.ConnectToMongo;
import com.affise.api.database.ConnectToMySql;
import com.affise.api.payloads.Go.Affiliates.AffiliateGo;
import com.affise.api.payloads.Php.Affiliates.Affiliates;
import com.affise.api.payloads.Php.Afiliate;
import com.affise.api.services.AffiliateApiService;
import com.affise.api.services.OfferApiService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.core.DockerClientBuilder;
import io.restassured.response.Response;
import lombok.SneakyThrows;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static com.affise.api.conditions.Conditions.*;
import static com.affise.api.config.Config.getConfig;
import static com.affise.api.generatedata.Generations.*;
import static com.affise.api.payloads.Go.Affiliates.AffiliateGo.showAllProps;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.emptyOrNullString;
import static org.hamcrest.Matchers.not;
import static org.testng.Assert.assertTrue;


public class Affiliate {

    private final AffiliateApiService affiliateApiService = new AffiliateApiService();
    private final OfferApiService offerApiService = new OfferApiService();
    private final ConnectToMongo connectToMongo = new ConnectToMongo();
    private final ConnectToMySql connectToMySql = new ConnectToMySql();
    private final DockerClient dockerClient = DockerClientBuilder.getInstance().build();



    @Positive @SneakyThrows
    @Test(description = "User can create affiliate with required fields", priority = 1)
    public void createAffiliatePhpMap() {
    // Generate data
        Map<String, Object> affiliate = new HashMap<>();
        affiliate.put("email", generateEmail());
        affiliate.put("password", generatePassword());
        affiliate.put("login", generateFirstName());
        affiliate.put("status", "active");

    // Validation assert
        Response partner = affiliateApiService.createAffiliate(affiliate)
                .shouldHave(statusCode(200))
                .shouldHave(bodyField("partner.id", not(emptyOrNullString())))
                .shouldHave(bodyField("partner.email", equalTo(affiliate.get("email"))))
                .shouldHave(bodyField("partner.status", equalTo(affiliate.get("status"))))
                .shouldHave(bodyField("partner.login", equalTo(affiliate.get("login"))))
                .shouldHave(bodyContainsAllFields("partner", affiliate)).getResponse();

    // Deserialize to verify existence
        Afiliate affiliateObj = new ObjectMapper().readValue(jsonNode(partner.asString(), "partner"), Afiliate.class);
        connectToMySql.existsInMySql(affiliateObj.id());
        connectToMongo.existsAffiliateInMongo(affiliateObj.id());
        connectToMongo.existsAffiliateInCentralMongo(affiliateObj.id());

    // Clear data
        connectToMongo.removeAffiliateById(affiliateObj.id());
        connectToMySql.deleteAffiliateFromMySql(affiliateObj.id());
        dockerClient.restartContainerCmd("affisedev-memcached").exec();
    }


    @Positive @SneakyThrows
    @Test(description = "User can create affiliate with required fields")
    public void createAffiliateGoObject(){

        AffiliateGo request = new AffiliateGo()
                .email(generateEmail())
                .password(generatePassword())
                .name(generateFirstName())
                .affiliateManagerId("507f1f77bcf86cd799439013")
                .status("active");

        AffiliateGo response = affiliateApiService.createGoAffiliate(request, getConfig().token())
                .shouldHave(statusCode(200))
                .shouldHave(bodyField("email", equalTo(request.email())))
                .shouldHave(bodyField("name", equalTo(request.name())))
                .shouldHave(bodyField("status", equalTo(request.status())))
                .shouldHave(bodyField("affiliate_manager_id", equalTo(request.affiliateManagerId())))
                .asPojo(AffiliateGo.class);
    }


    @Positive @SneakyThrows
    @Test(description = "User can create affiliate with required fields")
    public void createAffiliateGoString(){

        AffiliateGo request = new AffiliateGo()
                .email(generateEmail())
                .password(generatePassword())
                .name(generateFirstName())
                .affiliateManagerId("507f1f77bcf86cd799439013")
                .status("active");

        String json = new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(request);

        AffiliateGo response = affiliateApiService.createGoAffiliate(json, getConfig().token())
                .getResponse().as(AffiliateGo.class);

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
                .shouldHave(bodyContainsAllFields("partner", affiliate)).asPojo(Affiliates.class);

    // Clear data
        connectToMongo.removeAffiliateById(partner.partner().id());
    }


    @SneakyThrows
    @Test(description = "Can create affiliate with required fields")
    public void createAffiliateJsonSimple() {
        JSONObject body = new JSONObject();
        body.put("email", generateEmail());
        body.put("password", generatePassword());
        body.put("name", generateFirstName());
        body.put("affiliate_manager_id", "507f1f77bcf86cd799439013");
        body.put("status", "active");

        JSONObject response = (JSONObject) new JSONParser().parse(affiliateApiService.createGoAffiliate(body, getConfig().token())
                .shouldHave(statusCode(200)).getResponse().asString());

        assertThat(body.get("email"), equalTo(response.get("email")));
        assertThat(body.get("name"), equalTo(response.get("name")));
        assertThat(body.get("status"), equalTo(response.get("status")));
        assertThat(body.get("affiliate_manager_id"), equalTo(response.get("affiliate_manager_id")));
    }



    @AfterClass
    public void cleanUp(){
        connectToMongo.closeConnection();
        connectToMySql.closeConnection();
    }

}