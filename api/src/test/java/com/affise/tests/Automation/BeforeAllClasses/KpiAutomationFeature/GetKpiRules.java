package api.affise.com.automationTests.KpiAutomationFeature;

import api.affise.com.automationTests.AutomationPreSetUp;
import api.automation.kpi.Kpi;
import api.automation.kpi.KpiRule;
import api.helpers.tags.Negative;
import api.helpers.tags.Positive;
import api.users.User;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
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
import static api.helpers.OfferHelper.getOfferGoals;
import static api.helpers.constants.ConstantsStrings.*;
import static api.helpers.constants.ConstantsStrings.CONVERSION;
import static api.helpers.db.Mongo.MongoHelpers.*;
import static api.helpers.db.Mongo.MongoHelpers.deleteKpiRules;
import static api.helpers.enums.ConversionStatuses.CONFIRMED;
import static api.helpers.enums.TimePeriod.DAY;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith({AutomationPreSetUp.class})
public class GetKpiRules extends AutomationPreSetUp {

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
            .goal1(offer.getPayments().get(0).getGoal())
            .goal2(offer.getPayments().get(1).getGoal());

        addKpiRulesToMongo(kpiRule1, kpiRule2);
    }

    @AfterAll()
    public static void deletePreSetupData(){
        deleteKpiRules(kpiRule1, kpiRule2);
    }

    ///-------------------------|   Positive   |-------------------------///

    @Positive @Test
    @DisplayName("User can get kpi rules list")
    void userCanGetKpiRulesList2(){
        KpiRule kpiRules = kpiService.getKpiRulesList(generalManager.getApiKey())
            .shouldHave(statusCode(200))
            .shouldHave(bodyField("items", hasSize(greaterThanOrEqualTo(2))))
            .shouldHave(bodyField("items", is(not(emptyArray()))))
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
    }

    @Test
    @DisplayName("User can get kpi rules list with Query Parameters")
    void userCanGetKpiRulesListWithQueryParameters() {
        Kpi kpiRule3 = new Kpi()
            .offers(Arrays.asList(String.valueOf(offer.getId())))
            .period(DAY.getTitle())
            .actionType(CONVERSION)
            .changeTo(CONFIRMED.getTitle())
            .kpi(37.0)
            .goal1(getOfferGoals(offer).get(0))
            .goal2(getOfferGoals(offer).get(1));

        addKpiRulesToMongo(kpiRule3);

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

        deleteKpiRules(kpiRule3);
    }

    ///-------------------------|   Negative   |-------------------------///

    @Negative @ParameterizedTest
    @MethodSource("api.affise.com.BeforeAllClasses.AutomationPreSetUp#usersProvider")
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

}
