package api.affise.com.automationTests.KpiAutomationFeature;

import api.automation.kpi.Kpi;
import api.automation.kpi.KpiRule;
import api.helpers.tags.Negative;
import api.helpers.tags.Positive;
import api.offers.admin.additional_classes.Offer;
import api.services.KpiAutomationApiService;
import api.users.User;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.stream.Stream;

import static api.conditions.Conditions.bodyField;
import static api.conditions.Conditions.statusCode;
import static api.helpers.OfferHelper.getOfferGoals;
import static api.helpers.OfferHelper.getOfferWithGoalsInPayouts;
import static api.helpers.constants.ConstantsStrings.*;
import static api.helpers.db.Mongo.MongoHelpers.*;
import static api.helpers.enums.ConversionStatuses.CONFIRMED;
import static api.helpers.enums.TimePeriod.DAY;
import static api.services.UserApiService.UsersHelpers.createUserStatic;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class DeleteKpiRule {

    private final KpiAutomationApiService kpiService = new KpiAutomationApiService();
    private static User generalManager, affiliateManager, salesManager;
    private static Offer offer;
    private static Kpi kpiRule;

    @BeforeAll
    public static void createPreSetupData(){
        generalManager = createUserStatic(ROLE_ADMIN).getUser();
        affiliateManager = createUserStatic(ROLE_AFF_MANAGER).getUser();
        salesManager = createUserStatic(ROLE_SALES_MANAGER).getUser();
        offer = getOfferWithGoalsInPayouts(Arrays.asList("1", "2"));
    }

    @AfterAll()
    public static void deletePreSetupData(){
        deleteOfferFully(offer.getId());
        deleteUsersFully(
                generalManager.getId(),
                affiliateManager.getId(),
                salesManager.getId()
        );
    }

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
    @MethodSource("usersProvider")
    @DisplayName("User without permission can not delete kpi rule")
    void userWithNoAccessCanNotGetKpiRulesList(User user) {
        KpiRule kpiRules = kpiService.deleteKpiRule(user.getApiKey(), kpiRule.mongoId().toString())
                .shouldHave(statusCode(403))
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
