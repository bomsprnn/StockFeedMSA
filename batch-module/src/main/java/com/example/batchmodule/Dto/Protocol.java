package com.example.batchmodule.Dto;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@JacksonXmlRootElement(localName = "protocol")
public class Protocol {
    @JacksonXmlProperty(localName = "chartdata")
    private Chartdata chartdata;

}