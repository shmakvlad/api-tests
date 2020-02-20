package com.affise.api.assertions;

import com.affise.api.conditions.Condition;
import io.restassured.response.Response;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class AssertableResponse {

    private final Response response;

    public AssertableResponse shouldHave(Condition condition){
        condition.check(response);
        return this;
    }

}
