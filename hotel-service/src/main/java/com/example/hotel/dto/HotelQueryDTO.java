package com.example.hotel.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class HotelQueryDTO {

    private String city;

    private BigDecimal minPrice;

    private BigDecimal maxPrice;

    private String sortBy;

    private String sortOrder = "desc";
}