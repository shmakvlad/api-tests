package com.affise.api.database;

import com.mongodb.MongoClientSettings;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.Arrays;

import static com.affise.api.config.Config.getConfig;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.combine;
import static com.mongodb.client.model.Updates.set;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;


public class ConnectToMongo {

//    private static MongoClient mongoClientBuilder = connectToMongo("localhost", 27017);
    private MongoClient mongoClient = MongoClients.create(getConfig().mongodb());

    public MongoClient getMongoClient() {
        return mongoClient;
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

    public void connectClose(){
        mongoClient.close();
    }

    public void closeConnection(){
        connectClose();
    }

    public void removeAffiliateByEmail(String email) {
        MongoCollection<Document> collection = mongoClient.getDatabase("admin").getCollection("partners");
        assertThat(collection.deleteOne(eq("email", email)).getDeletedCount(), equalTo(1L));
    }

    public void removeAffiliateById(Object id) {
        MongoCollection<Document> collection = mongoClient.getDatabase("admin").getCollection("partners");
        assertThat(collection.deleteOne(eq("_id", id)).getDeletedCount(), equalTo(1L));
    }

    public void removeObject(String dbName, String colName, String key, Object value) {
        MongoCollection<Document> collection = mongoClient.getDatabase(dbName).getCollection(colName);
        long d = collection.deleteOne(eq(key, new ObjectId((String) value))).getDeletedCount();
        assertThat(d, equalTo(1L));
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

    public void main(String[] args) {

//        updateAffiliateInMongo("_id", 78, "email", "ola3@gmail.com");
//        Advertiser advertiser = getNewAdvertiser();
//        Advertiser advertiser1 = getNewAdvertiser();
//        removeObject("admin", "suppliers", "_id", advertiser.id());
//        updateAffiliateInMongo("_id", 78, "email", "ola2@gmail.com");
//        removeObject("admin", "suppliers", "_id", advertiser1.id());
//        updateAffiliateInMongo("_id", 78, "email", "ola1new@gmail.com");
//        closeConnection();

//        collection.find().forEach(doc-> System.out.println(doc.toJson()));
//        collection.find(eq("email", null)).first().toJson();
//        collection.find(and(eq("email", null))).forEach(doc-> System.out.println(doc.get("title")));

    }



    public String connectUsers(String email){
        String apiKey=null;
        try{
            MongoDatabase mongoDatabase = mongoClient.getDatabase("admin");
            MongoCollection<Document> collection = mongoDatabase.getCollection("users");
            Document myDoc = collection.find(eq("email",email)).first();
            apiKey =(String)myDoc.get("api_key");
        }
        catch(Exception e){
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
        }
        return apiKey;
    }

    public String removeUser(String email) {
        String apiKey = null;
        try {
            MongoDatabase mongoDatabase = mongoClient.getDatabase("admin");
            MongoCollection<Document> collection = mongoDatabase.getCollection("users");
            collection.deleteOne(Filters.eq("email", email));
        } catch (Exception e) {
            System.out.println("Not faund users in database");
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
        return apiKey;
    }

    public String partnerApiKey(String email){
        String apiKey=null;
        try{
            MongoDatabase mongoDatabase = mongoClient.getDatabase("admin");
            MongoCollection<Document> collection = mongoDatabase.getCollection("partners");
            Document myDoc = collection.find(eq("email",email)).first();
            apiKey =(String)myDoc.get("api_key");
        }
        catch(Exception e){
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.out.println("error");
        }
        return apiKey;
    }
    public String userId(String email){
        String id=null;
        Document myDoc = null;
        try{
            MongoDatabase mongoDatabase = mongoClient.getDatabase("admin");
            MongoCollection<Document> collection = mongoDatabase.getCollection("users");
            myDoc = collection.find(eq("email",email)).first();
        }
        catch(Exception e){
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
        }
        return myDoc.get("_id").toString();
    }

}
