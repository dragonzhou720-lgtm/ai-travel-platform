package com.example.hotel.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class HotelDTO {

    private String name;

    private String city;

    private String address;

    private BigDecimal pricePerNight;

    private BigDecimal rating;

    private Integer starLevel;

    private String description;

    private String tags;

    private String coverImage;
}