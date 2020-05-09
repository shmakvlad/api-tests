package com.affise.tests.Run;

import com.affise.api.annotations.Positive;
import com.affise.api.services.AffiliateApiService;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static com.affise.api.conditions.Conditions.statusCode;
import static com.affise.api.generatedata.Generations.*;

public class JsonSchema {

    private final AffiliateApiService affiliateApiService = new AffiliateApiService();

    @Positive
    @Test(description = "Validate json schema")
    public void jsonSchema() {

        Map<String, Object> affiliate = new HashMap<>();
        affiliate.put("email", generateEmail());
        affiliate.put("password", generatePassword());
        affiliate.put("login", generateFirstName());
        affiliate.put("status", "active");

        affiliateApiService.createAffiliate(affiliate)
                .shouldHave(statusCode(200));
//                .getResponse().body(matchesJsonSchemaInClasspath("createAffiliateJsonSchema.json"));
    }

}
