package com.example.route.dto;

import lombok.Data;

@Data
public class RouteRequest {
    private String city;
    private Integer days;
    private String style;
    private String[] attractions;
    private String[] hotels;
}
