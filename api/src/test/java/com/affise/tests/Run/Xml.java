package com.affise.tests.Run;

import com.affise.api.annotations.Positive;
import com.affise.api.payloads.Run.XML.Date;
import com.affise.api.payloads.Run.XML.Messages;
import com.affise.api.payloads.Run.XML.Note;
import com.affise.api.payloads.Run.XML.Role;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import io.restassured.http.ContentType;
import lombok.SneakyThrows;
import org.testng.annotations.Test;

import java.util.Arrays;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class Xml {

    private final XmlMapper xmlMapper = new XmlMapper();
    private final String host = "https://ennwmrm5q19lr.x.pipedream.net/";

    @Positive @SneakyThrows
    @Test(description = "Create note with XML body")
    public void xmlAsObject() {
        Note first = new Note()
                .from("Dima")
                .to("Vlad")
                .heading("Hello")
                .body("I am in Minsk")
                .date(new Date().year("2018").role(new Role().value("Administrator")).id(20));

        Note second = new Note()
                .from("Misha")
                .to("Alexander")
                .heading("Hello!")
                .body("How are you?")
                .id(108).clases("remember")
                .date(new Date().day("12").year("2019").month("12").role(new Role().section("Manager").value("Administrator")));

        Messages messages = new Messages()
                .note(Arrays.asList(first, second))
                .type("Client Manager")
                .role(new Role().section("Client Manager"))
                .text("It is a note! Good day")
                .status(true)
                .categories(Arrays.asList("health","blockchain"));

        given().contentType(ContentType.XML).body(messages).when().post(host).then().statusCode(200);
    }


    @Positive @SneakyThrows
    @Test(description = "Create note with XML body")
    public void xmlAsString() {
        Note first = new Note()
                .from("Dima")
                .to("Vlad")
                .heading("Hello")
                .body("I am in Minsk")
                .date(new Date().year("2018").role(new Role().value("Administrator")).id(20));

        Note second = new Note()
                .from("Misha")
                .to("Alexander")
                .heading("Hello!")
                .body("How are you?")
                .id(108).clases("remember")
                .date(new Date().day("12").year("2019").month("12").role(new Role().section("Manager").value("Administrator")));

        Messages messages = new Messages()
                .note(Arrays.asList(first, second))
                .type("Client Manager")
                .status(true)
                .role(new Role().section("Client Manager"))
                .text("It is a note! Good day")
                .categories(Arrays.asList("health","blockchain"));

        String xml = xmlMapper.writeValueAsString(messages);

        given().contentType(ContentType.XML).body(xml).log().all().when().post(host).then().statusCode(200).log().all();
    }


    @Positive @SneakyThrows
    @Test(description = "Deserialization XML response body")
    public void xmlDeserialization() {
        String response = "<messages>" +
                "  <note>" +
                "    <date id=\"20\">" +
                "      <year>2018</year>" +
                "      <role>Administrator</role>" +
                "    </date>" +
                "    <heading>Hello</heading>" +
                "    <from>Dima</from>" +
                "    <to>Vlad</to>" +
                "    <body>I am in Minsk</body>" +
                "  </note>" +
                "  <note class=\"remember\" id=\"108\">" +
                "    <date>" +
                "      <month>12</month>" +
                "      <year>2019</year>" +
                "      <day>12</day>" +
                "      <role section=\"Manager\">Administrator</role>" +
                "    </date>" +
                "    <heading>Hello!</heading>" +
                "    <from>Misha</from>" +
                "    <to>Alexander</to>" +
                "    <body>How are you?</body>" +
                "  </note>" +
                "  <type>Client Manager</type>" +
                "  <status>true</status>" +
                "  <role section=\"Client Manager\"/>" +
                "  <text>It is a note! Good day</text>" +
                "  <categories>health</categories>" +
                "  <categories>blockchain</categories>" +
                "</messages>";

        Messages messages = xmlMapper.readValue(response, Messages.class);
        assertThat("Administrator", equalTo(messages.note().get(1).date().role().value()));
        assertThat("blockchain", equalTo(messages.categories().get(1)));
    }

}
