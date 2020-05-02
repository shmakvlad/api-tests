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
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "date")
@JacksonXmlRootElement(localName = "date")
@RequiredArgsConstructor
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class Date
{
    @XmlElement(name = "month")
    @JacksonXmlProperty(localName = "month")
    private String month;

    @XmlElement(name = "year")
    @JacksonXmlProperty(localName = "year")
    private String year;

    @XmlElement(name = "day")
    @JacksonXmlProperty(localName = "day")
    private String day;

    @XmlAttribute(name = "id")
    @JacksonXmlProperty(localName = "id", isAttribute = true)
    private Integer id;

    @XmlElement(name = "role")
    @JacksonXmlProperty(localName = "role")
    private Role role;

    @Override
    public String toString() {
        return "Date{" +
                "month='" + month + '\'' +
                ", year='" + year + '\'' +
                ", day='" + day + '\'' +
                ", id=" + id +
                ", role=" + role +
                '}';
    }
}