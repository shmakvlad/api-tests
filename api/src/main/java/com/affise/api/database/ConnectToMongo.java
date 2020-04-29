package com.affise.api.database;

import com.mongodb.MongoClientSettings;
import com.mongodb.ServerAddress;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.affise.api.config.Config.getConfig;
import static com.affise.api.constans.Constans.DatabaseNames.LOCAL_DB;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

@Slf4j
@Getter
@Setter
public class ConnectToMongo {

//    private MongoClient mongoClientBuilder = connectToMongo("localhost", 27017);

    private MongoClient mongoClient;
    private MongoClient mongoClientCentralUsers;

    public ConnectToMongo() {
        this.mongoClient = MongoClients.create(getConfig().mongodb());
        this.mongoClientCentralUsers = MongoClients.create(getConfig().mongodbCentralUsers());
    }

    public MongoClient connectToMongo(String host, int port){
        mongoClient = MongoClients.create(
                MongoClientSettings.builder()
                        .applyToClusterSettings(builder ->
                                builder.hosts(Arrays.asList(
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

    public void removeAffiliateById(Object id) {
        MongoCollection<Document> collection = mongoClient.getDatabase("admin").getCollection("partners");
        assertThat(collection.deleteOne(eq("_id", id)).getDeletedCount(), equalTo(1L));
        log.info("Affiliates {} successfully delete from MongoDB", id);
    }

    public void removeObject(String dbName, String colName, String key, Object value) {
        MongoCollection<Document> collection = mongoClient.getDatabase(dbName).getCollection(colName);
        long d = collection.deleteOne(eq(key, new ObjectId((String) value))).getDeletedCount();
        assertThat(d, equalTo(1L));
        log.info("Object {} successfully delete from MongoDB", value);
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
        MongoCollection<Document> collection = mongoClientCentralUsers.getDatabase("users").getCollection("users");
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
        MongoCollection<Document> collection = mongoClientCentralUsers.getDatabase("users").getCollection("users");
        collection.updateOne(
                eq(findKey, new ObjectId((String) findValue)),
                combine(set(updateKey, updateValue)));
    }

    public void updateUserInCentralMongoAddToSet(String findKey, Object findValue, String updateKey, Object updateValue){
        MongoCollection<Document> collection = mongoClientCentralUsers.getDatabase("users").getCollection("users");
        collection.updateOne(
                eq(findKey, new ObjectId((String) findValue)),
                combine(addToSet(updateKey, updateValue)));
    }

    public void updateUserInCentralMongoUnset(String findKey, Object findValue, String updateKey){
        MongoCollection<Document> collection = mongoClientCentralUsers.getDatabase("users").getCollection("users");
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

    @SneakyThrows
    public void existsAffiliateInCentralMongo(Integer partnerId){
        MongoCollection<Document> collection = mongoClient.getDatabase("affiliates").getCollection("affiliates");
        if (collection.find(eq("id", partnerId)).first() != null){
            log.info("Affiliate {} exists in central MongoDB", partnerId);
        } else {
            log.info("Affiliate {} not exists in central MongoDB", partnerId);
            throw new IOException("affiliate not saved in central MongoDB");
        }
    }

    @SneakyThrows
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

}
