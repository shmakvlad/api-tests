package com.affise.api.services;


import com.affise.api.assertions.AssertableResponse;
import com.affise.api.payloads.AffiliateGo;

import java.util.Map;

import static com.affise.api.constans.Constans.Headers.API_KEY;
import static com.affise.api.constans.Constans.User.ADMIN;

public class AffiliateApiService extends PhpApiService {

    public AssertableResponse createAffiliate(Map affiliate) {
        return new AssertableResponse(setUp()
                .header(API_KEY, ADMIN)
                .formParams(affiliate)
                .when().post("/admin/partner"));
    }

    public AssertableResponse createAffiliate(Map affiliate, String usertype) {
        return new AssertableResponse(setUp()
                .header(API_KEY, usertype)
                .formParams(affiliate)
                .when().post("/admin/partner"));
    }

    public AssertableResponse editAffiliate(Map affiliate, String usertype, Integer id) {
        return new AssertableResponse(setUp()
                .header(API_KEY, usertype)
                .formParams(affiliate)
                .when().post("/admin/partner/{id}", id));
    }

    public AssertableResponse createGoAffiliate(AffiliateGo requestBody, String token) {
        return new AssertableResponse(goSetUp()
                .header("Authorization", "Bearer " + token)
                .body(requestBody)
                .when().post("/affiliates"));
    }

}


