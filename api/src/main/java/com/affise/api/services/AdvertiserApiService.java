package com.affise.api.services;

import com.affise.api.assertions.AssertableResponse;

import java.util.Map;

import static com.affise.api.constans.Constans.Headers.API_KEY;
import static com.affise.api.constans.Constans.User.ADMIN;

public class AdvertiserApiService extends PhpApiService {

    public AssertableResponse createAdvertiser(Map advertiser) {
        return new AssertableResponse(setUp()
                .header(API_KEY, ADMIN)
                .formParams(advertiser)
                .when().post("/admin/advertiser"));
    }

    public AssertableResponse createAdvertiser(Map advertiser, String usertype) {
        return new AssertableResponse(setUp()
                .header(API_KEY, usertype)
                .formParams(advertiser)
                .when().post("/admin/advertiser"));
    }

    public AssertableResponse editAdvertiser(Map advertiser, String usertype, String id) {
        return new AssertableResponse(setUp()
                .header(API_KEY, usertype)
                .formParams(advertiser)
                .when().post("/admin/advertiser{id}", id));
    }

}
