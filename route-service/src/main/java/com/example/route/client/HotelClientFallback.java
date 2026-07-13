package com.example.route.client;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class HotelClientFallback implements HotelClient {

    @Override
    public List<Map<String, Object>> searchHotels(String city, String keyword) {
        return new ArrayList<>();
    }

    @Override
    public Map<String, Object> getHotelDetail(Long id) {
        Map<String, Object> result = new HashMap<>();
        result.put("id", id);
        result.put("name", "Hotel service unavailable");
        return result;
    }
}
