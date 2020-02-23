package com.affise.api.assertions;

import com.affise.api.conditions.Condition;
import io.restassured.http.Headers;
import io.restassured.response.Response;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Map;

@Getter
@RequiredArgsConstructor
public class AssertableResponse {

    private final Response response;

    public AssertableResponse shouldHave(Condition condition){
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
    }}
