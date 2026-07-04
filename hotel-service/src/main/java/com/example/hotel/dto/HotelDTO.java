package com.example.hotel.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class HotelDTO {

    private String name;

    private String city;

    private BigDecimal price;

    private BigDecimal rating;

    private String address;

    private String imageUrl;
}