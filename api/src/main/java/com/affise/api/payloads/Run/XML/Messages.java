package com.affise.api.payloads.Run.XML;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

import javax.xml.bind.annotation.*;
import java.util.List;

@Slf4j
@Getter
@Setter
@Accessors(fluent = true)
@RequiredArgsConstructor
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "messages")
@JacksonXmlRootElement(localName = "messages")
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class Messages {

    @XmlElement(name = "note")
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "note")
    private List<Note> note;

    @XmlElement(name = "type")
    @JacksonXmlProperty(localName = "type")
    private String type;

    @XmlElement(name = "status")
    @JacksonXmlProperty(localName = "status")
    private Boolean status;

    @XmlElement(name = "role")
    @JacksonXmlProperty(localName = "role")
    private Role role;

    @XmlElement(name = "text")
    @JacksonXmlText
    @JsonIgnore
    private String text;

    @XmlElement(name = "categories")
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "categories")
    private List<String> categories;


    @Override
    public String toString() {
        return "Messages{" +
                "note=" + note +
                ", type='" + type + '\'' +
                ", status=" + status +
                ", role=" + role +
                ", text='" + text + '\'' +
                ", categories=" + categories +
                '}';
    }
}
