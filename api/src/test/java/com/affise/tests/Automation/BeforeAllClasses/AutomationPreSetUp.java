package api.affise.com.automationTests;

import api.affiliates.AffiliateAdd;
import api.affise.com.BaseTestSetup;
import api.offers.admin.additional_classes.Offer;
import api.services.KpiAutomationApiService;
import api.users.User;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.parallel.Execution;

import java.util.Arrays;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Stream;

import static api.helpers.AffiliateHelper.generatedMinAffiliateParams;
import static api.helpers.OfferHelper.getOfferWithGoalsInPayouts;
import static api.helpers.constants.ConstantsStrings.*;
import static api.helpers.db.Mongo.MongoHelpers.*;
import static api.services.AffiliateApiService.AffiliateHelpers.createAffiliate;
import static api.services.UserApiService.UsersHelpers.createUserStatic;
import static org.junit.jupiter.api.extension.ExtensionContext.Namespace.GLOBAL;

public class AutomationPreSetUp extends BaseTestSetup
        implements BeforeAllCallback, ExtensionContext.Store.CloseableResource {

    private static boolean started = false;
    final static Lock lock = new ReentrantLock();

    protected static KpiAutomationApiService kpiService;
    protected static User generalManager;
    protected static User affiliateManager;
    protected static User salesManager;
    protected static Offer offer;
    protected static AffiliateAdd affiliate;

    @Override
    public void beforeAll(ExtensionContext context) {
        lock.lock();
        if (!started) {
            started = true;
            generateKpiPreSetupData();
            context.getRoot().getStore(GLOBAL).put("call close", this);
        }
        lock.unlock();
    }

    @Override
    public void close() {
        deleteKpiPreSetupData();
    }

    public void generateKpiPreSetupData(){
        offer = getOfferWithGoalsInPayouts(Arrays.asList("1", "2"));
        kpiService = new KpiAutomationApiService();
        generalManager = createUserStatic(ROLE_ADMIN).getUser();
        affiliateManager = createUserStatic(ROLE_AFF_MANAGER).getUser();
        salesManager = createUserStatic(ROLE_SALES_MANAGER).getUser();
        affiliate = createAffiliate(generatedMinAffiliateParams());
    }

    public void deleteKpiPreSetupData(){
        deleteAffiliateFullyById(affiliate.getId());
        deleteUsersFully(generalManager.getId());
        deleteUsersFully(affiliateManager.getId());
        deleteUsersFully(salesManager.getId());
        deleteOfferFully(offer.getId());
    }

    ///-------------------------|   Providers   |-------------------------///

    public static Stream<User> usersProvider() {
        return Stream.of(
                affiliateManager,
                salesManager
        );
    }

}
