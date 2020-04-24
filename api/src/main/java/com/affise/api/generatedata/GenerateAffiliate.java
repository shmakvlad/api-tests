package com.affise.api.generatedata;

import com.affise.api.payloads.Php.Affiliate;
import com.affise.api.services.AffiliateApiService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;

import java.util.HashMap;
import java.util.Map;

import static com.affise.api.generatedata.Generations.*;

public class GenerateAffiliate {
    private static final AffiliateApiService affiliateApiService = new AffiliateApiService();


    public static Map generateAffiliateWithReqFields(){
        Map<String, Object> affiliate = new HashMap<>();
            affiliate.put("email", generateEmail());
            affiliate.put("password", generatePassword());
        return affiliate;
    }

    public static Map generateAffiliateWithoutReqFields(){
        Map<String, Object> affiliate = new HashMap<>();
            affiliate.put("notes", generateFirstName());
            affiliate.put("login",generateLastName());
        return affiliate;
    }

    @SneakyThrows
    public static Affiliate getNewAffiliate() {
            String json = affiliateApiService.createAffiliate(generateAffiliateWithReqFields()).asString();
            Affiliate affiliate = new ObjectMapper().readValue(jsonNode(json, "partner"), Affiliate.class);
        return affiliate;
    }

}
