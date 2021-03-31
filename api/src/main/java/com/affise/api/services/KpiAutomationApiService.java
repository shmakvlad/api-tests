package api.services;

import api.affiliates.AffiliateAdd;
import api.assertions.AssertableResponse;
import api.automation.kpi.KpiRule;
import api.conditions.Conditions;
import api.end_points.EndPoints;
import api.offers.admin.OfferAdmin;
import api.offers.admin.additional_classes.Offer;
import com.github.javafaker.Faker;

import java.util.Map;

import static api.helpers.AutomationHelper.getKpiRule;
import static api.helpers.constants.ConstantsStrings.*;
import static api.helpers.constants.HelperCredentials.ROOT_ADMIN_KEY;
import static api.helpers.enums.TimePeriod.HOUR;
import static org.hamcrest.core.IsEqual.equalTo;

public class KpiAutomationApiService extends ApiService{

    public AssertableResponse createKpiRule(String apiKey, Map<String, Object> body){
        return new AssertableResponse(setup(apiKey)
                .formParams(body)
                .post(EndPoints.kpiAddGet));
    }

    public AssertableResponse createKpiRule(Map<String, Object> body){
        return new AssertableResponse(setup()
                .formParams(body)
                .post(EndPoints.kpiAddGet));
    }

    public AssertableResponse getKpiRulesList(String apiKey, Map<String, Object> body){
        return new AssertableResponse(setup(apiKey)
                .queryParams(body)
                .get(EndPoints.kpiAddGet));
    }

    public AssertableResponse getKpiRulesList(String apiKey){
        return new AssertableResponse(setup(apiKey)
                .get(EndPoints.kpiAddGet));
    }

    public AssertableResponse getKpiRulesList(){
        return new AssertableResponse(setup()
                .get(EndPoints.kpiAddGet));
    }

    public AssertableResponse deleteKpiRule(String apiKey, String mongoId){
        return new AssertableResponse(setup(apiKey)
                .delete(EndPoints.kpiEditDelete, mongoId));
    }

    public AssertableResponse deleteKpiRule(String mongoId){
        return new AssertableResponse(setup()
                .delete(EndPoints.kpiEditDelete, mongoId));
    }

    public AssertableResponse editKpiRule(String apiKey, String mongoId, Map<String, Object> body){
        return new AssertableResponse(setup(apiKey)
                .formParams(body)
                .post(EndPoints.kpiEditDelete, mongoId));
    }

    public AssertableResponse editKpiRule(String mongoId, Map<String, Object> body){
        return new AssertableResponse(setup()
                .formParams(body)
                .post(EndPoints.kpiEditDelete, mongoId));
    }

    public AssertableResponse editKpiRule(String mongoId){
        return new AssertableResponse(setup()
                .post(EndPoints.kpiEditDelete, mongoId));
    }

    public AssertableResponse getChangeLogs(String apiKey, String dateFrom, String dateTo){
        return new AssertableResponse(setup(apiKey)
                .queryParam(DATE_FROM, dateFrom)
                .queryParam(DATE_TO, dateTo)
                .get(EndPoints.kpiGetChangeLogs));
    }

    public AssertableResponse getChangeLogs(String apiKey, Map<String, Object> parameters){
        return new AssertableResponse(setup(apiKey)
                .queryParams(parameters)
                .get(EndPoints.kpiGetChangeLogs));
    }

    public AssertableResponse getChangeLogs(String dateFrom, String dateTo){
        return new AssertableResponse(setup()
                .queryParam(DATE_FROM, dateFrom)
                .queryParam(DATE_TO, dateTo)
                .get(EndPoints.kpiGetChangeLogs));
    }

    public AssertableResponse getBlockLogs(String apiKey, String dateFrom, String dateTo){
        return new AssertableResponse(setup(apiKey)
                .queryParam(DATE_FROM, dateFrom)
                .queryParam(DATE_TO, dateTo)
                .get(EndPoints.kpiGetBlockLogs));
    }

    public AssertableResponse getBlockLogs(String dateFrom, String dateTo){
        return new AssertableResponse(setup()
                .queryParam(DATE_FROM, dateFrom)
                .queryParam(DATE_TO, dateTo)
                .get(EndPoints.kpiGetBlockLogs));
    }

    public AssertableResponse getBlockLogs(String apiKey, Map<String, Object> parameters){
        return new AssertableResponse(setup(apiKey)
                .queryParams(parameters)
                .get(EndPoints.kpiGetBlockLogs));
    }

    public static class KpiAutomationHelper {
        private static final KpiAutomationApiService kpiAutomationApiService = new KpiAutomationApiService();

        public static KpiRule createKpiRuleWithOfferAffiliate(Offer offer, AffiliateAdd affiliate) {
            Map<String, Object> body = getKpiRule(offer.getId(), HOUR.getTitle(), BLOCK, null, 25, null,
                offer.getPayments().get(0).getGoal(), offer.getPayments().get(1).getGoal(), null, affiliate.getId());
            return createKpiRule(body);
        }

        public static KpiRule createKpiRule(Map<String, Object> body) {
            return kpiAutomationApiService.createKpiRule(ROOT_ADMIN_KEY, body)
                    .shouldHave(Conditions.statusCode(200))
                    .shouldHave(Conditions.bodyField(STATUS, equalTo(1)))
                    .asClass(KpiRule.class);
        }
    }

}
