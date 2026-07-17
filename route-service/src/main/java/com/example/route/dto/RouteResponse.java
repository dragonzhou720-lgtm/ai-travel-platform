package com.example.route.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RouteResponse {
    private Long id;
    private String name;
    private String city;
    private String destination;
    private Integer days;
    private BigDecimal budget;
    private String style;
    private String preference;
    private List<Map<String, Object>> itinerary;
    private List<Map<String, Object>> routeDays;
    private String aiSummary;
    private List<Map<String, Object>> attractions;
    private List<Map<String, Object>> hotels;
    private Long createdAt;
}
