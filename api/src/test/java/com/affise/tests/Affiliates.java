package com.affise.tests;

import com.affise.api.annotations.Positive;
import com.affise.api.payloads.Affiliate;
import com.affise.api.services.AffiliateApiService;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static com.affise.api.conditions.Conditions.bodyField;
import static com.affise.api.conditions.Conditions.statusCode;
import static com.affise.api.constans.Constans.Run.host;
import static com.affise.api.generatedata.Generations.generateEmail;
import static com.affise.api.generatedata.Generations.generatePassword;
import static org.hamcrest.Matchers.*;


public class Affiliates {

    private final AffiliateApiService affiliateApiService = new AffiliateApiService();

    @BeforeClass
    public void setUp() {
        RestAssured.baseURI = host;
    }

    @Positive
    @Test(description = "User can create affiliate with required fields")
    public void createAffiliatePhp() {
        Map<String, Object> affiliate = new HashMap<>();
        affiliate.put("email", generateEmail());
        affiliate.put("password", generatePassword());

        affiliateApiService.createAffiliate(affiliate)
                .shouldHave(statusCode(200))
                .shouldHave(bodyField("partner.id", not(emptyOrNullString())))
                .shouldHave(bodyField("partner.email", equalTo(affiliate.get("email"))));
    }

    @Positive
    @Test(description = "User can create affiliate with required fields")
    public void createAffiliateGoapi() {

        Affiliate affiliate = new Affiliate()
                .email(generateEmail())
                .password(generatePassword());

        RestAssured
                .given().log().all()
                .queryParam("client_id", "99999")
                .header("Authorization", "Bearer vEGs9PjxGSyF7nhLsuat_TWmv21XR9woPIthTCuAfl0.rpCAPStCu1Z7b_umnzZRo8LOVXZY-T8imfaFxTv0DHg")
                .contentType(ContentType.JSON)
                .body(affiliate)
                .when().post("/affiliates")
                .then().statusCode(200).log().status();
    }

}