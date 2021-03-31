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

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import static api.conditions.Conditions.bodyField;
import static api.conditions.Conditions.statusCode;
import static api.helpers.Helpers.getDateString;
import static api.helpers.constants.ConstantsStrings.*;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.params.provider.Arguments.arguments;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith({AutomationPreSetUp.class})
public class GetBlockLogs extends AutomationPreSetUp {

    private static final String dateFrom = getDateString(0);
    private static final String dateTo = getDateString(0);

    ///-------------------------|   Positive   |-------------------------///

    @Positive @Test
    @DisplayName("User can get block logs SMOKE")
    void getBlockLogsSmoke(){
        kpiService.getBlockLogs(generalManager.getApiKey(), dateFrom, dateTo)
            .shouldHave(statusCode(200))
            .shouldHave(bodyField(STATUS, equalTo(1)))
            .shouldHave(bodyField(PAGINATION_PAGE, equalTo(1)))
            .shouldHave(bodyField(PAGINATION_PER_PAGE, equalTo(10)));
    }

    @Test
    @DisplayName("User can get block logs MAT")
    void getBlockLogsMat(){
        Map<String, Object> queryParameters = new HashMap<>();
        queryParameters.put(PAGE, 1);
        queryParameters.put(LIMIT, 100);
        queryParameters.put(DATE_FROM, dateFrom);
        queryParameters.put(DATE_TO, dateTo);
        queryParameters.put(FILTER_OFFERS, offer.getId());
        queryParameters.put(FILTER_AFFILIATES, affiliate.getId());
        queryParameters.put(FILTER_SUB_INDEX, EMPTY_STRING);
        queryParameters.put(FILTER_SUB_VALUE, EMPTY_STRING);
        queryParameters.put(FILTER_ACTION, EMPTY_STRING);

        kpiService.getBlockLogs(generalManager.getApiKey(), queryParameters)
            .shouldHave(statusCode(200))
            .shouldHave(bodyField(PAGINATION_PAGE, equalTo(1)))
            .shouldHave(bodyField(PAGINATION_PER_PAGE, equalTo(100)))
            .shouldHave(bodyField("items", is((empty()))));
    }

    ///-------------------------|   Negative   |-------------------------///

    @Negative @ParameterizedTest
    @MethodSource("api.affise.com.BeforeAllClasses.AutomationPreSetUp#usersProvider")
    @DisplayName("User without permission can not get block logs")
    void userWithNoAccessCanNotGetBlockLogs(User user) {
        kpiService.getBlockLogs(user.getApiKey(), dateFrom, dateTo)
            .shouldHave(statusCode(403))
            .shouldHave(bodyField(ERROR, equalTo(ACCESS_DENIED)));
    }

    @Test
    @DisplayName("User with partner api-key can not get block logs")
    void affiliateCanNotGetBlockLogs() {
        kpiService.getBlockLogs(affiliate.getAffiliate().getApiKey(), dateFrom, dateTo)
            .shouldHave(statusCode(403))
            .shouldHave(bodyField(ERROR, equalTo(AUTH_DENIED)));
    }

    @Test
    @DisplayName("User with no api-key can not get block logs")
    void userWithNoApiKeyCanNotGetBlockLogs() {
        kpiService.getBlockLogs(dateFrom, dateTo)
            .shouldHave(statusCode(401))
            .shouldHave(bodyField(ERROR, equalTo(TOKEN_IS_NECESSARY)));
    }

    @ParameterizedTest
    @MethodSource("filterDataProvider")
    @DisplayName("User can not get block logs without required filter")
    void userCanNotGetBlockLogsWithoutFilter(String filterName, String filterValue, String message) {
        Map<String, Object> queryParameters = new HashMap<>();
        queryParameters.put(filterName, filterValue);

        kpiService.getBlockLogs(generalManager.getApiKey(), queryParameters)
            .shouldHave(statusCode(400))
            .shouldHave(bodyField(ERROR, equalTo(message)));
    }

    ///-------------------------|   Providers   |-------------------------///

    static Stream<Arguments> filterDataProvider() {
        return Stream.of(
                arguments(DATE_FROM, dateFrom, "[filter].[date_to].data: Not valid: 'date_to' is required"),
                arguments(DATE_TO, dateTo, "[filter].[date_from].data: Not valid: 'date_from' is required")
        );
    }

}
