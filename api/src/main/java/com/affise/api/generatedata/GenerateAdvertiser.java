package com.affise.api.generatedata;

import com.affise.api.payloads.Advertiser;
import com.affise.api.services.AdvertiserApiService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;

import java.util.HashMap;
import java.util.Map;

import static com.affise.api.generatedata.Generations.*;

public class GenerateAdvertiser {

    private static final AdvertiserApiService advertiserApiService = new AdvertiserApiService();


    public static Map generateAdvertiserWithReqFields(){
        Map<String, Object> advertiser = new HashMap<>();
            advertiser.put("email", generateEmail());
            advertiser.put("title", generateFullName());
        return advertiser;
    }

    public static Map generateAdvertiserWithReqFields(String manager_id){
        Map<String, Object> advertiser = new HashMap<>();
            advertiser.put("email", generateEmail());
            advertiser.put("title", generateFullName());
            advertiser.put("manager", manager_id);
        return advertiser;
    }

    public static Map generateAdvertiserWithoutReqFields(){
        Map<String, Object> advertiser = new HashMap<>();
            advertiser.put("note", generateFirstName());
            advertiser.put("url", generateUrl());
        return advertiser;
    }

    @SneakyThrows
    public static Advertiser getNewAdvertiser() {
            String json = advertiserApiService.createAdvertiser(generateAdvertiserWithReqFields()).asString();
            Advertiser advertiser = new ObjectMapper().readValue(jsonNode(json, "partner"), Advertiser.class);
        return advertiser;
    }

}
