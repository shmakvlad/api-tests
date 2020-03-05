package com.affise.tests;

import com.affise.api.annotations.Positive;
import com.affise.api.payloads.AffiliateGoApi;
import com.affise.api.services.AffiliateApiService;
import lombok.SneakyThrows;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static com.affise.api.conditions.Conditions.*;
import static com.affise.api.generatedata.Generations.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.emptyOrNullString;
import static org.hamcrest.Matchers.not;


public class Affiliatess {

    private final AffiliateApiService affiliateApiService = new AffiliateApiService();

//    @BeforeClass
//    public void setUp() {
//        RestAssured.baseURI = host;
//    }

    @Positive
    @Test(description = "User can create affiliate with required fields", priority = 1)
    public void createAffiliatePhp() {
    // Generate data
        Map<String, Object> affiliate = new HashMap<>();
        affiliate.put("email", generateEmail());
        affiliate.put("password", generatePassword());
        affiliate.put("login", generateFirstName());
        affiliate.put("status", "active");
        System.out.println(affiliate.get("email"));

    // Validation assert
        affiliateApiService.createAffiliate(affiliate)
                .shouldHave(statusCode(200))
                .shouldHave(bodyField("partner.id", not(emptyOrNullString())))
                .shouldHave(bodyField("partner.email", equalTo(affiliate.get("email"))))
                .shouldHave(bodyContainsAllFields("partner", affiliate));
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
                .status("active");

//        String json = new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(request);
//
//        AffiliateGoApi response = RestAssured
//                .given()
//                .filter(new ResponseLoggingFilter())
//                .filter(new RequestLoggingFilter())
//                .queryParam("client_id", "99999")
//                .header("Authorization", "Bearer NpzVESQvoVR_kGGvJorbawK412XXAGmTjESkyAm3m-w.td2PINetdUOQxFTM6iwVgL48OitrVWipGfS08ZRMWHU")
//                .contentType(ContentType.JSON)
//                .body(request)
//                .when().post("http://10.201.0.80:58990/4.0/affiliates").as(AffiliateGoApi.class);

    }
}