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
import java.util.stream.Stream;

import static api.conditions.Conditions.bodyField;
import static api.conditions.Conditions.statusCode;
import static api.helpers.OfferHelper.getOfferGoals;
import static api.helpers.constants.ConstantsStrings.*;
import static api.helpers.db.Mongo.MongoHelpers.*;
import static api.helpers.enums.ConversionStatuses.CONFIRMED;
import static api.helpers.enums.TimePeriod.DAY;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertFalse;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith({AutomationPreSetUp.class})
public class DeleteKpiRule extends AutomationPreSetUp {

    private static Kpi kpiRule;

    @BeforeEach
    public void createKpiRule(){
        kpiRule = new Kpi()
            .offers(Arrays.asList(String.valueOf(offer.getId())))
            .period(DAY.getTitle())
            .actionType(CONVERSION)
            .changeTo(CONFIRMED.getTitle())
            .kpi(37.0)
            .goal1(getOfferGoals(offer).get(0))
            .goal2(getOfferGoals(offer).get(1));

        addKpiRulesToMongo(kpiRule);
    }

    @AfterEach
    public void deleteKpiRule(){
        if (kpiRuleExistInMongo(kpiRule.mongoId())){
            deleteKpiRules(kpiRule);
        }
    }

    ///-------------------------|   Positive   |-------------------------///

    @Positive @Test
    @DisplayName("User can delete kpi rule")
    void userCanDeleteKpiRule() {
        KpiRule kpiRules = kpiService.deleteKpiRule(generalManager.getApiKey(), kpiRule.mongoId().toString())
            .shouldHave(statusCode(200))
            .asClass(KpiRule.class);

        assertFalse(kpiRuleExistInMongo(kpiRule.mongoId()));
    }

    ///-------------------------|   Negative   |-------------------------///

    @Negative @Test
    @DisplayName("User can not delete kpi rule twice")
    void userCanNotDeleteKpiRuleTwice() {
        KpiRule kpi1 = kpiService.deleteKpiRule(generalManager.getApiKey(), kpiRule.mongoId().toString())
            .shouldHave(statusCode(200)).asClass(KpiRule.class);

        KpiRule kpi2 = kpiService.deleteKpiRule(generalManager.getApiKey(), kpiRule.mongoId().toString())
            .shouldHave(statusCode(404))
            .shouldHave(bodyField(ERROR, equalTo(NOT_FOUND_NOTHING_TO_DELETE)))
            .asClass(KpiRule.class);
    }

    @ParameterizedTest
    @MethodSource("api.affise.com.BeforeAllClasses.AutomationPreSetUp#usersProvider")
    @DisplayName("User without permission can not delete kpi rule")
    void userWithNoAccessCanNotGetKpiRulesList(User user) {
        KpiRule kpiRules = kpiService.deleteKpiRule(user.getApiKey(), kpiRule.mongoId().toString())
            .shouldHave(statusCode(403))
            .asClass(KpiRule.class);
    }

    @Test
    @DisplayName("User with partner api-key can not delete kpi rule")
    void affiliateCanNotDeleteKpiRule() {
        KpiRule kpiRules = kpiService.deleteKpiRule(affiliate.getAffiliate().getApiKey(), kpiRule.mongoId().toString())
            .shouldHave(statusCode(403))
            .shouldHave(bodyField(ERROR, equalTo(AUTH_DENIED)))
            .asClass(KpiRule.class);
    }

    @Test
    @DisplayName("User with no api-key can not can not delete kpi rule")
    void userWithNoApiKeyCanNotDeleteKpiRule() {
        KpiRule kpiRules = kpiService.deleteKpiRule(kpiRule.mongoId().toString())
            .shouldHave(statusCode(401))
            .shouldHave(bodyField(ERROR, equalTo(TOKEN_IS_NECESSARY)))
            .asClass(KpiRule.class);
    }

}
