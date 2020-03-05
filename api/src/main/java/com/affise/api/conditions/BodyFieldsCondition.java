package com.affise.api.conditions;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.response.Response;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.affise.api.generatedata.Generations.jsonNode;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@RequiredArgsConstructor
public class BodyFieldsCondition implements Condition {

    private final String jsonPath;
    private final Map requestBody;
    private final List<String> deleteKeys = Arrays.asList("password", "api_key");


    @SneakyThrows
    @Override
    public void check(Response response) {
        Map responseBody = new ObjectMapper().readValue(jsonNode(response.asString(), jsonPath), HashMap.class);
        assertThat(areEqual(requestBody, responseBody, deleteKeys), equalTo(true));
    }

    private boolean areEqual(Map<String, Object> first, Map<String, Object> second, List<String> deleteKeys) {
        for (String key : deleteKeys){ first.remove(key); }
        return first.entrySet().stream()
                .allMatch(e -> e.getValue().equals(second.get(e.getKey())));
    }

    @Override
    public String toString() {
        return "body fields [ " + jsonPath + " ] " + requestBody.toString();
    }

}
