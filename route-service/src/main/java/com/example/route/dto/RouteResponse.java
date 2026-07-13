package com.example.route.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RouteResponse {
    private Long id;
    private String city;
    private Integer days;
    private String style;
    private List<Map<String, Object>> itinerary;
    private List<Map<String, Object>> attractions;
    private List<Map<String, Object>> hotels;
    private Long createdAt;
}
