package api.affise.com.automationTests.KpiAutomationFeature;

import api.affise.com.automationTests.AutomationPreSetUp;
import api.helpers.tags.Negative;
import api.helpers.tags.Positive;
import api.users.User;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.*;
import java.util.stream.Stream;

import static api.conditions.Conditions.bodyField;
import static api.conditions.Conditions.statusCode;
import static api.helpers.Helpers.getDateString;
import static api.helpers.constants.ConstantsStrings.*;
import static api.services.KpiAutomationApiService.KpiAutomationHelper.createKpiRuleWithOfferAffiliate;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith({AutomationPreSetUp.class})
public class GetChangeLogs extends AutomationPreSetUp {

    private static final String dateFrom = getDateString(0);
    private static final String dateTo = getDateString(0);

    ///-------------------------|   Positive   |-------------------------///

    @Positive @Test
    @DisplayName("User can get change logs")
    void getChangeLogs(){
        kpiService.getChangeLogs(generalManager.getApiKey(), dateFrom, dateTo)
            .shouldHave(statusCode(200))
            .shouldHave(bodyField(STATUS, equalTo(1)))
            .shouldHave(bodyField(PAGINATION_PAGE, equalTo(1)))
            .shouldHave(bodyField(PAGINATION_PER_PAGE, equalTo(10)));

    }

    @Test
    @DisplayName("Get change logs with Filters: Page, Limit")
    void getChangeLogsWithPageLimitFilters(){
        Map<String, Object> queryParameters = new HashMap<>();
        queryParameters.put(PAGE, 1);
        queryParameters.put(LIMIT, 100);
        queryParameters.put(DATE_FROM, getDateString(-100));
        queryParameters.put(DATE_TO, getDateString(0));

        kpiService.getChangeLogs(generalManager.getApiKey(), queryParameters)
            .shouldHave(statusCode(200))
            .shouldHave(bodyField(PAGINATION_PAGE, equalTo(1)))
            .shouldHave(bodyField(PAGINATION_PER_PAGE, equalTo(100)))
            .shouldHave(bodyField("items", is(not(empty()))));
    }

    @ParameterizedTest
    @MethodSource("typeDataProvider")
    @DisplayName("Get change logs with Filter: Type")
    void getChangeLogsWithFilterType(List<String> types, List<String> positiveResult, List<String> negativeResult){
        Map<String, Object> queryParameters = new HashMap<>();
        queryParameters.put(PAGE, 1);
        queryParameters.put(LIMIT, 100);
        queryParameters.put(DATE_FROM, getDateString(-100));
        queryParameters.put(DATE_TO, getDateString(0));
        queryParameters.put(FILTER_TYPE, types);

        kpiService.getChangeLogs(generalManager.getApiKey(), queryParameters)
            .shouldHave(statusCode(200))
            .shouldHave(bodyField("items", is(not(empty()))))
            .shouldHave(bodyField("items.type", hasItems(positiveResult.toArray())))
            .shouldHave(bodyField("items.type", not(hasItems(negativeResult.toArray()))));
    }

    @Test
    @DisplayName("Get change logs with Filters: Affiliates, Offers")
    void getChangeLogsWithFiltersAffiliateOffers(){
        createKpiRuleWithOfferAffiliate(offer, affiliate);

        Map<String, Object> queryParameters = new HashMap<>();
        queryParameters.put(FILTER_OFFERS, offer.getId());
        queryParameters.put(FILTER_AFFILIATES, affiliate.getId());

        kpiService.getChangeLogs(generalManager.getApiKey(), queryParameters)
            .shouldHave(statusCode(200))
            .shouldHave(bodyField("items", is(not(empty()))))
            .shouldHave(bodyField("items[0].new_data.offers[0]", containsString(String.valueOf(offer.getId()))))
            .shouldHave(bodyField("items[0].new_data.affiliates[0]", containsString(String.valueOf(affiliate.getId()))));
    }

    ///-------------------------|   Negative   |-------------------------///

    @Negative @ParameterizedTest
    @MethodSource("api.affise.com.BeforeAllClasses.AutomationPreSetUp#usersProvider")
    @DisplayName("User without permission can not get change logs")
    void userWithNoAccessCanNotGetChangeLogs(User user) {
        kpiService.getChangeLogs(user.getApiKey(), dateFrom, dateTo)
            .shouldHave(statusCode(403))
            .shouldHave(bodyField(ERROR, equalTo(ACCESS_DENIED)));
    }

    @Test
    @DisplayName("User with partner api-key can not get change logs")
    void affiliateCanNotGetChangeLogs() {
        kpiService.getChangeLogs(affiliate.getAffiliate().getApiKey(), dateFrom, dateTo)
            .shouldHave(statusCode(403))
            .shouldHave(bodyField(ERROR, equalTo(AUTH_DENIED)));
    }

    @Test
    @DisplayName("User with no api-key can not get change logs")
    void userWithNoApiKeyCanNotGetChangeLogs() {
        kpiService.getChangeLogs(dateFrom, dateTo)
            .shouldHave(statusCode(401))
            .shouldHave(bodyField(ERROR, equalTo(TOKEN_IS_NECESSARY)));
    }

    ///-------------------------|   Providers   |-------------------------///

    static Stream<Arguments> typeDataProvider() {
        return Stream.of(
                arguments(Arrays.asList("delete", "add", "update"), Arrays.asList("delete", "add", "update"), Arrays.asList("")),
                arguments(Arrays.asList("delete"), Arrays.asList("delete"), Arrays.asList("add", "update")),
                arguments(Arrays.asList("add"), Arrays.asList("add"), Arrays.asList("delete", "update")),
                arguments(Arrays.asList("update"), Arrays.asList("update"), Arrays.asList("delete", "add"))
        );
    }

}
