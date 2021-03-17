package api.affise.com.automationTests.KpiAutomationFeature;

import api.affiliates.AffiliateAdd;
import api.affise.com.BaseTestSetup;
import api.automation.kpi.KpiRule;
import api.conditions.Conditions;
import api.helpers.enums.ConversionStatuses;
import api.helpers.enums.TimePeriod;
import api.helpers.tags.Negative;
import api.helpers.tags.Positive;
import api.offers.admin.additional_classes.Offer;
import api.services.KpiAutomationApiService;
import api.services.OffersApiService;
import api.users.User;
import io.restassured.response.Response;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.*;
import java.util.stream.Stream;

import static api.helpers.AffiliateHelper.generatedMinAffiliateParams;
import static api.helpers.AutomationHelper.getKpiRule;
import static api.helpers.AutomationHelper.getKpiRuleByResponse;
import static api.helpers.OfferHelper.getOfferGoals;
import static api.helpers.OfferHelper.getOfferWithGoalsInPayouts;
import static api.helpers.constants.ConstantsStrings.*;
import static api.helpers.db.Mongo.MongoHelpers.*;
import static api.helpers.enums.ConversionStatuses.CONFIRMED;
import static api.helpers.enums.TimePeriod.HOUR;
import static api.services.AffiliateApiService.AffiliateHelpers.createAffiliate;
import static api.services.UserApiService.UsersHelpers.createUserStatic;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static api.conditions.Conditions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AddKpiRule extends BaseTestSetup {

    private final KpiAutomationApiService kpiAutomationApiService = new KpiAutomationApiService();
    private static User generalManager;
    private static User affiliateManager;
    private static User salesManager;
    private static Offer offer;
    private static AffiliateAdd affiliate;

    @BeforeAll
    public static void createPreSetupData(){
        generalManager = createUserStatic(ROLE_ADMIN).getUser();
        affiliateManager = createUserStatic(ROLE_AFF_MANAGER).getUser();
        salesManager = createUserStatic(ROLE_SALES_MANAGER).getUser();
        affiliate = createAffiliate(generatedMinAffiliateParams());
        offer = getOfferWithGoalsInPayouts(Arrays.asList("1", "2"));
    }

    @AfterAll()
    public static void deletePreSetupData(){
        deleteOfferFully(offer.getId());
        deleteAffiliateFullyById(affiliate.getId());
        deleteUsersFully(
            generalManager.getId(),
            affiliateManager.getId(),
            salesManager.getId()
        );
    }

    ///-------------------------|   Positive   |-------------------------///

    @Positive @Test
    @DisplayName("User can create rule with type Conversion")
    void createKpiRuleWithTypeConversion(){
        // Generate data
        Map<String, Object> body = new HashMap<>();
        body.put(OFFER, offer.getId());
        body.put(PERIOD, HOUR.getTitle());
        body.put(ACTION_TYPE, CONVERSION);
        body.put(CHANGE_TO, CONFIRMED.getTitle());
        body.put(KPI, 25);
        body.put(GOAL1, offer.getPayments().get(0).getGoal());
        body.put(GOAL2, offer.getPayments().get(1).getGoal());

        // Validation assert
        KpiRule kpi = kpiAutomationApiService.createKpiRule(generalManager.getApiKey(), body)
            .shouldHave(Conditions.statusCode(200))
            .shouldHave(Conditions.bodyField("message.id", not(isEmptyOrNullString())))
            .asClass(KpiRule.class);

        assertAll("Validate" + kpi,
                () -> assertEquals(body.get(OFFER).toString(), kpi.kpi().offers().get(0)),
                () -> assertEquals(body.get(PERIOD), kpi.kpi().period()),
                () -> assertEquals(body.get(ACTION_TYPE), kpi.kpi().actionType()),
                () -> assertEquals(body.get(CHANGE_TO), kpi.kpi().changeTo()),
                () -> assertEquals(body.get(KPI), kpi.kpi().kpi().intValue()),
                () -> assertEquals(body.get(GOAL1), kpi.kpi().goal1()),
                () -> assertEquals(body.get(GOAL2), kpi.kpi().goal2())
        );

        assertTrue(kpiRuleExistInMongo(new ObjectId(kpi.kpi().id())));

        // Clear data
        deleteKpiRules(kpi);
    }

    @Test
    @DisplayName("User can create rule with type Block")
    void createKpiRuleWithTypeBlock(){
        // Generate data
        Map<String, Object> body = new HashMap<>();
        body.put(OFFER, offer.getId());
        body.put(PERIOD, HOUR.getTitle());
        body.put(ACTION_TYPE, BLOCK);
        body.put(KPI, 25);
        body.put(GOAL1, offer.getPayments().get(0).getGoal());
        body.put(GOAL2, offer.getPayments().get(1).getGoal());

        // Validation assert
        KpiRule kpi = kpiAutomationApiService.createKpiRule(generalManager.getApiKey(), body)
                .shouldHave(Conditions.statusCode(200))
                .shouldHave(Conditions.bodyField("message.id",  not(isEmptyOrNullString())))
                .shouldHave(Conditions.bodyField("message.offers[0]", equalTo(body.get(OFFER).toString())))
                .shouldHave(Conditions.bodyField("message.period", equalTo(body.get(PERIOD))))
                .shouldHave(Conditions.bodyField("message.action_type", equalTo(body.get(ACTION_TYPE))))
                .shouldHave(Conditions.bodyField("message.goal1", equalTo(body.get(GOAL1))))
                .shouldHave(Conditions.bodyField("message.goal2", equalTo(body.get(GOAL2))))
                .shouldHave(Conditions.bodyField("message.kpi", equalTo(body.get(KPI))))
                .asClass(KpiRule.class);

        // Clear data
        deleteKpiRules(kpi);
    }

    @Test
    @DisplayName("User can create rule with type Block and Affiliate")
    void createKpiRuleWithTypeBlockAndAffiliates(){
        // Generate data
        Map<String, Object> body = new HashMap<>();
        body.put(OFFER, offer.getId());
        body.put(PERIOD, HOUR.getTitle());
        body.put(ACTION_TYPE, BLOCK);
        body.put(AFFILIATESKPI, affiliate.getId());
        body.put(KPI, 25);
        body.put(GOAL1, offer.getPayments().get(0).getGoal());
        body.put(GOAL2, offer.getPayments().get(1).getGoal());

        // Validation assert
        KpiRule kpi = kpiAutomationApiService.createKpiRule(generalManager.getApiKey(), body)
                .shouldHave(Conditions.statusCode(200))
                .shouldHave(Conditions.bodyField("message.id",  not(isEmptyOrNullString())))
                .shouldHave(Conditions.bodyField("message.affiliates[0]", equalTo(body.get(AFFILIATESKPI).toString())))
                .asClass(KpiRule.class);

        // Clear data
        deleteKpiRules(kpi);
    }

    @ParameterizedTest
    @ValueSource(strings = { "sub1", "sub2", "sub3", "sub4", "sub5" })
    @DisplayName("User can create rule with Affiliates and Sub")
    void createKpiRuleWithAffiliatesAndSub(String sub){
        // Generate data
        Map<String, Object> body = new HashMap<>();
        body.put(OFFER, offer.getId());
        body.put(PERIOD, HOUR.getTitle());
        body.put(ACTION_TYPE, BLOCK);
        body.put(AFFILIATESKPI, affiliate.getId());
        body.put(SUB, sub);
        body.put(KPI, 25);
        body.put(GOAL1, offer.getPayments().get(0).getGoal());
        body.put(GOAL2, offer.getPayments().get(1).getGoal());

        // Validation assert
        KpiRule kpi = kpiAutomationApiService.createKpiRule(generalManager.getApiKey(), body)
                .shouldHave(Conditions.statusCode(200))
                .shouldHave(Conditions.bodyField("message.id",  not(isEmptyOrNullString())))
                .shouldHave(Conditions.bodyField("message.sub", equalTo(body.get(SUB))))
                .asClass(KpiRule.class);

        // Clear data
        deleteKpiRules(kpi);
    }

    @ParameterizedTest
    @EnumSource(ConversionStatuses.class)
    @DisplayName("User can create rule with different conversion statuses")
    void createKpiRuleWithDifferentConversionStatuses(ConversionStatuses conversionStatuses){
        // Generate data
        Map<String, Object> body = new HashMap<>();
        body.put(OFFER, offer.getId());
        body.put(PERIOD, HOUR.getTitle());
        body.put(ACTION_TYPE, CONVERSION);
        body.put(CHANGE_TO, conversionStatuses.getTitle());
        body.put(KPI, 25);
        body.put(GOAL1, offer.getPayments().get(0).getGoal());
        body.put(GOAL2, offer.getPayments().get(1).getGoal());

        // Validation assert
        KpiRule kpi = kpiAutomationApiService.createKpiRule(generalManager.getApiKey(), body)
                .shouldHave(Conditions.statusCode(200))
                .shouldHave(Conditions.bodyField("message.change_to", equalTo(body.get(CHANGE_TO))))
                .asClass(KpiRule.class);

        // Clear data
        deleteKpiRules(kpi);
    }

    @ParameterizedTest
    @EnumSource(TimePeriod.class)
    @DisplayName("User can create rule with different time periods")
    void createKpiRuleWithDifferentTimePeriod(TimePeriod timePeriod){
        // Generate data
        Map<String, Object> body = new HashMap<>();
        body.put(OFFER, offer.getId());
        body.put(PERIOD, timePeriod.getTitle());
        body.put(ACTION_TYPE, BLOCK);
        body.put(KPI, 25);
        body.put(GOAL1, offer.getPayments().get(0).getGoal());
        body.put(GOAL2, offer.getPayments().get(1).getGoal());

        // Validation assert
        KpiRule kpi = kpiAutomationApiService.createKpiRule(generalManager.getApiKey(), body)
                .shouldHave(statusCode(200))
                .shouldHave(bodyField("message.period", equalTo(body.get(PERIOD))))
                .asClass(KpiRule.class);

        // Clear data
        deleteKpiRules(kpi);
    }

    ///-------------------------|   Negative   |-------------------------///

    @Negative @ParameterizedTest @Disabled(value = "Bug, access is available now")
    @MethodSource("usersProvider")
    @DisplayName("User without permission can not create kpi rule")
    void userWithNoAccessCanNotCreateRule(User user){
        // Generate data
        Map<String, Object> body = getKpiRule(offer.getId(), HOUR.getTitle(), BLOCK, null, 25, null,
                offer.getPayments().get(0).getGoal(), offer.getPayments().get(1).getGoal(), null, 0);

        Response response = kpiAutomationApiService.createKpiRule(user.getApiKey(), body)
                .getResponse();

        // Validation assert, clear data if status code is 200
        try {
            assertThat(response.statusCode(), equalTo(403));
        } finally {
            deleteKpiRules(getKpiRuleByResponse(response));
        }
    }

    @Test
    @DisplayName("User can not create kpi rule with disabled partner")
    void usersCanNotCreateRuleWithDisabledPartner(){
        // Generate data
        AffiliateAdd affiliateDisabled = createAffiliate(generatedMinAffiliateParams());
        OffersApiService.OfferHelpers.disableAffiliateFromOffer(generalManager.getApiKey(), offer.getId(), affiliateDisabled.getId());

        Map<String, Object> body = getKpiRule(offer.getId(), HOUR.getTitle(), BLOCK, null, 25, null,
                offer.getPayments().get(0).getGoal(), offer.getPayments().get(1).getGoal(), null, affiliateDisabled.getId());

        Response response = kpiAutomationApiService.createKpiRule(generalManager.getApiKey(), body)
                .getResponse();

        // Validation assert, clear data if status code is 200
        try {
            assertThat(response.statusCode(), equalTo(400));
            assertThat(response.getBody().jsonPath().getString(ERROR),
                    equalTo(String.format("The following affiliate ID (%s) is not valid for this offer", affiliateDisabled.getId())));
        } finally {
            deleteKpiRules(getKpiRuleByResponse(response));
        }
    }

    @ParameterizedTest
    @MethodSource("invalidDataProvider")
    @DisplayName("User can not create kpi rule with invalid data")
    void userCanNotCreateRuleWithIncorrectData(Integer offerId, String timePeriod, String actionType,
                                               String conversionStatuses, Integer kpiValue,
                                               Integer notify_manager, String goal1, String goal2,
                                               String sub, Integer affiliate, String expectedResult){
        // Generate data
        Map<String, Object> body = getKpiRule(offerId, timePeriod, actionType,
                conversionStatuses, kpiValue, notify_manager, goal1, goal2, sub, affiliate);

        Response response = kpiAutomationApiService.createKpiRule(generalManager.getApiKey(), body)
                .getResponse();

        // Validation assert, clear data if status code is 200
        try {
            assertAll(
                () -> assertEquals(400, response.statusCode()),
                () -> assertEquals(expectedResult, response.path(ERROR))
            );
        } finally {
            deleteKpiRules(getKpiRuleByResponse(response));
        }
    }

    ///-------------------------|   Providers   |-------------------------///

    static Stream<Arguments> invalidDataProvider() {
        return Stream.of(
                arguments(999999999, HOUR.getTitle(), BLOCK, null, 25, null, getOfferGoals(offer).get(0), getOfferGoals(offer).get(1), null, 0,
                        "Offer not found\\n[offers].data: Offers [999999999] not found"),
                arguments(offer.getId(), HOUR.getTitle(), BLOCK, null, 25, null, getOfferGoals(offer).get(0), getOfferGoals(offer).get(1), null, 999999999,
                        "[affiliates].data: Affiliates [999999999] not found"),
                arguments(offer.getId(), HOUR.getTitle(), BLOCK, null, 25, null, getOfferGoals(offer).get(0), getOfferGoals(offer).get(1), "sub6", affiliate.getId(),
                        "[sub]: This value is not valid."),
                arguments(offer.getId(), HOUR.getTitle(), BLOCK, null, 25, 0, getOfferGoals(offer).get(0), getOfferGoals(offer).get(1), "sub2", 0,
                        "data[affiliates]: You need to choose affiliates for subaccount."),
                arguments(offer.getId(), HOUR.getTitle(), CONVERSION, CONFIRMED.getTitle(), 25, 0, getOfferGoals(offer).get(0), getOfferGoals(offer).get(1), null, 0,
                        ": Method does not allow extra fields: notify_manager"),
                arguments(offer.getId(), "year", BLOCK, null, 25, null, getOfferGoals(offer).get(0), getOfferGoals(offer).get(1), null, 0,
                        "[period]: This value is not valid."),
                arguments(offer.getId(), HOUR.getTitle(), CONVERSION, "not_found", 25, null, getOfferGoals(offer).get(0), getOfferGoals(offer).get(1), null, 0,
                        "[change_to]: This value is not valid."),
                arguments(offer.getId(), HOUR.getTitle(), CONVERSION, CONFIRMED.getTitle(), 101, null, getOfferGoals(offer).get(0), getOfferGoals(offer).get(1), null, 0,
                        "[kpi].data: field kpi must be in range (0, 100]"),
                arguments(offer.getId(), HOUR.getTitle(), CONVERSION, CONFIRMED.getTitle(), 0, null, getOfferGoals(offer).get(0), getOfferGoals(offer).get(1), null, 0,
                        "[kpi].data: field kpi must be in range (0, 100]"),
                arguments(offer.getId(), HOUR.getTitle(), BLOCK, null, 25, null, "3", "4", null, 0,
                        "goal1 field not valid\\ngoal2 field not valid")
        );
    }

    static Stream<User> usersProvider() {
        return Stream.of(
                affiliateManager,
                salesManager
        );
    }

    static Stream<User> allUsersProvider() {
        return Stream.of(
                generalManager,
                affiliateManager,
                salesManager
        );
    }

}
