package com.affise.api.conditions;

import lombok.experimental.UtilityClass;
import org.hamcrest.Matcher;

import java.util.Map;

@UtilityClass
public class Conditions {

    public StatusCodeCondition statusCode(int code) {
        return new StatusCodeCondition(code);
    }

    public BodyFieldCondition bodyField(String jsonPath, Matcher matcher) {
        return new BodyFieldCondition(jsonPath, matcher);
    }

    public BodyFieldsCondition bodyContainsAllFields(String jsonPath, Map map) {
        return new BodyFieldsCondition(jsonPath, map);
    }

}
