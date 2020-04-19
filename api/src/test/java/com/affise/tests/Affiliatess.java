package com.affise.tests;

import com.affise.api.annotations.Positive;
import com.affise.api.database.ConnectToMongo;
import com.affise.api.database.ConnectToMySql;
import com.affise.api.payloads.Affiliate;
import com.affise.api.payloads.AffiliateGoApi;
import com.affise.api.payloads.Affiliates.Affiliates;
import com.affise.api.services.AffiliateApiService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.core.DockerClientBuilder;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import lombok.SneakyThrows;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static com.affise.api.conditions.Conditions.*;
import static com.affise.api.generatedata.Generations.*;
import static com.affise.api.payloads.AffiliateGoApi.showAllProps;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.emptyOrNullString;
import static org.hamcrest.Matchers.not;
import static org.testng.Assert.assertTrue;


public class Affiliatess {

    private final AffiliateApiService affiliateApiService = new AffiliateApiService();
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
    public void createAffiliateGoapi(){

        AffiliateGoApi request = new AffiliateGoApi()
                .email(generateEmail())
                .password(generatePassword())
                .name(generateFirstName())
                .affiliateManagerId("507f1f77bcf86cd799439013")
                .subAccount1("sub1")
                .subAccount2("sub2")
                .subAccount1Except(true)
                .subAccount2Except(false)
                .status("active");

        String json = new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(request);

        AffiliateGoApi response = RestAssured
                .given()
                .filter(new ResponseLoggingFilter())
                .filter(new RequestLoggingFilter())
                .queryParam("client_id", "3")
                .header("Authorization", "Bearer 2JQxFCe0slViLKXyG91uSa4gqoPb7LOI3M1HHqZ0NQ0.Ck38pmgYHdpb-8XjgWEW-nZ5bBvC5lB9Y2Gt6oZTxC0")
                .contentType(ContentType.JSON)
                .body(request)
                .when().post("http://localhost:50603/4.0/affiliates").as(AffiliateGoApi.class);
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