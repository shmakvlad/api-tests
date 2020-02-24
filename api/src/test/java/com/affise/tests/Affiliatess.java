package com.affise.tests;

import com.affise.api.annotations.Positive;
import com.affise.api.services.AffiliateApiService;
import io.restassured.RestAssured;
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


public class Affiliatess {

    private final AffiliateApiService affiliateApiService = new AffiliateApiService();

    @BeforeClass
    public void setUp() {
        RestAssured.baseURI = host;
    }

    @Positive
    @Test(description = "User can create affiliate with required fields")
    public void createAffiliatePhp() {
    // Generate data
        Map<String, Object> affiliate = new HashMap<>();
        affiliate.put("email", generateEmail());
        affiliate.put("password", generatePassword());

    // Validation assert
        affiliateApiService.createAffiliate(affiliate)
                .shouldHave(statusCode(200))
                .shouldHave(bodyField("partner.id", not(emptyOrNullString())))
                .shouldHave(bodyField("partner.email", equalTo(affiliate.get("email"))));
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
//    @Positive
//    @Test(description = "User can create affiliate with required fields")
//    public void createAffiliateGoapi() {
//        Affiliate affiliate = new Affiliate()
//                .email(generateEmail())
//                .password(generatePassword());
//
//        RestAssured
//                .given().log().all()
//                .queryParam("client_id", "99999")
//                .header("Authorization", "Bearer vEGs9PjxGSyF7nhLsuat_TWmv21XR9woPIthTCuAfl0.rpCAPStCu1Z7b_umnzZRo8LOVXZY-T8imfaFxTv0DHg")
//                .contentType(ContentType.JSON)
//                .body(affiliate)
//                .when().post("/affiliates")
//                .then().statusCode(200).log().status();
//    }

}