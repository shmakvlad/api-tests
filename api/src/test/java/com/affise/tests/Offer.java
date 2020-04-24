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

import static com.affise.api.config.Config.getConfig;
import static com.affise.api.generatedata.Generations.*;

public class Offer {

    private final OfferApiService offerApiService = new OfferApiService();
    private final ConnectToMongo connectToMongo = setUpMongo();
    private final ConnectToMySql connectToMySql = setUpMySql();



    @BeforeSuite
    public ConnectToMySql setUpMySql(){
        return new ConnectToMySql();
    }


    @BeforeClass
    public ConnectToMongo setUpMongo(){
        return new ConnectToMongo();
    }


    @Positive
    @SneakyThrows
    @Test(description = "User can create offer")
    public void createOfferGo(){

        OfferGo request = new OfferGo()
                .title(generateFirstName())
                .trackingUrl(generateUrl())
                .advertiserId("5e9587705a27262185c64cf5")
                .categories(Arrays.asList("health", "gambling", "marketplace"))
                .allowedIps(generateList("192.168.100.11", "216.58.215.110"))
                .description(new Description().en("description").ru("информация"))
                .landings(generateList(
                        new LandingsItem()
                                .previewUrl("https://google.com").title("Search").trackingUrl("dev.affise.com").type("landing"),
                        new LandingsItem()
                                .previewUrl("https://yandex.ru").title("Search").trackingUrl("dev.affise.com").type("prelanding")))
                .payouts(Arrays.asList(
                        new PayoutsItem()
                                .affiliates(Arrays.asList(3, 4)).goalTitle("hello").goalValue("world")
                                .currency("USD").paymentType("percent").total(12).payouts(3)))
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
                                                .goalType("exact").goals(Arrays.asList("world")).resetToValue(0).value(50).isRemaining(true))));

        OfferGo response = offerApiService.createGoOffer(request, getConfig().token())
                .asPojo(OfferGo.class);
    }



    @AfterClass
    public void cleanMongo(){
        connectToMongo.closeConnection();
    }


    @AfterSuite
    public void cleanMySql(){
        connectToMySql.closeConnection();
    }

}
