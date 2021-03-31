package api.affise.com.automationTests.KpiAutomationFeature;

import api.affise.com.automationTests.AutomationPreSetUp;
import api.automation.kpi.Kpi;
import api.automation.kpi.KpiRule;
import api.conditions.Conditions;
import api.helpers.tags.Negative;
import api.helpers.tags.Positive;
import api.offers.admin.additional_classes.Offer;
import api.offers.admin.additional_classes.PartnerPaymentsItem;
import api.users.User;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import static api.conditions.Conditions.bodyField;
import static api.conditions.Conditions.statusCode;
import static api.helpers.AutomationHelper.getKpiRule;
import static api.helpers.OfferHelper.getOfferGoals;
import static api.helpers.OfferHelper.getPartnerOfferGoals;
import static api.helpers.constants.ConstantsStrings.*;
import static api.helpers.constants.ConstantsStrings.CONVERSION;
import static api.helpers.db.Mongo.MongoHelpers.*;
import static api.helpers.enums.ConversionStatuses.CONFIRMED;
import static api.helpers.enums.ConversionStatuses.HOLD;
import static api.helpers.enums.TimePeriod.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith({AutomationPreSetUp.class})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class EditKpiRule extends AutomationPreSetUp {

    private static Kpi kpiRule1, kpiRule2;

    @BeforeAll
    public static void createPreSetupData(){
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
            .sub(SUB2)
            .notifyManager(1)
            .affiliates(Arrays.asList(String.valueOf(affiliate.getId())))
            .goal1(offer.getPayments().get(0).getGoal())
            .goal2(offer.getPayments().get(1).getGoal());

        addKpiRulesToMongo(kpiRule1, kpiRule2);
    }

    @AfterAll()
    public static void deletePreSetupData(){
        deleteKpiRules(kpiRule1, kpiRule2);
    }

    ///-------------------------|   Positive   |-------------------------///

    @Positive @Test @Order(1)
    @DisplayName("User can edit rule with type Conversion")
    void editKpiRuleWithTypeConversion(){
        Map<String, Object> body = new HashMap<>();
        body.put(OFFERKPI, offer.getId());
        body.put(PERIOD, WEEK.getTitle());
        body.put(ACTION_TYPE, CONVERSION);
        body.put(CHANGE_TO, HOLD.getTitle());
        body.put(KPI, 44);
        body.put(GOAL1, offer.getPayments().get(1).getGoal());
        body.put(GOAL2, offer.getPayments().get(0).getGoal());

        KpiRule kpi = kpiService.editKpiRule(generalManager.getApiKey(), kpiRule1.mongoId().toString(), body)
            .shouldHave(Conditions.statusCode(200))
            .shouldHave(Conditions.bodyField("message.id", not(isEmptyOrNullString())))
            .asClass(KpiRule.class);

        assertAll("Validate" + kpi,
                () -> assertEquals(body.get(OFFERKPI).toString(), kpi.kpi().offers().get(0)),
                () -> assertEquals(body.get(PERIOD), kpi.kpi().period()),
                () -> assertEquals(body.get(ACTION_TYPE), kpi.kpi().actionType()),
                () -> assertEquals(body.get(CHANGE_TO), kpi.kpi().changeTo()),
                () -> assertEquals(body.get(KPI), kpi.kpi().kpi().intValue()),
                () -> assertEquals(body.get(GOAL1), kpi.kpi().goal1()),
                () -> assertEquals(body.get(GOAL2), kpi.kpi().goal2())
        );
    }

    @Test @Order(1)
    @DisplayName("User can edit rule with type Block")
    void editKpiRuleWithTypeBlock(){
        Map<String, Object> body = new HashMap<>();
        body.put(OFFERKPI, offer.getId());
        body.put(PERIOD, MONTH.getTitle());
        body.put(ACTION_TYPE, BLOCK);
        body.put(AFFILIATESKPI, affiliate.getId());
        body.put(NOTIFY_MANAGER, 1);
        body.put(SUB, SUB1);
        body.put(KPI, 55);
        body.put(GOAL1, offer.getPayments().get(1).getGoal());
        body.put(GOAL2, offer.getPayments().get(0).getGoal());

        KpiRule kpi = kpiService.editKpiRule(generalManager.getApiKey(), kpiRule2.mongoId().toString(), body)
            .shouldHave(Conditions.statusCode(200))
            .shouldHave(Conditions.bodyField("message.id", not(isEmptyOrNullString())))
            .asClass(KpiRule.class);

        assertAll("Validate" + kpi,
                () -> assertEquals(body.get(OFFERKPI).toString(), kpi.kpi().offers().get(0)),
                () -> assertEquals(body.get(PERIOD), kpi.kpi().period()),
                () -> assertEquals(body.get(ACTION_TYPE), kpi.kpi().actionType()),
                () -> assertEquals(body.get(AFFILIATESKPI).toString(), kpi.kpi().affiliates().get(0)),
                () -> assertEquals(body.get(KPI), kpi.kpi().kpi().intValue()),
                () -> assertEquals(body.get(SUB), kpi.kpi().sub()),
                () -> assertEquals(body.get(GOAL1), kpi.kpi().goal1()),
                () -> assertEquals(body.get(GOAL2), kpi.kpi().goal2()),
                () -> assertEquals(body.get(NOTIFY_MANAGER), kpi.kpi().notifyManager())
        );
    }

    @Test @Order(2)
    @DisplayName("Change action type from Block to Conversion")
    void editKpiRuleChangeActionTypeOnConversion(){
        Map<String, Object> body = new HashMap<>();
        body.put(OFFERKPI, offer.getId());
        body.put(PERIOD, WEEK.getTitle());
        body.put(ACTION_TYPE, CONVERSION);
        body.put(CHANGE_TO, HOLD.getTitle());
        body.put(KPI, 44);
        body.put(GOAL1, offer.getPayments().get(0).getGoal());
        body.put(GOAL2, offer.getPayments().get(1).getGoal());

        KpiRule kpi = kpiService.editKpiRule(generalManager.getApiKey(), kpiRule2.mongoId().toString(), body)
            .shouldHave(Conditions.statusCode(200))
            .shouldHave(Conditions.bodyField("message.id", not(isEmptyOrNullString())))
            .shouldHave(Conditions.bodyField("message.affiliates", is(emptyCollectionOf(String.class))))
            .shouldHave(Conditions.bodyField("message.sub", is(nullValue())))
            .shouldHave(Conditions.bodyField("message.notify_manager", is(nullValue())))
            .asClass(KpiRule.class);
    }

    @Test @Order(2)
    @DisplayName("Change action type from Conversion to Block")
    void editKpiRuleChangeActionTypeOnBlock(){
        Map<String, Object> body = new HashMap<>();
        body.put(OFFERKPI, offer.getId());
        body.put(PERIOD, WEEK.getTitle());
        body.put(ACTION_TYPE, BLOCK);
        body.put(KPI, 88);
        body.put(GOAL1, offer.getPayments().get(0).getGoal());
        body.put(GOAL2, offer.getPayments().get(1).getGoal());

        KpiRule kpi = kpiService.editKpiRule(generalManager.getApiKey(), kpiRule1.mongoId().toString(), body)
            .shouldHave(Conditions.statusCode(200))
            .shouldHave(Conditions.bodyField("message.id", not(isEmptyOrNullString())))
            .shouldHave(Conditions.bodyField("message.affiliates", is(emptyCollectionOf(String.class))))
            .shouldHave(Conditions.bodyField("message.sub", is(nullValue())))
            .shouldHave(Conditions.bodyField("message.notify_manager", is(nullValue())))
            .shouldHave(Conditions.bodyField("message.change_to", is(nullValue())))
            .asClass(KpiRule.class);
    }

    ///-------------------------|   Negative   |-------------------------///

    @Negative
    @ParameterizedTest
    @MethodSource("api.affise.com.BeforeAllClasses.AutomationPreSetUp#usersProvider")
    @DisplayName("User without permission can not edit kpi rule")
    void userWithNoAccessCanNotEditRule(User user){
        Map<String, Object> body = getKpiRule(offer.getId(), HOUR.getTitle(), BLOCK, null, 25, null,
                offer.getPayments().get(0).getGoal(), offer.getPayments().get(1).getGoal(), null, 0);

        Response response = kpiService.editKpiRule(user.getApiKey(), kpiRule2.mongoId().toString(), body)
                .getResponse();

        assertThat(response.statusCode(), equalTo(403));
    }

    @Test
    @DisplayName("User with partner api-key can not edit kpi rule")
    void userWithPartnerApiKeyCanNotEditRule(){
        Map<String, Object> body = getKpiRule(offer.getId(), HOUR.getTitle(), BLOCK, null, 25, null,
                offer.getPayments().get(0).getGoal(), offer.getPayments().get(1).getGoal(), null, 0);

        KpiRule kpiRules = kpiService.editKpiRule(affiliate.getAffiliate().getApiKey(), kpiRule2.mongoId().toString(), body)
            .shouldHave(statusCode(403))
            .shouldHave(bodyField(ERROR, equalTo(AUTH_DENIED)))
            .asClass(KpiRule.class);
    }

    @Test
    @DisplayName("User with with no api-key can not edit kpi rule")
    void userWithNoApiKeyCanNotEditRule(){
        Map<String, Object> body = getKpiRule(offer.getId(), HOUR.getTitle(), BLOCK, null, 25, null,
                offer.getPayments().get(0).getGoal(), offer.getPayments().get(1).getGoal(), null, 0);

        KpiRule kpiRules = kpiService.editKpiRule(kpiRule2.mongoId().toString(), body)
            .shouldHave(statusCode(401))
            .shouldHave(bodyField(ERROR, equalTo(TOKEN_IS_NECESSARY)))
            .asClass(KpiRule.class);

        getPartnerOfferGoals(offer);
    }

    @ParameterizedTest
    @MethodSource("invalidDataProvider")
    @DisplayName("User can not edit kpi rule without one of required fields")
    void userCanNotEditRuleWithoutOneOfRequiredFields(Integer offerId, String timePeriod, String actionType, Integer kpiValue,
                                                      String goal1, String goal2, String expectedResult){
        Map<String, Object> body = new HashMap<>();
        if (offerId != null) body.put(OFFERKPI, offerId);
        body.put(PERIOD, timePeriod);
        body.put(ACTION_TYPE, actionType);
        body.put(KPI, kpiValue);
        body.put(GOAL1, goal1);
        body.put(GOAL2, goal2);

        Response response = kpiService.editKpiRule(generalManager.getApiKey(), kpiRule2.mongoId().toString(), body)
                .getResponse();

        assertAll(
                () -> assertEquals(400, response.statusCode()),
                () -> assertEquals(expectedResult, response.path(ERROR))
        );
    }

    ///-------------------------|   Providers   |-------------------------///

    static Stream<Arguments> invalidDataProvider() {
        return Stream.of(
                arguments(null, HOUR.getTitle(), BLOCK, 77, getOfferGoals(offer).get(0), getOfferGoals(offer).get(1),
                        "[offers].data: You should provide offer value"),
                arguments(offer.getId(), null, BLOCK, 77, getOfferGoals(offer).get(0), getOfferGoals(offer).get(1),
                        "[period].data: This value should not be blank."),
                arguments(offer.getId(), HOUR.getTitle(), null, 77, getOfferGoals(offer).get(0), getOfferGoals(offer).get(1),
                        "[action_type].data: This value should not be blank."),
                arguments(offer.getId(), HOUR.getTitle(), BLOCK, null, getOfferGoals(offer).get(0), getOfferGoals(offer).get(1),
                        "[kpi].data: field kpi must be in range (0, 100]"),
                arguments(offer.getId(), HOUR.getTitle(), BLOCK, 77, null, getOfferGoals(offer).get(1),
                        "[goal1].data: This value should not be blank."),
                arguments(offer.getId(), HOUR.getTitle(), BLOCK, 77, getOfferGoals(offer).get(0), null,
                        "[goal2].data: This value should not be blank.")
        );
    }

}
