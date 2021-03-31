package api.affise.com.automationTests.KpiAutomationFeature;

import api.affiliates.AffiliateAdd;
import api.affise.com.BaseTestSetup;
import api.automation.kpi.Kpi;
import api.automation.kpi.KpiRule;
import api.helpers.tags.Negative;
import api.helpers.tags.Positive;
import api.offers.admin.additional_classes.Offer;
import api.services.KpiAutomationApiService;
import api.users.User;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static api.conditions.Conditions.bodyField;
import static api.conditions.Conditions.statusCode;
import static api.helpers.AffiliateHelper.generatedMinAffiliateParams;
import static api.helpers.OfferHelper.getOfferGoals;
import static api.helpers.OfferHelper.getOfferWithGoalsInPayouts;
import static api.helpers.constants.ConstantsStrings.*;
import static api.helpers.constants.ConstantsStrings.CONVERSION;
import static api.helpers.db.Mongo.MongoHelpers.*;
import static api.helpers.db.Mongo.MongoHelpers.deleteKpiRules;
import static api.helpers.enums.ConversionStatuses.CONFIRMED;
import static api.helpers.enums.TimePeriod.DAY;
import static api.helpers.enums.TimePeriod.HOUR;
import static api.services.AffiliateApiService.AffiliateHelpers.createAffiliate;
import static api.services.UserApiService.UsersHelpers.createUserStatic;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class GetKpiRules extends BaseTestSetup {

    private final KpiAutomationApiService kpiService = new KpiAutomationApiService();
    private static User generalManager, affiliateManager, salesManager;
    private static Offer offer;
    private static AffiliateAdd affiliate;
    private static Kpi kpiRule1, kpiRule2;

    @BeforeAll
    public static void createPreSetupData(){
        generalManager = createUserStatic(ROLE_ADMIN).getUser();
        affiliateManager = createUserStatic(ROLE_AFF_MANAGER).getUser();
        salesManager = createUserStatic(ROLE_SALES_MANAGER).getUser();
        affiliate = createAffiliate(generatedMinAffiliateParams());
        offer = getOfferWithGoalsInPayouts(Arrays.asList("1", "2"));

        kpiRule1 = new Kpi()
                .offers(Arrays.asList(String.valueOf(offer.getId())))
                .period(DAY.getTitle())
                .actionType(CONVERSION)
                .changeTo(CONFIRMED.getTitle())
                .kpi(37.0)
                .goal1(getOfferGoals(offer).get(0))
                .goal2(getOfferGoals(offer).get(1));

        kpiRule2 = new Kpi()
                .offers(Arrays.asList(String.valueOf(offer.getId())))
                .period(DAY.getTitle())
                .actionType(BLOCK)
                .affiliates(Collections.emptyList())
                .kpi(37.0)
                .goal1(offer.getPayments().get(0).getGoal())
                .goal2(offer.getPayments().get(1).getGoal());

        addKpiRulesToMongo(kpiRule1, kpiRule2);
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
        deleteKpiRules(kpiRule1, kpiRule2);
    }

    ///-------------------------|   Positive   |-------------------------///

    @Positive @Test
    @DisplayName("User can get kpi rules list") // 1 variant
    void userCanGetKpiRulesList1(){
        Response response = kpiService.getKpiRulesList(generalManager.getApiKey())
                .shouldHave(statusCode(200))
                .shouldHave(bodyField("items", hasSize(greaterThanOrEqualTo(2))))
                .shouldHave(bodyField("items.id", hasItems(kpiRule1.mongoId().toString(), kpiRule2.mongoId().toString())))
                .shouldHave(bodyField("items.findAll {it}.id", hasItems(kpiRule1.mongoId().toString(), kpiRule2.mongoId().toString())))
                .shouldHave(bodyField("items.collect {it.id}", hasItems(kpiRule1.mongoId().toString(), kpiRule2.mongoId().toString())))

                .shouldHave(bodyField("items.id[-1]", equalTo(kpiRule2.mongoId().toString())))
                .shouldHave(bodyField("items.offers[-1]", equalTo(Arrays.asList(kpiRule2.offers().get(0)))))
                .shouldHave(bodyField("items.period[-1]", equalTo(kpiRule2.period())))
                .shouldHave(bodyField("items.action_type[-1]", equalTo(kpiRule2.actionType())))
                .shouldHave(bodyField("items.affiliates[-1]", equalTo(kpiRule2.affiliates())))
                .shouldHave(bodyField("items.change_to[-1]", equalTo(kpiRule2.changeTo())))
                .shouldHave(bodyField("items.sub[-1]", equalTo(kpiRule2.sub())))
                .shouldHave(bodyField("items.goal1[-1]", equalTo(kpiRule2.goal1())))
                .shouldHave(bodyField("items.goal2[-1]", equalTo(kpiRule2.goal2())))
                .shouldHave(bodyField("items.kpi[-1]", equalTo(kpiRule2.kpi().intValue())))
                .shouldHave(bodyField("items.notify_manager[-1]", equalTo(kpiRule2.notifyManager())))

                .shouldHave(bodyField("pagination.per_page", equalTo(10)))
                .shouldHave(bodyField("pagination.total_count", greaterThanOrEqualTo(2)))
                .shouldHave(bodyField("pagination.page", equalTo(1)))
                .getResponse();

        Map findOne = response.path("items.find {it.id == '%s'}", kpiRule1.mongoId().toString());
        assertAll( "Validate first kpi rule object",
                () -> assertEquals(findOne.get(PERIOD), kpiRule1.period()),
                () -> assertEquals(findOne.get(ACTION_TYPE), kpiRule1.actionType()),
                () -> assertEquals(findOne.get(OFFERS), Arrays.asList(kpiRule2.offers().get(0))),
                () -> assertEquals(findOne.get(AFFILIATES), kpiRule1.affiliates()),
                () -> assertEquals(findOne.get(CHANGE_TO), kpiRule1.changeTo()),
                () -> assertEquals(findOne.get(SUB), kpiRule1.sub()),
                () -> assertEquals(findOne.get(GOAL1), kpiRule1.goal1()),
                () -> assertEquals(findOne.get(GOAL2), kpiRule1.goal2()),
                () -> assertEquals(findOne.get(KPI), kpiRule1.kpi().intValue()),
                () -> assertEquals(findOne.get(NOTIFY_MANAGER), kpiRule1.notifyManager())
        );
    }

    @Test
    @DisplayName("User can get kpi rules list") // 2 variant
    void userCanGetKpiRulesList2(){
        KpiRule kpiRules = kpiService.getKpiRulesList(generalManager.getApiKey())
                .shouldHave(statusCode(200))
                .shouldHave(bodyField("items", hasSize(greaterThanOrEqualTo(2))))
                .shouldHave(bodyField("items.id", hasItems(kpiRule1.mongoId().toString(), kpiRule2.mongoId().toString())))
                .asClass(KpiRule.class);

        if (kpiRules.pagination().getTotalCount() <= 10){
            Kpi kpi = kpiRules.kpiList().stream().filter(x -> x.id().equals(kpiRule1.mongoId().toString()))
                    .collect(Collectors.toList()).get(0);

            assertAll("Validate" +  kpiRule1.mongoId(),
                    () -> assertEquals(kpiRule1.mongoId(), kpi.id()),
                    () -> assertEquals(kpiRule1.period(), kpi.period()),
                    () -> assertEquals(kpiRule1.actionType(), kpi.actionType()),
                    () -> assertEquals(kpiRule1.offers(), kpi.offers()),
                    () -> assertEquals(kpiRule1.affiliates(), kpi.affiliates()),
                    () -> assertEquals(kpiRule1.changeTo(), kpi.changeTo()),
                    () -> assertEquals(kpiRule1.sub(), kpi.sub()),
                    () -> assertEquals(kpiRule1.notifyManager(), kpi.notifyManager()),
                    () -> assertEquals(kpiRule1.goal1(), kpi.goal1()),
                    () -> assertEquals(kpiRule1.goal2(), kpi.goal2()),
                    () -> assertEquals(kpiRule1.kpi().intValue(), kpi.kpi().intValue())
            );
        }

        if (kpiRules.pagination().getTotalCount() <= 10){
            Kpi kpi = kpiRules.kpiList().stream().filter(x -> x.id().equals(kpiRule2.mongoId().toString()))
                    .collect(Collectors.toList()).get(0);

            assertAll("Validate" +  kpiRule2.mongoId(),
                    () -> assertEquals(kpiRule2.mongoId(), kpi.id()),
                    () -> assertEquals(kpiRule2.period(), kpi.period()),
                    () -> assertEquals(kpiRule2.actionType(), kpi.actionType()),
                    () -> assertEquals(kpiRule2.offers(), kpi.offers()),
                    () -> assertEquals(kpiRule2.affiliates(), kpi.affiliates()),
                    () -> assertEquals(kpiRule2.changeTo(), kpi.changeTo()),
                    () -> assertEquals(kpiRule2.sub(), kpi.sub()),
                    () -> assertEquals(kpiRule2.notifyManager(), kpi.notifyManager()),
                    () -> assertEquals(kpiRule2.goal1(), kpi.goal1()),
                    () -> assertEquals(kpiRule2.goal2(), kpi.goal2()),
                    () -> assertEquals(kpiRule2.kpi().intValue(), kpi.kpi().intValue())
            );
        }

        if (kpiRules.pagination().getPerPage() == 2){
            assertAll("Pagination",
                    () -> assertEquals(kpiRules.pagination().getPerPage(), 10),
                    () -> assertEquals(kpiRules.pagination().getTotalCount(), 2),
                    () -> assertEquals(kpiRules.pagination().getPage(), 1)
            );
        }
    }

    @Test
    @DisplayName("User can get kpi rules list") // 3 variant | We can add a definite kpi rule to mongo via api
        // and check only it in body response with queryParameters in request
    void userCanGetKpiRulesList3() {
        // Generate data
        Map<String, Object> body = new HashMap<>();
        body.put(OFFER, offer.getId());
        body.put(PERIOD, HOUR.getTitle());
        body.put(ACTION_TYPE, CONVERSION);
        body.put(CHANGE_TO, CONFIRMED.getTitle());
        body.put(KPI, 25);
        body.put(GOAL1, offer.getPayments().get(0).getGoal());
        body.put(GOAL2, offer.getPayments().get(1).getGoal());

        KpiRule kpiCreate = kpiService.createKpiRule(generalManager.getApiKey(), body)
                .shouldHave(statusCode(200)).asClass(KpiRule.class);

        // Validate data
        Map<String, Object> queryParameters = new HashMap<>();
        queryParameters.put(PAGE, getKpiRulesCount());
        queryParameters.put(LIMIT, 1);

        Kpi kpi = kpiService.getKpiRulesList(generalManager.getApiKey(), queryParameters)
                .shouldHave(statusCode(200)).asClass(KpiRule.class).kpiList().get(0);

        assertAll("Validate" + kpi,
                () -> assertEquals(body.get(OFFER).toString(), kpi.offers().get(0)),
                () -> assertEquals(body.get(PERIOD), kpi.period()),
                () -> assertEquals(body.get(ACTION_TYPE), kpi.actionType()),
                () -> assertEquals(body.get(CHANGE_TO), kpi.changeTo()),
                () -> assertEquals(body.get(KPI), kpi.kpi().intValue()),
                () -> assertEquals(body.get(GOAL1), kpi.goal1()),
                () -> assertEquals(body.get(GOAL2), kpi.goal2())
        );

        // Clear data
        deleteKpiRules(kpiCreate);
    }

    @Test
    @DisplayName("User can get kpi rules list with query parameters") // 4 variant | We can add a definite kpi rule to mongo via mongoDB
        // and check only it in body response with queryParameters in request
    void userCanGetKpiRulesListWithQueryParameters() {
        // Generate data
        Kpi kpiRule3 = new Kpi()
            .offers(Arrays.asList(String.valueOf(offer.getId())))
            .period(DAY.getTitle())
            .actionType(CONVERSION)
            .changeTo(CONFIRMED.getTitle())
            .kpi(37.0)
            .goal1(getOfferGoals(offer).get(0))
            .goal2(getOfferGoals(offer).get(1));

        addKpiRulesToMongo(kpiRule3);

        // Validate data
        Map<String, Object> queryParameters = new HashMap<>();
        queryParameters.put(PAGE, getKpiRulesCount());
        queryParameters.put(LIMIT, 1);

        Kpi kpi = kpiService.getKpiRulesList(generalManager.getApiKey(), queryParameters)
                .shouldHave(statusCode(200)).asClass(KpiRule.class).kpiList().get(0);

        assertAll("Validate" + kpi,
                () -> assertEquals(kpiRule3.offers().get(0), kpi.offers().get(0)),
                () -> assertEquals(kpiRule3.period(), kpi.period()),
                () -> assertEquals(kpiRule3.actionType(), kpi.actionType()),
                () -> assertEquals(kpiRule3.changeTo(), kpi.changeTo()),
                () -> assertEquals(kpiRule3.kpi().intValue(), kpi.kpi().intValue()),
                () -> assertEquals(kpiRule3.goal1(), kpi.goal1()),
                () -> assertEquals(kpiRule3.goal2(), kpi.goal2())
        );

        // Clear data
        deleteKpiRules(kpiRule3);
    }

    ///-------------------------|   Negative   |-------------------------///

    @Negative @ParameterizedTest
    @MethodSource("usersProvider")
    @DisplayName("User without permission can not get kpi rules list")
    void userWithNoAccessCanNotGetKpiRulesList(User user) {
        KpiRule kpiRules = kpiService.getKpiRulesList(user.getApiKey())
            .shouldHave(statusCode(403))
            .asClass(KpiRule.class);
    }

    @Test
    @DisplayName("User with partner api-key can not get kpi rules list")
    void affiliateCanNotGetKpiRulesList() {
        KpiRule kpiRules = kpiService.getKpiRulesList(affiliate.getAffiliate().getApiKey())
            .shouldHave(statusCode(403))
            .shouldHave(bodyField(ERROR, equalTo(AUTH_DENIED)))
            .asClass(KpiRule.class);
    }

    @Test
    @DisplayName("User with no api-key can not get kpi rules list")
    void userWithNoApiKeyCanNotGetKpiRulesList() {
        KpiRule kpiRules = kpiService.getKpiRulesList()
            .shouldHave(statusCode(401))
            .shouldHave(bodyField(ERROR, equalTo(TOKEN_IS_NECESSARY)))
            .asClass(KpiRule.class);
    }

    ///-------------------------|   Providers   |-------------------------///

    static Stream<User> usersProvider() {
        return Stream.of(
                affiliateManager,
                salesManager
        );
    }
}
