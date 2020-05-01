package com.affise.tests;

import com.affise.api.annotations.Positive;
import com.affise.api.database.ConnectToMongo;
import com.affise.api.database.ConnectToMySql;
import com.affise.api.payloads.Go.Offers.*;
import com.affise.api.services.OfferApiService;
import lombok.SneakyThrows;
import org.testng.annotations.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.affise.api.conditions.Conditions.statusCode;
import static com.affise.api.config.Config.getConfig;
import static com.affise.api.generatedata.Generations.*;

public class Offer {

    private final OfferApiService offerApiService = new OfferApiService();
    private final ConnectToMongo connectToMongo = setUpMongo();
    private final ConnectToMySql connectToMySql = setUpMySql();



    @BeforeSuite
    public ConnectToMySql setUpMySql(){ return new ConnectToMySql(); }

    @BeforeTest
    public void beforeTest(){}

    @BeforeClass
    public ConnectToMongo setUpMongo(){ return new ConnectToMongo(); }

    @BeforeMethod
    public void beforeMethod(){}

    @BeforeGroups("Offer")
    public void beforeGroup(){}


    @DataProvider(name = "Create Offer")
    public static Object[][] examples() {

        CapsItem capsItem2 = new CapsItem().affiliates(Collections.emptyList()).affiliateType("all")
                .timeframe("day").type("clicks").goalType("all").resetToValue(32).value(0).isRemaining(true);

        CapsItem capsItem3 = new CapsItem().affiliates(Arrays.asList(1, 2)).affiliateType("exact").timeframe("day").type("clicks").goalType("all").resetToValue(32).value(0).isRemaining(true);
        CapsItem capsItem4 = new CapsItem().affiliates(Collections.emptyList()).affiliateType("all").timeframe("day").type("clicks").goalType("all").resetToValue(100).value(0).isRemaining(true);
        CapsItem capsItem5 = new CapsItem().affiliateType("all").timeframe("day").type("clicks").goalType("all").resetToValue(0).value(44).isRemaining(false);

        return new Object[][]{
                {0, true, "http://www.tutorialspoint.com", Arrays.asList("health", "gambling", "marketplace"), new CapsItem().affiliates(Collections.emptyList()).affiliateType("all").timeframe("all").type("conversions").goalType("exact").goals(Arrays.asList("world")).resetToValue(0).value(20).isRemaining(false)},
                {1, false, "http://www.affise.com", Arrays.asList("health", "gambling"), capsItem2},
                {11, true, "http://www.google.com", Arrays.asList(), capsItem3},
                {12, false, "http://www.yandex.ru", Arrays.asList(), capsItem4},
                {24, true, "http://www.w3schools.com", Collections.emptyList(), capsItem5}
        };
    }

    @Positive
    @SneakyThrows
    @Test(description = "User can create offer", priority = 1, dataProvider = "Create Offer", groups = "Offer")
    public void createOfferGo(Integer payouts, Boolean uniqueIp, String previewUrl, List categories, CapsItem capsItem){

        OfferGo request = new OfferGo()
                .title(generateFirstName())
                .trackingUrl(generateUrl())
                .previewUrl(previewUrl)
                .advertiserId("5eaacff96d1bda817c1b6fa4")
                .top(true).uniqueIp(uniqueIp)
                .categories(categories)
                .allowedIps(generateList("192.168.100.11", "216.58.215.110"))
                .description(new Description().en("description").ru("информация"))
                .landings(generateList(
                        new LandingsItem()
                                .previewUrl("https://google.com").title("Search").trackingUrl("dev.affise.com").type("landing"),
                        new LandingsItem()
                                .previewUrl("https://yandex.ru").title("Search").trackingUrl("dev.affise.com").type("prelanding")))
                .payouts(Arrays.asList(
                        new PayoutsItem()
                                .affiliates(Arrays.asList(1, 2)).goalTitle("hello").goalValue("world")
                                .currency("USD").paymentType("percent").total(12).payouts(payouts)))
                .caps(
                        new Caps()
                                .conversionStatus(Arrays.asList("confirmed", "pending", "declined", "hold", "not_found"))
                                .currency("USD").timezone("UTC")
                                .caps(Arrays.asList(
                                        new CapsItem()
                                                .affiliates(Collections.emptyList()).affiliateType("all").timeframe("all").type("conversions")
                                                .goalType("exact").goals(Arrays.asList("world")).resetToValue(0).value(100).isRemaining(false),
                                        new CapsItem()
                                                .affiliates(Collections.emptyList()).affiliateType("all").timeframe("all").type("budget")
                                                .goalType("exact").goals(Arrays.asList("world")).resetToValue(5).value(0).isRemaining(true),
                                        capsItem)));

        OfferGo response = offerApiService.createGoOffer(request, getConfig().token())
                .shouldHave(statusCode(200))
                .asPojo(OfferGo.class);
    }


    @AfterMethod
    public void afterMethod(){}

    @AfterClass
    public void cleanMongo(){ connectToMongo.closeConnection(); }

    @AfterTest
    public void afterTest(){}

    @AfterSuite
    public void cleanMySql(){ connectToMySql.closeConnection(); }

    @AfterGroups("Offer")
    public void afterGroup(){}

}
