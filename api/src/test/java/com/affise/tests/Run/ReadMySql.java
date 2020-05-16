package com.affise.tests.Run;

import com.affise.api.annotations.Positive;
import com.affise.api.database.ConnectToMySql;
import com.affise.api.services.AffiliateApiService;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static com.affise.api.conditions.Conditions.bodyField;
import static com.affise.api.conditions.Conditions.statusCode;
import static org.hamcrest.Matchers.*;

public class ReadMySql {

    private final AffiliateApiService affiliateApiService = new AffiliateApiService();
    private final ConnectToMySql connectToMySql = new ConnectToMySql();

    @BeforeClass
    public void setUp() {
        connectToMySql.createAffiliate(
                "INSERT INTO `partner` (`id`, `email`, `status`, `created_at`, `updated_at`) VALUES ('1', 'mike@gmail.com', '0', '2020-05-11 13:00:23', '2020-05-12 13:00:23')",
                "INSERT INTO `partner` (`id`, `email`, `status`, `created_at`, `updated_at`) VALUES ('2', 'maks@gmail.com', '1', '2020-04-10 13:00:23', '2020-05-12 13:00:23')",
                "INSERT INTO `partner` (`id`, `email`, `status`, `created_at`, `updated_at`) VALUES ('3', 'alex@gmail.com', '2', '2020-05-11 13:00:23', '2020-05-12 13:00:23')",
                "INSERT INTO `partner` (`id`, `email`, `status`, `created_at`, `updated_at`) VALUES ('4', 'kate@gmail.com', '3', '2020-02-11 13:00:23', '2020-05-12 13:00:23')"
        );
    }



    @Positive @Test(description = "User can get Affiliates")
    public void getAffiliates() {
        affiliateApiService.getAffiliates()
                .shouldHave(statusCode(200))
                .shouldHave(bodyField("status", equalTo(1)))
                .shouldHave(bodyField("pagination.per_page", equalTo(100)))
                .shouldHave(bodyField("pagination.total_count", equalTo(4)))
                .shouldHave(bodyField("pagination.page", equalTo(1)))
                .shouldHave(bodyField("partners.id", hasItems(1,2,3,4)))
                .shouldHave(bodyField("partners.id", not(hasItems(5))))
                .shouldHave(bodyField("partners.id[-1]", equalTo(4)))
                .shouldHave(bodyField("partners.id[-2]", equalTo(3)))
                .shouldHave(bodyField("partners.id[0]", equalTo(1)))
                .shouldHave(bodyField("partners.id[1]", equalTo(2)))
                .shouldHave(bodyField("partners.collect {it.email}", hasItems("mike@gmail.com","kate@gmail.com","alex@gmail.com","maks@gmail.com")))
                .shouldHave(bodyField("partners.findAll {it.id == 2 || it.id == 3}.email", hasItems("maks@gmail.com","alex@gmail.com")))
                .shouldHave(bodyField("partners.find {it.id == 4 && it.email == 'kate@gmail.com'}.status", equalTo("on moderation")))
                .shouldHave(bodyField("partners.findAll {it.id > 0 && it.id < 5}.find {it.status == 'active'}.id", equalTo(2)));
    }


    @Positive @Test(description = "User can get one Affiliate")
    public void getAffiliate() {
        connectToMySql.createAffiliate(
                "INSERT INTO `partner` (`id`, `email`, `status`, `created_at`, `updated_at`) " +
                        "VALUES ('5', 'lora@gmail.com', '1', '2020-05-11 13:00:23', '2020-05-12 13:00:23')"
        );

        affiliateApiService.getAffiliate(5)
                .shouldHave(statusCode(200))
                .shouldHave(bodyField("status", equalTo(1)))
                .shouldHave(bodyField("partner.id", equalTo(5)))
                .shouldHave(bodyField("partner.status", equalTo("active")))
                .shouldHave(bodyField("id", equalTo(5)));

        connectToMySql.deleteAffiliateFromMySql(5);
    }



    @AfterClass
    public void cleanUp() {
        connectToMySql.deleteAffiliates(1,2,3,4);
        connectToMySql.closeConnection();
    }

}
