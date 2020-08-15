package com.affise.api.config;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.Filter;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.aeonbits.owner.ConfigFactory;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.affise.api.constans.Constans.Run.*;

public class Config {

    public static ProjectConfig getConfig() {
        return config;
    }

    public static final ProjectConfig config = ConfigFactory.create(ProjectConfig.class, System.getProperties(), System.getenv());

    public RequestSpecification phpApiReqSpec = new RequestSpecBuilder()
            .setBaseUri(getConfig().baseUrl())
            .setBasePath(apipath)
            .addFilters(getFilters())
            .build();

    public ResponseSpecification phpApiRespSpec = new ResponseSpecBuilder()
            .log(LogDetail.STATUS)
            .log(LogDetail.BODY)
            .build();

    public RequestSpecification newPhpApiReqSpec = new RequestSpecBuilder()
            .setBaseUri(host)
            .setBasePath(otherApiPath)
            .setContentType(ContentType.JSON)
            .addFilters(getFilters())
            .build();

    public RequestSpecification goApiReqSpec = new RequestSpecBuilder()
            .setBaseUri(goapihost)
            .setBasePath(goapipath)
            .setContentType(ContentType.JSON)
            .setAccept(ContentType.JSON)
            .addFilters(getFilters())
            .addQueryParam("client_id", getConfig().clientId())
            .build();

    private List<Filter> getFilters(){
        if (getConfig().logging()){
            return Arrays.asList(new RequestLoggingFilter(), new ResponseLoggingFilter(), new AllureRestAssured());
        }
        return Collections.emptyList();
    }

}
