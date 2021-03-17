package api.helpers.db;

import api.automation.kpi.Kpi;
import api.automation.kpi.KpiRule;
import com.mongodb.*;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j;
import org.bson.types.ObjectId;
import org.mongojack.JacksonDBCollection;

import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static api.helpers.Helpers.getLastItemInt;
import static api.helpers.constants.ConstantsStrings.*;
import static api.helpers.db.ClientMysql.deleteItemMysql;
import static api.services.ApiService.getConfig;

@Log4j
@Getter
@Setter
public class Mongo {
    private static final String CONNECTED = "MongoClient connected";
    private MongoClient mongoClientLocal;
    private MongoClient mongoUsersCentral;
    private MongoClient mongoOffersCentral;
    private MongoClient mongoAffiliatesCentral;
    private MongoClient mongoAdvertisersCentral;
    private MongoClient mongoIntegrationStore;
    private MongoClient mongoPostbackProcessor;
    private MongoClient mongoPixelsCentral;
    private MongoClient mongoSmartlinksCentral;

    public Mongo() {
        this.mongoClientLocal = this.connect(getConfig().mongoHost(),getConfig().mongoPort());
        this.mongoUsersCentral = this.connect(getConfig().mongoUsers());
        this.mongoOffersCentral = this.connect(getConfig().mongoOffers());
        this.mongoAffiliatesCentral = this.connect(getConfig().mongoAffiliates());
        this.mongoAdvertisersCentral = this.connect(getConfig().mongoAdvertisers());
        this.mongoIntegrationStore = this.connect(getConfig().mongoIntegrationStore());
        this.mongoPostbackProcessor = this.connect(getConfig().mongoPostbackProcessor());
        this.mongoPixelsCentral = this.connect(getConfig().mongoIntegrationStore());
        this.mongoSmartlinksCentral = this.connect(getConfig().mongoSmartlinks());
    }

    private MongoClient connect(String host, int port) {
        MongoClient client = null;
        try {
            client = new MongoClient(host,port);
            log.info(CONNECTED); }
        catch (UnknownHostException e1) {

            log.error(MONGO_HOST_NOT_FOUND + e1.toString()); }
            return client;
         }

    @SneakyThrows
    private MongoClient connect(){
        return new MongoClient(getConfig().mongoHost(),getConfig().mongoPort());
    }
    @SneakyThrows
    private MongoClient connect(String uri){
        MongoClient client = null;
        MongoClientURI mongoClientURI = new MongoClientURI(uri);
        log.info(CONNECTED + mongoClientURI);
        try {

            client = new MongoClient(mongoClientURI);
            log.info(CONNECTED + client); }
        catch (UnknownHostException e1) {

            log.error(MONGO_HOST_NOT_FOUND + e1.toString()); }
        return client;
    }

    public List getItemsIdListCentral(MongoClient mongoClient, String db, String collection, String key) {
        DBObject query = new BasicDBObject(CLIENT_ID,getConfig().clientId());
        return mongoClient.getDB(db).getCollection(collection).distinct(key, query); }

    public List getItemsIdListFromLocal(String db, String collection, String key) {
        return mongoClientLocal.getDB(db).getCollection(collection).distinct(key);}

    public List getItemsFilteredList(MongoClient mongoClient, String db , String collection,
                                     String field, Object value) {
        BasicDBObject query = new BasicDBObject(field, value);
        return mongoClient.getDB(db).getCollection(collection).distinct("_id", query);
    }

    public DBObject getItemFromMongoById(MongoClient mongoClient, String db, String collection, String id){
        DBObject searchById = new BasicDBObject("_id", new ObjectId(id));
        return mongoClient.getDB(db).getCollection(collection).findOne(searchById); }

    public DBObject getMongoDocumentFiltered(MongoClient mongoClient, String db , String collection,
                                        String field, Object value) {
        DBObject document = null;
        BasicDBObject query = new BasicDBObject(field, value);

        try (DBCursor cursor = mongoClient.getDB(db).getCollection(collection).find(query)) {
            while (cursor.hasNext()) document = cursor.next();
        }
        return document;
    }
    public void deleteItemByIdCentral(MongoClient mongoClient, String db ,String collection,String itemId){

        DBObject searchById = new BasicDBObject("_id", new ObjectId(itemId));
        searchById.put(CLIENT_ID,getConfig().clientId());
        mongoClient.getDB(db).getCollection(collection).remove(searchById);
    }

    public void deleteItemByFieldCentral(MongoClient mongoClient, String db, String collection,String field, Object value){

            DBObject searchById = new BasicDBObject(field,value);
            searchById.put(CLIENT_ID,getConfig().clientId());
            mongoClient.getDB(db).getCollection(collection).remove(searchById);
        }

    public void deleteItemByIdLocal(MongoClient mongoClient, String db, String collection, String itemId){

            DBObject searchById = new BasicDBObject("_id", new ObjectId(itemId));
        mongoClient.getDB(db).getCollection(collection).remove(searchById);
    }

    public void deleteItemByFieldLocal(MongoClient mongoClient, String db, String collection, String field, Object value){

        DBObject searchById = new BasicDBObject(field,value);
        mongoClient.getDB(db).getCollection(collection).remove(searchById);
    }

    public void deleteItemByCriteria(MongoClient mongoClient, String db ,String collection,DBObject search){
        mongoClient.getDB(db).getCollection(collection).remove(search);
    }


    public static class MongoHelpers{

        public static final Mongo mongo = new Mongo();
        private static final String OFFER = "Offer: ";

        public static void deleteAdvertiserFully(String id){
            mongo.deleteItemByFieldLocal(mongo.getMongoClientLocal() ,DEFAULT_DB_NAME,
                    ADVERTISER_COLLECTION_LOCAL,ID_KEY, new ObjectId(id) );
            log.info("Deleted Advertiser From Central Mongo, id: " + id);

            mongo.deleteItemByFieldCentral(mongo.getMongoAdvertisersCentral(), ADVERTISER_DB, ADVERTISERS,
                    ID_KEY, new ObjectId(id));
            log.info("Deleted Advertiser From Local Mongo, id: " + id);
        }

        public static void deleteUsersFully(String id){
            Mongo mongo = new Mongo();
            mongo.deleteItemByIdCentral(mongo.getMongoUsersCentral(), USERS_DB, USERS,
                    id);
            log.info("User is deleted from Mongo Users Central, id=" + id);

            mongo.deleteItemByIdLocal(mongo.getMongoClientLocal(),DEFAULT_DB_NAME, USERS, id);
            log.info("User is deleted from Mongo Local, id=" + id);
        }

        public static void deleteUsersFully(String...id){
            Consumer<String> logCentral = user -> log.info("User is deleted from Mongo Users Central, id=" + user);
            Consumer<String> logLocal = user -> log.info("User is deleted from Mongo Local, id=" + user);
            Consumer<String> deleteCentral = user -> mongo.deleteItemByIdCentral(mongo.getMongoUsersCentral(), USERS_DB, USERS, user);
            Consumer<String> deleteLocal = user -> mongo.deleteItemByIdLocal(mongo.getMongoClientLocal(),DEFAULT_DB_NAME, USERS, user);

            Arrays.stream(id).forEach( x ->
                    deleteCentral
                        .andThen(logCentral)
                            .andThen(deleteLocal)
                                .andThen(logLocal)
                                    .accept(x));
        }

        public static void addKpiRulesToMongo(Kpi...kpi){
            JacksonDBCollection<Kpi, Object> jacksonDBCollection =
                    JacksonDBCollection.wrap(mongo.getMongoClientLocal().getDB(DEFAULT_DB_NAME).getCollection(AUTOMATION_KPI), Kpi.class);
            jacksonDBCollection.insert(kpi);
        }

        public static long getKpiRulesCount(){
            JacksonDBCollection<Kpi, Object> jacksonDBCollection =
                    JacksonDBCollection.wrap(mongo.getMongoClientLocal().getDB(DEFAULT_DB_NAME).getCollection(AUTOMATION_KPI), Kpi.class);
            return jacksonDBCollection.count();
        }

        public static boolean kpiRuleExistInMongo(ObjectId kpi){
            JacksonDBCollection<Kpi, Object> jacksonDBCollection =
                    JacksonDBCollection.wrap(mongo.getMongoClientLocal().getDB(DEFAULT_DB_NAME).getCollection(AUTOMATION_KPI), Kpi.class);
            if (jacksonDBCollection.findOneById(kpi) != null){
                log.info(String.format("Kpi rule '%s' exists in MongoDB", kpi.toString()));
                return true;
            } else {
                log.info(String.format("Kpi rule does not '%s' exists in MongoDB", kpi.toString()));
                return false;
            }
        }

        public static void deleteKpiRules(KpiRule...kpi){
            Consumer<KpiRule> deleteLocal = kpiRule ->
                    mongo.deleteItemByIdLocal(mongo.getMongoClientLocal(), DEFAULT_DB_NAME, AUTOMATION_KPI, kpiRule.kpi().id());
            Consumer<KpiRule> logLocal = kpiRule ->
                    log.info("KpiRule is deleted from Mongo Local, id=" + kpiRule.kpi().id());

            Arrays.stream(kpi).filter(x -> x.status() == 1).collect(Collectors.toList())
                    .forEach( x -> deleteLocal.andThen(logLocal).accept(x));
        }

        public static void deleteKpiRules(Kpi...kpi){
            Consumer<Kpi> deleteLocal = kpiRule ->
                    mongo.deleteItemByIdLocal(mongo.getMongoClientLocal(), DEFAULT_DB_NAME, AUTOMATION_KPI, kpiRule.mongoId().toString());
            Consumer<Kpi> logLocal = kpiRule ->
                    log.info("KpiRule is deleted from Mongo Local, id=" + kpiRule.mongoId().toString());

            Arrays.stream(kpi).forEach( x -> deleteLocal.andThen(logLocal).accept(x));
        }

        public static void deleteAffiliateFullyById(int affiliateId){

            mongo.deleteItemByFieldLocal(mongo.getMongoClientLocal(),
                    DEFAULT_DB_NAME, PARTNERS_COLLECTION, ID_KEY,affiliateId);
            log.info("Deleted Affiliate From Local Mongo: " + affiliateId);

            mongo.deleteItemByFieldCentral(mongo.getMongoAffiliatesCentral(), AFFILIATES_DB, AFFILIATES,
                    "id",affiliateId);
            log.info("Deleted Affiliate From Central Mongo: " + affiliateId);

            try {
                deleteItemMysql(PARTNER, affiliateId);
                log.info("Deleted From Mysql Partner: " + affiliateId);
            } catch (Exception e) {
                log.error("Exception = " + e.toString());
            }
        }

        public static void deleteOfferFully(int id){
            mongo.deleteItemByFieldLocal(mongo.getMongoClientLocal(),DEFAULT_DB_NAME,
                    OFFERS_COLLECTION_LOCAL,INT_ID, id );
            log.info(OFFER + id + "deleted from Local DB");
            mongo.deleteItemByFieldCentral(mongo.getMongoOffersCentral(), OFFERS_DB, OFFERS,
                    INT_ID, id);

            log.info(OFFER + id + "deleted from Central Offers DB");

            try {
                deleteItemMysql("cpa_offers", id);
                log.info(OFFER + id + "deleted from Mysql DB");
            } catch (Exception e) {
                log.error(e.toString());
            }
        }

        public static void deletePostbackFully(int postbackId){

            mongo.deleteItemByFieldCentral(mongo.getMongoPostbackProcessor(), INTEGRATION_DB , POSTBACKS,
                    INT_ID, postbackId);

            log.info("Deleted Postback From Central Mongo: " + postbackId);
        }

        public static void deleteSmartlinkFully(String id){
            mongo.deleteItemByIdLocal(mongo.getMongoClientLocal(), DEFAULT_DB_NAME, SMARTLINK_CATEGORIES_COLLECTION, id);
            log.info("Deleted from Local Db Smartlink: "  + id);
            mongo.deleteItemByIdCentral(mongo.getMongoSmartlinksCentral(), SMARTLINKS, SMARTLINKS, id);
            log.info("Deleted from Central Db Smartlink: "  + id);

        }

        public static DBObject getItemFromMongoByIdFromLocal(String collection, String id){
            return  mongo.getItemFromMongoById(mongo.getMongoClientLocal(),
                    DEFAULT_DB_NAME, collection, id);
        }

        public static List returnItemsIdListFromLocalDb(String collection, String key) {
            return mongo.getItemsIdListFromLocal(DEFAULT_DB_NAME, collection, key);
        }

        public static List returnItemsIdFilteredListFromLocal(String collection, String field, Object value) {
            return mongo.getItemsFilteredList(mongo.getMongoClientLocal(),
                    DEFAULT_DB_NAME, collection, field, value);
        }

        public static void deleteItemByIdFromLocalDb(String collection, String itemId){
            mongo.deleteItemByIdLocal(mongo.getMongoClientLocal(),
                    DEFAULT_DB_NAME, collection, itemId);}

        public static int getLastOfferIntId(){
            return getLastItemInt(
                    mongo.getItemsIdListCentral(mongo.getMongoOffersCentral(),
                            OFFERS_DB,OFFERS, INT_ID));
        }

        public String getFirstSmartlinkFromMongo() {
            return mongo.getItemsIdListCentral(mongo.getMongoSmartlinksCentral(), SMARTLINKS,
                    SMARTLINKS, ID_KEY).get(0).toString();
        }

        public static int getLastAffiliateId(){
            return getLastItemInt(mongo.getItemsIdListCentral(
                    mongo.getMongoAffiliatesCentral(), AFFILIATES_DB,AFFILIATES, "id"));
        }
    }

}
