package com.affise.tests.Run;

import com.affise.api.annotations.Positive;
import com.affise.api.services.AffiliateApiService;
import io.restassured.response.Response;
import org.hamcrest.Matchers;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.affise.api.constans.Constans.Headers.API_KEY;
import static com.affise.api.constans.Constans.Run.otherApiPath;
import static com.affise.api.constans.Constans.User.ADMIN;
import static io.restassured.RestAssured.given;
import static io.restassured.path.json.JsonPath.from;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.is;

public class Groovy {

    private final AffiliateApiService affiliateApiService = new AffiliateApiService();

    @Positive @Test(description = "Get Affiliates")
    public void groovy1() {

        Response response = affiliateApiService.getAffiliates().getResponse();

        ArrayList<Object> allListPartners = response.path("partners");
        ArrayList<Object> allListLogin = response.path("partners.login");
        ArrayList<Map<String, ?>> allTeamData = response.path("partners");
        Integer lastName = response.path("partners.id[-1]");

        Integer maxId = response.path("partners.max { it.id }.id");
        String minEmail = response.path("partners.min { it.id }.email");
        Integer sum = response.path("partners.collect {it.id}.sum()");
        Integer sumFind = response.path("partners.findAll {it}.id.sum()");

        String email = response.path("partners.find {it.id == 142}.email");
        Map<String, ?> findOne = response.path("partners.find { it.email == '80@gmail.com' }");

        List<String> listStatus = response.path("partners.findAll { it.id > 141 }.status");
        ArrayList<Map<String, ?>> map = response.path("partners.findAll { it.id > 186 }");
        List affiliateId = response.path("partners.findAll {it.id}.email");
        List affiliate = response.path("partners.findAll {it}.email");
        List<String> ls = response.path("partners.collect {it.email}");

        assertThat(affiliateId, equalTo(response.path("partners.collect {it.email}")));
    }

    @Test @Positive
    public void groovy2(){
        given()
            .headers(API_KEY, ADMIN)
            .queryParam("country[]", "BY","US")
            .pathParam("path", otherApiPath)
        .when()
            .get( "/{path}/regions")
        .then()
            .body("regions.country_code", hasItems("BY","US")).and()
            .body("regions.name*.length().sum()", lessThan(700))
            .body("regions.name*.length().sum()", greaterThan(500))
            .body("regions.id.sum()", is(196090))
            .body("regions*.id.sum()", is(196090))
            .body("regions.sum { it.id }", equalTo(196090))
            .body("regions.sum { it.id }", Matchers.<Object>equalTo(196090))
            .assertThat().statusLine("HTTP/1.1 200 OK")
            .assertThat().statusCode(200);
    }

    @Test @Positive
    public void groovy3(){
        String response = given().headers(API_KEY, ADMIN).when().get("/3.1/regions?country[]=BY&country[]=US").asString();
        int sumOfAllAuthorLengths = from(response).getInt("regions.country_code*.length().sum()");
        assertThat(sumOfAllAuthorLengths, is(124));
    }

}
