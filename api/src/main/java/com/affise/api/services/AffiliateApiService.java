package com.affise.api.services;


import com.affise.api.assertions.AssertableResponse;

import java.util.Map;

public class AffiliateApiService extends PhpApiService{

    public AssertableResponse createAffiliate(Map affiliate) {
        return new AssertableResponse(setUp()
                .header("api-key","f87d35d9e7ed3fe153ce95b259133019")
                .formParams(affiliate)
                .when().post("/admin/partner"));
    }

}


