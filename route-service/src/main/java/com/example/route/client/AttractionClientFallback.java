package com.example.route.client;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class AttractionClientFallback implements AttractionClient {

    @Override
    public List<Map<String, Object>> searchAttractions(String city, String keyword) {
        return new ArrayList<>();
    }

    @Override
    public Map<String, Object> getAttractionDetail(Long id) {
        Map<String, Object> result = new HashMap<>();
        result.put("id", id);
        result.put("name", "Attraction service unavailable");
        return result;
    }
}
