package com.affise.api.services;

import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import com.affise.api.config.Config;


public class PhpApiService extends Config{

    public RequestSpecification setUp(){
        return RestAssured
                .given().spec(phpApiReqSpec);
    }

}
