package com.affise.tests.Run;

import com.affise.api.database.ConnectToMongo;
import com.affise.api.payloads.MongoDB.*;
import com.affise.api.services.AdvertiserApiService;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import lombok.SneakyThrows;
import org.bson.Document;
import org.bson.UuidRepresentation;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.types.ObjectId;
import org.mongojack.JacksonMongoCollection;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.affise.api.conditions.Conditions.bodyField;
import static com.affise.api.conditions.Conditions.statusCode;
import static com.affise.api.config.Config.getConfig;
import static com.affise.api.constans.Constans.User.ADMIN;
import static com.affise.api.generatedata.Generations.*;
import static java.util.Arrays.asList;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItems;


public class ReadMongoDB {

    private final ConnectToMongo connectToMongo = new ConnectToMongo();
    private final AdvertiserApiService advertiserApiService = new AdvertiserApiService();

    MongoAdvertiser a1 = new MongoAdvertiser().title("Atletiko").id(new ObjectId("4e902dbe5bc5ef95a4696d15"));
    MongoAdvertiser a2 = new MongoAdvertiser().title("Granada").id(new ObjectId("8e902dbe5bc5ef95a4696d15"));
    MongoAdvertiser a3 = new MongoAdvertiser().title("Barselona").id(new ObjectId("6e902dbe5bc5ef95a4696d15"));

    @BeforeClass
    public void setUp() {
        connectToMongo.addAdvertisersToMongoDB(a1, a2, a3);
    }

    @Test
    public void getAdvertisers(){
        advertiserApiService.getListAdvertisers(ADMIN)
                .shouldHave(statusCode(200))
                .shouldHave(bodyField("advertisers.id", hasItems(a1.id().toString(), a2.id().toString(), a3.id().toString())))
                .shouldHave(bodyField("advertisers.title[0]", equalTo("Atletiko")))
                .shouldHave(bodyField("advertisers.title[1]", equalTo("Barselona")))
                .shouldHave(bodyField("advertisers.title[-1]", equalTo("Granada")));
    }

    @SneakyThrows @Test
    public void jsonToMongoDB(){
        String json = "{" +
                "    '_id' : ObjectId('5ed2cdca4d01fb781fc1aa16'), " +
                "    'sub_account_2_except' : false, " +
                "    'country' : '', " +
                "    'note' : '', " +
                "    'vat_code' : '', " +
                "    'address2' : '', " +
                "    'city' : '', " +
                "    'address1' : '', " +
                "    'created_at' : ISODate('2020-05-30T21:19:06.939Z'), " +
                "    'secure_code' : '', " +
                "    'title' : 'Atletiko', " +
                "    'client_id' : NumberLong(99999), " +
                "    'sub_account_1_except' : false, " +
                "    'url' : '', " +
                "    'zip_code' : '', " +
                "    'skype' : '', " +
                "    'sub_account_2' : '', " +
                "    'sub_account_1' : '', " +
                "    'updated_at' : ISODate('2020-12-12T09:12:12.123Z'), " +
                "    'user_id' : ObjectId('5e902dbe5bc5ef95a4696d15'), " +
                "    'contact' : '', " +
                "    'email' : '' " +
                "}";

        Document doc = Document.parse(json);
        MongoCollection<Document> collection = connectToMongo
                .getMongoClient().getDatabase("advertisers").getCollection("advertisers");
        collection.insertOne(doc);
    }

    @Test
    public void insertNewDocumentMap(){
        Map advertiserMap = new HashMap();
        advertiserMap.put("title", "Mallorca");
        advertiserMap.put("country", "ES");
        advertiserMap.put("skype", "Spain");

        Document advertiser = new Document(advertiserMap);
        MongoCollection<Document> collection = connectToMongo
                .getMongoClient().getDatabase("advertisers").getCollection("advertisers");

        collection.insertOne(advertiser);
    }

    @Test
    public void insertNewDocument(){
        Document advertiser = new Document("title", "Valencia")
                .append("country", "ES")
                .append("email", generateEmail())
                .append("url", generateUrl())
                .append("note", null)
                .append("client_id", 99999L)
                .append("sub_account_1", "")
                .append("sub_account_2", "")
                .append("sub_accounts", new Document()
                        .append("1", new Document()
                                .append("except", true)
                                .append("value", "sub1"))
                        .append("2", new Document()
                                .append("except", false)
                                .append("value", "sub2")))
                .append("sub_account_1_except", false)
                .append("sub_account_2_except", true)
                .append("address1", "Picasso")
                .append("address2", "49")
                .append("user_id", "49")
                .append("allowed_ip", asList("1.1.1.1","2.2.2.2"))
                .append("disallowed_ip", Collections.emptyList())
                .append("created_at", new Date())
                .append("updated_at", toISODate("2020-07-07T20:01:01.789Z"));

        MongoCollection<Document> collection = connectToMongo
                .getMongoClient().getDatabase("advertisers").getCollection("advertisers");

        collection.insertOne(advertiser);
    }

    @AfterClass
    public void cleanUp() {
        connectToMongo.removeObjectId("advertisers", "advertisers", "_id", a1.id(), a2.id(), a3.id());
    }



    ////---///---///---///---///---///---///---///---///---///---///---///---///---///---///---///---///---///---///---///---///---///---////



    @SneakyThrows
    @Test(description = "Central Advertiser / Create via Jackson Pojo / There is way to save key with null/default values")
    public void createAdvertiserJacksonCentral() {
        MongoClient mongoClient = MongoClients.create(getConfig().mongodb());
        MongoCollection<MongoAdvertiser> collection = JacksonMongoCollection.builder()
                .build(mongoClient, "advertisers", "advertisers", MongoAdvertiser.class, UuidRepresentation.STANDARD);

        MongoAdvertiser advertiserMongoDB = new MongoAdvertiser()
                .id(new ObjectId())
                .clientId(99999L)
                .title("Atletiko")
                .managerId(new ObjectId("5cd55442d596c1c7008b4567"))
                .email(generateEmail())
                .userId(new ObjectId("5e902dbe5bc5ef95a4696d15"))
                .updatedAt(toISODate("2020-12-12T12:12:12.123Z"));

        collection.insertOne(advertiserMongoDB);
        connectToMongo.removeObjectId("advertisers", "advertisers", "_id", advertiserMongoDB.id());
    }


    @SneakyThrows
    @Test(description = "Local Advertiser / Create via Jackson Pojo / There is way to save key with null value")
    public void createAdvertiserJackson() {
        MongoClient mongoClient = MongoClients.create(getConfig().mongodb());
        MongoCollection<AdvertiserMongoDB> collection = JacksonMongoCollection.builder()
                .build(mongoClient, "admin", "suppliers", AdvertiserMongoDB.class, UuidRepresentation.STANDARD);

        AdvertiserMongoDB advertiserMongoDB = new AdvertiserMongoDB()
                .id(new ObjectId())
                .title("Selta")
                .country("BY")
                .address1("Minsk").address2(null)
                .disabledChoicePostbackStatus(true)
                .manager(new ObjectId("5e902dbe5bc5ef95a4696d15"))
                .allowedIp(asList("1.1.1.1","2.2.2.2"))
                .disallowedIp(Collections.emptyList())
                .subAccounts(new SubAccounts()
                        .sub1(new SubAccount().value("sub1").except(true))
                        .sub2(new SubAccount().value("sub2").except(false)))
                .date(toISODate("2020-05-05T20:01:01.789Z"))
                .updatedAt(toISODate("2020-05-05T20:01:01.789Z"))
                .contact("")
                .note(asList(11, 22d, 33L))
                .email("hello@gmail.com")
                .hashPassword(new String());

        AdvertiserMongoDB advertiserMongoDB1 = new AdvertiserMongoDB()
                .id(new ObjectId())
                .date(toISODate("2020-05-06T21:01:01.123Z"))
                .updatedAt(toISODate("2020-05-06T21:01:01.123Z"))
                .title("Barselona");

        AdvertiserMongoDB advertiserMongoDB2 = new AdvertiserMongoDB()
                .id(new ObjectId())
                .date(new Date())
                .updatedAt(new Date())
                .title("Sevilia");

        collection.insertMany(asList(advertiserMongoDB, advertiserMongoDB1, advertiserMongoDB2));
    }


    @Test(description = "Local Advertiser / Create via Mongo Pojo / No way to save key with null value")
    public void createAdvertiserPOJOs() {
        ConnectionString connectionString = new ConnectionString(getConfig().mongodb());
        CodecRegistry pojoCodecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),
                fromProviders(PojoCodecProvider.builder().automatic(true).build()));
        MongoClientSettings clientSettings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .codecRegistry(pojoCodecRegistry)
                .build();
        MongoClient mongoClient = MongoClients.create(clientSettings);

        MongoCollection<AdvertiserPogo> collection = mongoClient.getDatabase("admin")
                .getCollection("suppliers", AdvertiserPogo.class);

        AdvertiserPogo advertiserMongoDB = new AdvertiserPogo()
                .setId(new ObjectId())
                .setTitle("Barcelona")
                .setCountry("ES")
                .setDisabledChoicePostbackStatus(true)
                .setAddress1("123").setAddress2(null).setContact(null)
                .setManager(new ObjectId("5e902dbe5bc5ef95a4696d15"))
                .setEmail(new String()).setNote("")
                .setDate(new Date()).setUpdatedAt(new Date())
                .setAllowedIp(Collections.emptyList())
                .setDisallowedIp(asList("1.1.1.1","2.2.2.2"));

        collection.insertOne(advertiserMongoDB);
    }

}
