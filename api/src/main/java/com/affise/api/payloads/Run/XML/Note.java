package com.affise.api.payloads.Run.XML;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

import javax.xml.bind.annotation.*;


@Slf4j
@Getter
@Setter
@Accessors(fluent = true)
@RequiredArgsConstructor
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "note")
@JacksonXmlRootElement(localName = "note")
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class Note
{

    @XmlElement(name = "date")
    @JacksonXmlProperty(localName = "date")
    private Date date;

    @XmlElement(name = "heading")
    @JacksonXmlProperty(localName = "heading")
    private String heading;

    @XmlElement(name = "from")
    @JacksonXmlProperty(localName = "from")
    private String from;

    @XmlElement(name = "to")
    @JacksonXmlProperty(localName = "to")
    private String to;

    @XmlElement(name = "body")
    @JacksonXmlProperty(localName = "body")
    private String body;

    @XmlAttribute(name = "class")
    @JacksonXmlProperty(localName = "class", isAttribute = true)
    private String clases;

    @XmlAttribute(name = "id")
    @JacksonXmlProperty(localName = "id", isAttribute = true)
    private Integer id;

    @Override
    public String toString() {
        return "Note{" +
                "date=" + date +
                ", heading='" + heading + '\'' +
                ", from='" + from + '\'' +
                ", to='" + to + '\'' +
                ", body='" + body + '\'' +
                ", clases='" + clases + '\'' +
                ", id=" + id +
                '}';
    }
}
