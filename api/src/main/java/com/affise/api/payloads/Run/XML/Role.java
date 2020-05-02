package com.affise.api.payloads.Run.XML;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlValue;

@Slf4j
@Getter
@Setter
@Accessors(fluent = true)
@RequiredArgsConstructor
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class Role {

    @XmlAttribute(name = "section")
    @JacksonXmlProperty(localName = "section", isAttribute = true)
    private String section;

    @XmlValue
    @JacksonXmlText
    private String value;

    @Override
    public String toString() {
        return "Role{" +
                "section='" + section + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
