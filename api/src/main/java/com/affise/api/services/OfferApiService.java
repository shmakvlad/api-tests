package com.affise.api.services;

import com.affise.api.assertions.AssertableResponse;

import java.util.Map;

import static com.affise.api.constans.Constans.Headers.API_KEY;
import static com.affise.api.constans.Constans.User.ADMIN;

public class OfferApiService extends PhpApiService {

    public AssertableResponse createOffer(Map offer) {
        return new AssertableResponse(setUp()
                .header(API_KEY, ADMIN)
                .formParams(offer)
                .when().post("/admin/offer"));
    }

    public AssertableResponse createOffer(Map offer, String usertype) {
        return new AssertableResponse(setUp()
                .header(API_KEY, usertype)
                .formParams(offer)
                .when().post("/admin/offer"));
    }

    public AssertableResponse editOffer(Map offer, String usertype, Integer id) {
        return new AssertableResponse(setUp()
                .header(API_KEY, usertype)
                .formParams(offer)
                .when().post("/admin/offer/{id}", id));
    }

    public AssertableResponse getListOffers(String usertype) {
        return new AssertableResponse(setUp()
                .header(API_KEY, usertype)
                .when().get("/offers"));
    }

    public AssertableResponse deleteOffer(Integer id, String usertype) {
        return new AssertableResponse(setUp()
                .header(API_KEY, usertype)
                .formParam("offer_id[]", id)
                .when().post("/admin/offer/delete"));
    }

}
