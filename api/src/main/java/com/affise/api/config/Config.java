package com.affise.api.config;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

import static com.affise.api.constans.Constans.Run.*;

public class Config {

    public RequestSpecification phpApiReqSpec = new RequestSpecBuilder()
            .setBaseUri(host)
            .setBasePath(apipath)
//            .addFilter(new RequestLoggingFilter())
//            .addFilter(new ResponseLoggingFilter())
            .log(LogDetail.URI).log(LogDetail.METHOD).log(LogDetail.PARAMS).log(LogDetail.HEADERS).log(LogDetail.BODY)
            .build();

    public ResponseSpecification phpApiRespSpec = new ResponseSpecBuilder()
            .log(LogDetail.STATUS)
            .log(LogDetail.BODY)
            .build();

    public RequestSpecification newPhpApiReqSpec = new RequestSpecBuilder()
            .setBaseUri(host)
            .setBasePath(otherApiPath)
            .setContentType(ContentType.JSON)
            .log(LogDetail.URI).log(LogDetail.METHOD).log(LogDetail.PARAMS).log(LogDetail.HEADERS).log(LogDetail.BODY)
            .build();

}
