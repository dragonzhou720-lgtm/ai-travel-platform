package com.example.attraction.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class AttractionDTO {

    private String name;

    private String city;

    private String address;

    private BigDecimal ticketPrice;

    private BigDecimal rating;

    private String description;

    private String openTime;

    private String tags;

    private String coverImage;
}