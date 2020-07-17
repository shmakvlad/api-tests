package com.affise.api.assertions;

import com.affise.api.conditions.Condition;
import com.affise.api.payloads.Go.Affiliates.AffiliateBuilder;
import com.affise.api.payloads.Php.Afiliate;
import com.affise.api.payloads.Php.Offers.Offer;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.http.Headers;
import io.restassured.response.Response;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

import static com.affise.api.config.Config.getConfig;
import static com.affise.api.generatedata.Generations.jsonNode;

@Slf4j
@Getter
@RequiredArgsConstructor
public class AssertableResponse {

    private final Response response;

    public AssertableResponse shouldHave(Condition condition){
        if (getConfig().logging()){
            log.info("Check condition: {}", condition);
        }
        condition.check(response);
        return this;
    }

    public Headers getHeaders(){
        return response.getHeaders();
    }

    public Integer getStatusCode(){
        return response.getStatusCode();
    }

    public Map<String, String> getCookies(){
        return response.getCookies();
    }

    public String asString(){
        return response.asString();
    }

    public <T> T asPojo(Class<T> tClass){
        return response.as(tClass);
    }

    @SneakyThrows
    public Afiliate asAffiliatePojo(){
        String json = response.asString();
        Afiliate affiliate = new ObjectMapper().readValue(jsonNode(json, "partner"), Afiliate.class);
        return affiliate;
    }

    @SneakyThrows
    public Offer asOfferPojo(){
        String json = response.asString();
        Offer offer = new ObjectMapper().readValue(jsonNode(json, "offer"), Offer.class);
        return offer;
    }

    @SneakyThrows
    public AffiliateBuilder asAffiliateGoPojo(){
        String json = response.asString();
        AffiliateBuilder affiliate = new ObjectMapper().readValue(json, AffiliateBuilder.class);
        return affiliate;
    }

}
