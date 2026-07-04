package com.example.attraction.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class AttractionDTO {

    private String name;

    private String city;

    private BigDecimal price;

    private BigDecimal rating;

    private String description;

    private String imageUrl;
}