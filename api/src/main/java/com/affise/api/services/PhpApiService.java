package com.affise.api.services;

import com.affise.api.config.Config;
import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;


public class PhpApiService extends Config{

    public RequestSpecification setUp(){
        return RestAssured
                .given().spec(phpApiReqSpec);
    }

    public RequestSpecification setUpNewApi(){
        return RestAssured
                .given().spec(newPhpApiReqSpec);
    }

    public RequestSpecification goSetUp(){
        return RestAssured
                .given().spec(goApiReqSpec);
    }

}
