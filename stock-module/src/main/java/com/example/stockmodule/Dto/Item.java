package com.example.stockmodule.Dto;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Item {
    @JacksonXmlProperty(localName = "data")
    private String data;
}
