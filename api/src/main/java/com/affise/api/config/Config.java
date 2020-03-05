package com.affise.api.config;

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

    public static final ProjectConfig config = ConfigFactory.create(ProjectConfig.class);

    public RequestSpecification phpApiReqSpec = new RequestSpecBuilder()
            .setBaseUri(getConfig().baseUrl())
            .setBasePath(apipath)
            .addFilters(getFilters())
//            .setConfig(RestAssured.config.decoderConfig(decoderConfig().contentDecoders(DEFLATE, GZIP)))
//            .setConfig(config().encoderConfig(encoderConfig().defaultContentCharset("US-ASCII")))
//            .setConfig(config().decoderConfig(decoderConfig().defaultContentCharset("UTF-8")))
            .log(LogDetail.URI).log(LogDetail.METHOD).log(LogDetail.PARAMS).log(LogDetail.HEADERS).log(LogDetail.BODY)
//            .addFilter(new ResponseLoggingFilter())
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
            .log(LogDetail.URI).log(LogDetail.METHOD).log(LogDetail.PARAMS).log(LogDetail.HEADERS).log(LogDetail.BODY)
            .build();

    private List<Filter> getFilters(){
        if (getConfig().logging()){
            return Arrays.asList(new RequestLoggingFilter(), new ResponseLoggingFilter());
        }
        return Collections.emptyList();
    }
}
