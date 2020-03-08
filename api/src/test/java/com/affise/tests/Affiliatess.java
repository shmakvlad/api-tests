package com.affise.tests;

import com.affise.api.annotations.Positive;
import com.affise.api.payloads.Affiliate;
import com.affise.api.payloads.AffiliateGoApi;
import com.affise.api.services.AffiliateApiService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import lombok.SneakyThrows;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;
import com.affise.api.database.ConnectToMongo

import static com.affise.api.conditions.Conditions.*;
import static com.affise.api.generatedata.Generations.*;
import static com.affise.api.payloads.AffiliateGoApi.showAllProps;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.emptyOrNullString;
import static org.hamcrest.Matchers.not;
import static org.testng.Assert.assertTrue;


public class Affiliatess {

    private final AffiliateApiService affiliateApiService = new AffiliateApiService();

//    @BeforeClass
//    public void setUp() {
//        RestAssured.baseURI = host;
//    }

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
        removeAffiliateById(affiliateObj.id());
    }

//    @Positive
//    @Test(description = "Get Affiliate Object")
//    public void createAffiliatePhpAsSting() throws IOException {
//    // Generate data
//        Map<String, Object> affiliate = new HashMap<>();
//        affiliate.put("email", generateEmail());
//        affiliate.put("password", generatePassword());
//
//    // Validation assert
//        String json = affiliateApiService.createAffiliate(affiliate).getResponse().asString();
//        AffiliateResponse aff = new ObjectMapper().readValue(new ObjectMapper().readTree(json).get("partner").toString(), AffiliateResponse.class);
//    }

//    @Positive
//    @Test(description = "Get partner as pogo")
//    public void getAffiliateAsPogo() {
//        // Generate data
//        Map body = generateMap(email, password);
//
//        // Validation assert
//        Affiliate affiliate = affiliateApiService.createAffiliate(body)
//                .shouldHave(statusCode(200))
//                .asPojo(Affiliate.class);
//    }
//

    @Positive
    @SneakyThrows
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
                .header("Authorization", "Bearer r2zVIsVqOQCTKjb5sSsythrxTOsHmROEIq6zgBfCyh4.ywlE9oPNfZfo_gcGClyT8HKq8swfvBGi5RdRg89liKM")
                .contentType(ContentType.JSON)
                .body(request)
                .when().post("http://10.201.0.80:58990/4.0/affiliates").as(AffiliateGoApi.class);

        assertTrue(showAllProps(request, response));

    }
}