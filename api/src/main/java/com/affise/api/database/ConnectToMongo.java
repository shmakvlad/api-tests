package com.affise.api.database;

import com.affise.api.payloads.MongoDB.MongoAdvertiser;
import com.mongodb.MongoClientSettings;
import com.mongodb.ServerAddress;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import io.qameta.allure.Step;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.bson.Document;
import org.bson.UuidRepresentation;
import org.bson.types.ObjectId;
import org.mongojack.JacksonMongoCollection;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.affise.api.config.Config.getConfig;
import static com.affise.api.constans.Constans.DatabaseNames.LOCAL_DB;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.*;
import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

@Slf4j
@Getter
@Setter
public class ConnectToMongo {

//    private MongoClient mongoClientBuilder = connectToMongo("localhost", 27017);

    private MongoClient mongoClient;
    private MongoClient mongoClientCentral;
    private MongoClient mongoClientAdvertisers;
    private MongoClient mongoClientOffers;
    private MongoClient mongoClientAffiliates;
    private MongoClient mongoClientSmartlinks;
    private MongoClient mongoClientUsers;

    public ConnectToMongo() {
        this.mongoClient = MongoClients.create(getConfig().mongodb());
        this.mongoClientCentral = MongoClients.create(getConfig().mongodbCentral());
        this.mongoClientAdvertisers = MongoClients.create(getConfig().mongodbCentralAdvertisers());
        this.mongoClientOffers = MongoClients.create(getConfig().mongodbCentralOffers());
        this.mongoClientAffiliates = MongoClients.create(getConfig().mongodbCentralAffiliates());
        this.mongoClientSmartlinks = MongoClients.create(getConfig().mongodbCentralSmartlinks());
        this.mongoClientUsers = MongoClients.create(getConfig().mongodbCentralUsers());
    }

    public MongoClient connectToMongo(String host, int port){
        mongoClient = MongoClients.create(
                MongoClientSettings.builder()
                        .applyToClusterSettings(builder ->
                                builder.hosts(asList(
                                        new ServerAddress(host, port))))
                        .build());
        return mongoClient;
    }

    public void closeConnection(){
        mongoClient.close();
    }

    public void removeAffiliateByEmail(String email) {
        MongoCollection<Document> collection = mongoClient.getDatabase("admin").getCollection("partners");
        assertThat(collection.deleteOne(eq("email", email)).getDeletedCount(), equalTo(1L));
        log.info("Affiliates {} successfully delete from MongoDB", email);
    }

    @Step
    public void removeAffiliateById(Object id) {
        MongoCollection<Document> collection = mongoClient.getDatabase("admin").getCollection("partners");
        assertThat(collection.deleteOne(eq("_id", id)).getDeletedCount(), equalTo(1L));
        log.info("Affiliates {} successfully delete from MongoDB", id);
    }

    @Step
    public void removeAffiliateByIdCentral(Object id) {
        MongoCollection<Document> collection = mongoClientAffiliates.getDatabase("affiliates").getCollection("affiliates");
        assertThat(collection.deleteOne(eq("id", id)).getDeletedCount(), equalTo(1L));
        log.info("Affiliates {} successfully delete from MongoDB", id);
    }

    public void removeObject(String dbName, String colName, String key, Object value) {
        MongoCollection<Document> collection = mongoClient.getDatabase(dbName).getCollection(colName);
        long d = collection.deleteOne(eq(key, new ObjectId((String) value))).getDeletedCount();
        assertThat(d, equalTo(1L));
        log.info("Object {} successfully delete from MongoDB", value);
    }

    public void addAdvertisersToMongoDB(MongoAdvertiser... mongoAdvertisers){
        MongoCollection<MongoAdvertiser> collection = JacksonMongoCollection.builder()
                .build(mongoClientCentral, "advertisers", "advertisers", MongoAdvertiser.class, UuidRepresentation.STANDARD);
        collection.insertMany(asList(mongoAdvertisers));
    }

    public void removeObjectId(String dbName, String colName, String key, ObjectId...value) {
        MongoCollection<Document> collection = mongoClientCentral.getDatabase(dbName).getCollection(colName);
        for (ObjectId id : value){
            long d = collection.deleteOne(eq(key, id)).getDeletedCount();
            assertThat(d, equalTo(1L));
            log.info("Object {} successfully delete from MongoDB", id);
        }
    }

    public MongoCollection<Document> getCollection(String colName){
        return mongoClient.getDatabase("admin").getCollection(colName);
    }

    public void updateAffiliateInMongo(String findKey, Object findValue, String updateKey, Object updateValue){
        MongoCollection<Document> collection = getCollection("partners");
        collection.updateOne(
                eq(findKey, findValue),
                combine(set(updateKey, updateValue)));
    }

    public void updateUserInMongo(String findKey, Object findValue, String updateKey, Object updateValue){
        MongoCollection<Document> collection = mongoClientCentral.getDatabase("users").getCollection("users");
        collection.updateOne(
                eq(findKey, new ObjectId((String) findValue)),
                combine(set(updateKey, updateValue)));

        collection.findOneAndUpdate(
                eq(findKey, new ObjectId((String) findValue)),
                combine(addToSet(updateKey, updateValue)));

        collection.findOneAndUpdate(
                eq(findKey, new ObjectId((String) findValue)),
                combine(unset(updateKey)));

        collection.findOneAndUpdate(
                eq(findKey, new ObjectId((String) findValue)),
                combine(push(updateKey, updateValue)));

        collection.findOneAndUpdate(
                eq(findKey, new ObjectId((String) findValue)),
                combine(pull(updateKey, new Document("$lt", updateValue))));
    }

    @SneakyThrows
    public void existsUserInMongo(){
        MongoCollection<Document> collection = mongoClient.getDatabase("admin").getCollection("suppliers");
        if (collection.find(eq("_id", new ObjectId("5bc9d7c16d73e41c008b4567"))).first() != null){
            log.info("User {} exists in MongoDB");
        } else {
            log.info("User {} not exists in MongoDB");
            throw new IOException("User not saved in MongoDB");
        }

        ArrayList col = collection.find(eq("_id", new ObjectId("5bc9d7c16d73e41c008b4567"))).into(new ArrayList<>());
        log.info("Collection in MongoDB: {}", col);
        log.info("Collection in MongoDB is empty: {}", col.isEmpty());
    }

    public void updateUserInCentralMongo(String findKey, Object findValue, String updateKey, Object updateValue){
        MongoCollection<Document> collection = mongoClientCentral.getDatabase("users").getCollection("users");
        collection.updateOne(
                eq(findKey, new ObjectId((String) findValue)),
                combine(set(updateKey, updateValue)));
    }

    public void updateUserInCentralMongoAddToSet(String findKey, Object findValue, String updateKey, Object updateValue){
        MongoCollection<Document> collection = mongoClientCentral.getDatabase("users").getCollection("users");
        collection.updateOne(
                eq(findKey, new ObjectId((String) findValue)),
                combine(addToSet(updateKey, updateValue)));
    }

    public void updateUserInCentralMongoUnset(String findKey, Object findValue, String updateKey){
        MongoCollection<Document> collection = mongoClientCentral.getDatabase("users").getCollection("users");
        collection.findOneAndUpdate(
                eq(findKey, new ObjectId((String) findValue)),
                combine(unset(updateKey)));
    }

    public void updateAdvertiserInMongo(String findKey, Object findValue, String updateKey, Object updateValue){
        MongoCollection<Document> collection = mongoClient.getDatabase("admin").getCollection("suppliers");
        collection.updateOne(
                eq(findKey, new ObjectId((String) findValue)),
                combine(set(updateKey, new ObjectId((String) updateValue))));
    }

    public void updateAdvertiserInCentralMongo(String findKey, Object findValue, String updateKey, Object updateValue){
        MongoCollection<Document> collection = mongoClient.getDatabase("advertisers").getCollection("advertisers");
        collection.updateOne(
                eq(findKey, new ObjectId((String) findValue)),
                combine(set(updateKey, new ObjectId((String) updateValue))));
    }

    @SneakyThrows @Step
    public void existsAffiliateInCentralMongo(Integer partnerId){
        MongoCollection<Document> collection = mongoClientAffiliates.getDatabase("affiliates").getCollection("affiliates");
        if (collection.find(eq("id", partnerId)).first() != null){
            log.info("Affiliate {} exists in central MongoDB", partnerId);
        } else {
            log.info("Affiliate {} not exists in central MongoDB", partnerId);
            throw new IOException("affiliate not saved in central MongoDB");
        }
    }

    @SneakyThrows @Step
    public void existsAffiliateInMongo(Integer partnerId){
        FindIterable<Document> iterable = mongoClient.getDatabase("admin")
                .getCollection("partners").find(new Document("_id", partnerId));
        if (iterable.first() != null) {
            log.info("Affiliate {} exists in MongoDB", partnerId);
        }
        else {
            log.info("Affiliate {} not exists in MongoDB", partnerId);
            throw new IOException("affiliate not saved in MongoDB");
        }
    }

    public String getFirstObjectFromMongo(String collection, String key) {
        return returnItemsIdList(LOCAL_DB, collection, key).get(0).toString();
    }

    public List returnItemsIdList(String db, String collection, String key) {
        Assertions.assertThat(mongoClient.getDatabase(db).getCollection(collection).countDocuments()).isNotZero();

        if (key.equals("int_id")) return mongoClient.getDatabase(db).getCollection(collection).distinct(key, Integer.class)
                    .into(new ArrayList<>());
        else if (key.equals("_id")) return mongoClient.getDatabase(db).getCollection(collection).distinct(key, ObjectId.class)
                .into(new ArrayList<>());
        else return mongoClient.getDatabase(db).getCollection(collection).distinct(key, String.class)
                .into(new ArrayList<>());
    }

    ///-------------------------|   Helpers   |-------------------------///

    public void distinct(String db, String collection, String key) {
//        // 1
//        List list = new ArrayList<String>();
//        mongoClient.getDatabase(db).getCollection(collection).distinct(key, String.class).forEach(list::add);
//        System.out.println(list);


//        // 2
//        List list1 = mongoClient.getDatabase(db).getCollection(collection).distinct(key, String.class).into(new ArrayList<>());
//        System.out.println(list1); System.out.println(list1.get(0)); System.out.println(list1.get(list1.size()-1));


//        // 3
//        System.out.println(mongoClient.getDatabase(db).getCollection(collection).distinct(key, String.class).first());


//        // 4
//        for (String s : mongoClient.getDatabase(db).getCollection(collection).distinct(key, String.class)) {
//            System.out.println(s);
//        }


//        // 5
//        System.out.println(mongoClient.getDatabase(db).getCollection(collection).distinct(key, eq("title", "Kreiger - Harber"), String.class).first());


//        // 6
//        for (String s : mongoClient.getDatabase(db).getCollection(collection).distinct(key, eq("title", "test11151"), String.class)){
//            System.out.println(s);
//        }


//        // 7
//        mongoClient.getDatabase(db).getCollection(collection).distinct(key, String.class).filter(eq("title", "123")).first();


//        // 8
//        Bson bson = eq("title", "test11151");
//        System.out.println(mongoClient.getDatabase(db).getCollection(collection).distinct(key, bson, String.class).into(new ArrayList<>()));
//        System.out.println(mongoClient.getDatabase(db).getCollection(collection).distinct(key, bson, String.class).into(new ArrayList<>()).get(0));
//        System.out.println(mongoClient.getDatabase(db).getCollection(collection).distinct(key, bson, String.class).first());


//        // 9 ( Different filter types )

          // Bson filter
//        Bson bson = eq("title", "Kreiger - Harber");
//        System.out.println("Bson filter: " + mongoClient.getDatabase(db).getCollection(collection).distinct(key, bson, String.class).first());

          // Document filter
//        Document document = new Document().append("title", "Kreiger - Harber");
//        System.out.println("Document filter: " + mongoClient.getDatabase(db).getCollection(collection).distinct(key, document, String.class).first());

          // BasicDBObject filter
//        BasicDBObject query = new BasicDBObject("title", "Kreiger - Harber");
//        System.out.println("BasicDBObject filter: " + mongoClient.getDatabase(db).getCollection(collection).distinct(key, query, String.class).first());


          // 10 ObjectId
//        for (ObjectId s : mongoClientLocal.getDatabase(db).getCollection(collection).distinct(key, ObjectId.class)){
//            System.out.println(s);
//        }

    }

    public void find(String db, String collection, String key) {
//        // 1
//        for (Document document : mongoClient.getDatabase(db).getCollection(collection).find(empty())){
//            System.out.println(document);
//        }


//        // 2
//        for (Document document : mongoClient.getDatabase(db).getCollection(collection).find()){
//            System.out.println(document.toJson());
//        }


//        // 3
//        List list = new ArrayList<String>();
//        for (Document document : mongoClient.getDatabase(db).getCollection(collection).find()){

//            // create collection
//            list.add(document.getString("title"));

//            // extract path
//            try {
//                Document path = (Document) document.get("sub_accounts");
//                System.out.println(path.get("1"));
//                System.out.println(path.toJson());
//            } catch (Exception e) {
//                e.printStackTrace();
//            }

//            // other methods
//            System.out.println(document.getInteger("shave.mode"));
//            System.out.println(document.get("sub_accounts"));
//            System.out.println(document.getObjectId("_id"));
//            System.out.println(document.containsKey("tags"));
//            System.out.println(document.containsValue("AG"));
//            System.out.println(document.getBoolean("consider_personal_targeting_only"));
//            System.out.println(document.entrySet());
//            System.out.println(document.getDate("updated_at"));
//            System.out.println(document.isEmpty());
//            System.out.println(document.size());
//            System.out.println(document.values());
//        }
//        System.out.println(list);


//        // 4

    }

}
