package com.affise.tests.Run;

import com.affise.api.annotations.Positive;
import com.affise.api.config.Config;
import com.affise.api.services.AffiliateApiService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.common.mapper.TypeRef;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.hamcrest.Matchers;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.affise.api.constans.Constans.Headers.API_KEY;
import static com.affise.api.constans.Constans.User.ADMIN;
import static io.restassured.RestAssured.given;
import static io.restassured.path.json.JsonPath.from;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

public class Deserialization extends Config {

    private final AffiliateApiService affiliateApiService = new AffiliateApiService();

    @Test @Positive
    public void deserialization(){
        try {
            Response response =
                given().spec(phpApiReqSpec)
                    .queryParam("limit",5)
                    .header("api-key", ADMIN)
                .when()
                    .get("/admin/partners")
                    .then().extract().response();

            String json = response.asString();

            JsonNode jsonNode = new ObjectMapper().readTree(json);
            Integer status = jsonNode.get("status").asInt();
            Boolean flag = jsonNode.get("partners").isArray();
            Integer str = jsonNode.get("pagination").path("per_page").asInt();
            String list = jsonNode.get("partners").toString();
            String map1 = jsonNode.get("pagination").toString();
            String map2 = jsonNode.get("partners").get(0).toString();

            ArrayList l1 = response.path("partners");
            ArrayList l2 = from(json).get("partners");
            List l3 = from(json).getList("partners");
            String p1 = response.path("partners").toString();
            String p2 = response.path("partners[0]").toString();
            String p3 = response.path("pagination").toString();
            Map<String, Object> map3 = from(json).getMap("pagination");

            JsonPath jsonPath = new JsonPath(json);
            List lp1 = jsonPath.getList("partners");
            List lp2 = jsonPath.get("partners");
            String lp3 = jsonPath.getMap("pagination").toString();
            String lp4 = jsonPath.getList("partners").toString();
            Map map4 = jsonPath.getMap("pagination");

            List<HashMap<String, Object>> listmap = response.path("partners");
            HashMap<String, Object> hashmap1 = response.path("");
            List<HashMap<String, Object>> hashmap2 = response.path("partners.balance");

            HashMap jackmap2 = new ObjectMapper().readValue(json, HashMap.class);
            Map jackmap4 = new ObjectMapper().readValue(json, Map.class);

            HashMap<String, Object> jackmap3 = new ObjectMapper().readValue(json, new TypeReference<HashMap<String,Object>>(){});

            Map<String, Object> j2 = new ObjectMapper().readValue(map2, new TypeReference<Map<String,Object>>(){});

            HashMap<String, Object> j3 = new ObjectMapper().readValue(map2, new TypeReference<HashMap<String,Object>>(){});
            HashMap j6 = new ObjectMapper().readValue(map2, new TypeReference<HashMap<String,Object>>(){});
            HashMap<String, Object> j4 = new ObjectMapper().readValue(map2, HashMap.class);
            HashMap j5 = new ObjectMapper().readValue(map2, HashMap.class);

            ArrayList readlist = new ObjectMapper().readValue(list, new TypeReference<ArrayList>(){});

            ArrayList readlist2 = new ObjectMapper().readValue(jsonNode.get("partners").toString(), ArrayList.class);

            List<HashMap<String, Object>> readListMap = new ObjectMapper().readValue(list, new TypeReference<List<HashMap<String, Object>>>(){});

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test @Positive
    public void Gson(){
        String jsonString1 = given().headers(API_KEY, ADMIN).when().get("/3.1/regions?country[]=BY&country[]=US").asString();

//        HashMap<String,Object> map1 = new Gson().fromJson(jsonString1, new TypeToken<HashMap<String, Object>>(){}.getType());
//        HashMap map2 = new Gson().fromJson(jsonString1, HashMap.class);
//
//        String jsonString2 = given().headers(API_KEY, ADMIN).when().get("/3.0/admin/partner/1").asString();
//        Map map3 = new Gson().fromJson(jsonString2, Map.class);
    }

    @Test @Positive
    public void TypeRef(){
        Map<String, Object> jsonString = given().headers(API_KEY, ADMIN).when().get("/3.0/admin/partner/1").as(new TypeRef<Map<String, Object>>(){});
        Map jsonString1 = given().headers(API_KEY, ADMIN).when().get("/3.0/admin/partner/1").as(Map.class);
    }

    @Test @Positive
    public void Ger_Affiliate_Goapi2(){
        Response response = given().headers("Authorization", "Bearer miMFgpoACl84V2PxWyZqd6Pg0FAajh3pe-ALUlm4_Yg._6GC8ffiR3tZCYKI2KeuOeQu6pe_jOLONwIgI7jzLTk")
                .when().get("http://10.201.0.80:25292/4.0/affiliates?client_id=3").then().extract().response();
        List<HashMap<String, Object>> jsonString = response.path("affiliates");
        assertThat(response.getStatusCode(), is(200));
        assertThat(jsonString, hasSize(100));
        assertThat(jsonString.get(0).get("affiliate_id"), Matchers.<Object>equalTo(1));
        assertThat(jsonString.get(0).get("email"), Matchers.<Object>equalTo("demo@demo.com"));
    }

    @Test @Positive
    public void jsonPath(){
        Map map = given().headers(API_KEY, ADMIN).when().get("/3.1/regions?country[]=BY").path("pagination");

        String response = given().headers(API_KEY, ADMIN).when().get("/3.1/regions?country[]=BY").asString();

        JsonPath jsonPath = new JsonPath(response).setRootPath("regions");
        int is = jsonPath.getInt("id[0]");
        String isqwe = jsonPath.getString("name[0]");
        List<Integer> ids = jsonPath.get("id");
        List<String> cities = jsonPath.get("name");
        List<Object> idslist = jsonPath.getList("");
        int lottoId = from(response).getInt("regions[0].id");
        List<Object> list = from(response).getList("regions");
        Map<String, Object> maps = from(response).getMap("pagination");
        List<Integer> id = from(response).get("regions.id");
    }

}
