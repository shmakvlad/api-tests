package com.affise.api.generatedata;

import com.affise.api.database.ConnectToMongo;
import com.affise.api.payloads.Php.Offers.Offer;
import com.affise.api.services.OfferApiService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;

import java.util.HashMap;
import java.util.Map;

import static com.affise.api.generatedata.Generations.*;

public class GenerateOffer {

    private static final ConnectToMongo connectToMongo = new ConnectToMongo();
    private static final OfferApiService offerApiService = new OfferApiService();

    public static Map generateOfferWithReqFields(String advertiser){
        Map<String, Object> offer = new HashMap();
            offer.put("title", generateFirstName());
            offer.put("advertiser", advertiser);
            offer.put("url", generateUrl());
            offer.put("status", "active");
        return offer;
    }

    public static Map generateOfferWithReqFields(){
        Map<String, Object> offer = new HashMap();
            offer.put("title", generateFirstName());
            offer.put("advertiser", connectToMongo.getFirstObjectFromMongo("suppliers", "_id"));
            offer.put("url", generateUrl());
            offer.put("status", "active");
        return offer;
    }

    public static Map generateOfferWithoutReqFields(){
        Map<String, Object> offer = new HashMap<>();
            offer.put("title", generateFirstName());
            offer.put("url", generateUrl());
        return offer;
    }

    @SneakyThrows
    public static Offer getNewOffer() {
        String json = offerApiService.createOffer(generateOfferWithReqFields()).asString();
            Offer offer = new ObjectMapper().readValue(jsonNode(json, "offer"), Offer.class);
        return offer;
    }

    @SneakyThrows
    public static Offer getNewOffer(String advertiser) {
        String json = offerApiService.createOffer(generateOfferWithReqFields(advertiser)).asString();
        Offer offer = new ObjectMapper().readValue(jsonNode(json, "offer"), Offer.class);
        return offer;
    }

}
