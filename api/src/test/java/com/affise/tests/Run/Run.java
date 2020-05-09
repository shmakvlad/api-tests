package com.affise.tests.Run;

import com.affise.api.annotations.Positive;
import com.affise.api.config.Config;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.response.Response;
import org.hamcrest.Matchers;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static com.affise.api.constans.Constans.Headers.API_KEY;
import static com.affise.api.constans.Constans.Run.apipath;
import static com.affise.api.constans.Constans.Run.otherApiPath;
import static com.affise.api.constans.Constans.User.ADMIN;
import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

public class Run extends Config {

    @Test @Positive
    public void put1(){
        String body = "{\"id\": 1,\"title\": \"foo\",\"body\":\"bar\",\"userId\":1}";

        String response =
            given()
                .body(body)
                .baseUri("https://jsonplaceholder.typicode.com")
                .header("Content-type","application/json; charset=UTF-8")
            .when()
                .put("/posts/1").asString();

        assertThat(body.replaceAll("[\\s\n]", ""), equalTo(response.replaceAll("[\\s\n]", "")));
    }

    @Test @Positive
    public void put2(){
        String body = "{\"id\": 1,\"title\": \"foo\",\"body\":\"bar\",\"userId\":1}";
            given()
                .body(body)
                .baseUri("https://jsonplaceholder.typicode.com")
                .header("Content-type","application/json; charset=UTF-8")
            .when()
                .put("/posts/1")
            .then()
                .body("id", is(1))
                .body("title", equalTo("foo"))
                .body("body", equalTo("bar"))
                .body("userId", Matchers.<Object>is(1));
    }

    @Test @Positive
    public void getRegionsOk(){
        given()
            .headers(API_KEY, ADMIN)
            .param("ola")
            .queryParam("country[]", "BY", "US", "RU")
            .pathParam("path", otherApiPath)
        .when()
            .get( "/{path}/regions")
        .then()
            .assertThat().statusCode(200);
    }

    @Test @Positive
    public void getAffiliateOk(){
            given()
                .pathParams("path",apipath)
                .pathParams("pid",1)
                .headers(API_KEY, ADMIN).
            when()
                .get("/{path}/admin/partner/{pid}").
            then()
                .assertThat().statusCode(200);

    }

    @Test @Positive
    public void getAffiliateOkWithSpec(){
        given()
            .spec(phpApiReqSpec).
        when()
            .get("/admin/partner/{pid}",1).
        then()
            .spec(phpApiRespSpec).statusCode(200)
            .body("partner.id", equalTo(1));
    }

    @Test @Positive
    public void getAffiliateResponse(){
        Response response =
                given().spec(phpApiReqSpec).
                        when().get("/admin/partner/{pid}",1).
                        then().spec(phpApiRespSpec).extract().response();

        assertThat(response.statusCode(),is(200));
        assertThat(response.jsonPath().getInt("partner.id"),equalTo(1));
        assertThat(response.jsonPath().getString("partner.email"),equalTo("demo@demo.com"));
        assertThat(response.getContentType(),equalTo("application/json"));
    }

    @Test @Positive
    public void path(){
        given()
            .headers(API_KEY,ADMIN).
        when()
            .get("/{path}/admin/partner/{pid}", apipath, 1).
        then()
            .statusCode(200)
            .assertThat().statusCode(200).and()
            .body("partner.id", equalTo(1))
            .body("partner.email", equalTo("demo@demo.com"))
            .body("partner.ref_percent", equalTo(null));
    }

    @Test @Positive
    public void postAffiliateOkFormData(){
        String email = (int) ( Math.random() * 800 ) + "ola@gmail.com";
        given()
            .spec(phpApiReqSpec)
            .formParam("password","123456")
            .formParam("email", email)
            .formParam("custom_fields[1]","message").
        when()
            .post("/admin/partner").
        then()
            .statusCode(200)
            .body("partner.email", equalTo(email));
    }

    @Test @Positive
    public void postAffiliateGoWithHashMap(){
        Map<String, Object> jsonAsMap = new HashMap<>();
        jsonAsMap.put("password", "123456");
        jsonAsMap.put("email", (int) ( Math.random() * 800 ) + "ola@gmail.com");

        given()
            .spec(goApiReqSpec)
            .contentType(JSON)
            .body(jsonAsMap).
        when()
            .post("/affiliates").
        then()
            .statusCode(200)
            .body("email", equalTo(jsonAsMap.get("email")));
    }

    @Test @Positive
    public void postAffiliateGoWithGson(){
        Map<String, Object> jsonAsMap = new HashMap<>();
        jsonAsMap.put("password", "123456");
        jsonAsMap.put("email", (int) ( Math.random() * 800 ) + "ola@gmail.com");

//        Gson gson = new GsonBuilder().setPrettyPrinting().create();
//        String json = gson.toJson(jsonAsMap);
//
//        given()
//            .spec(goApiReqSpec)
//            .contentType(JSON)
//            .body(json).
//        when()
//            .post("/affiliates").
//        then()
//            .statusCode(200)
//            .body("email", equalTo(jsonAsMap.get("email")));
    }

    @Test @Positive
    public void postAffiliateGoWithJackson(){
        Map<String, Object> jsonAsMap = new HashMap<>();
        jsonAsMap.put("password", "123456");
        jsonAsMap.put("email", (int) ( Math.random() * 800 ) + "ola@gmail.com");

        try {
            String json = new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(jsonAsMap);
            given()
                .spec(goApiReqSpec)
                .contentType(JSON)
                .body(json).
            when()
                .post("/affiliates").
            then()
                .statusCode(200)
                .body("email", equalTo(jsonAsMap.get("email")));

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @Test @Positive
    public void postAffiliateOkWithSpecWithFormParams(){
        Map<String, Object> jsonAsMap = new HashMap<>();
        jsonAsMap.put("password", "123456");
        jsonAsMap.put("email", (int) ( Math.random() * 800 ) + "ola@gmail.com");

        given()
            .spec(phpApiReqSpec)
            .formParams(jsonAsMap).
        when()
            .post("/admin/partner").
        then()
            .statusCode(200)
            .body("partner.email", equalTo(jsonAsMap.get("email")));
    }

}
