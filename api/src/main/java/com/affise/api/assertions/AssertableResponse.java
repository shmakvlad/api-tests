package com.affise.api.assertions;

import com.affise.api.conditions.Condition;
import com.affise.api.payloads.Affiliate;
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
    public Affiliate asAffiliatePojo(String path){
        String json = response.asString();
        Affiliate affiliate = new ObjectMapper().readValue(jsonNode(json, path), Affiliate.class);
        return affiliate;
    }

}
