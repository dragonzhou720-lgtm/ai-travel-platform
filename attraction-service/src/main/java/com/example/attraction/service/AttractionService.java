package com.example.attraction.service;

import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class AttractionService {

    private final Map<Long, Map<String, Object>> attractionStore = new ConcurrentHashMap<>();

    public AttractionService() {
        initMockData();
    }

    private void initMockData() {
        addAttraction(1L, "Beijing", "Great Wall", "historic", 4.9, "The Great Wall of China");
        addAttraction(2L, "Beijing", "Forbidden City", "historic", 4.8, "Imperial palace complex");
        addAttraction(3L, "Beijing", "Summer Palace", "nature", 4.7, "Royal garden");
        addAttraction(4L, "Shanghai", "Bund", "scenic", 4.8, "Famous waterfront");
        addAttraction(5L, "Shanghai", "Yu Garden", "historic", 4.6, "Traditional Chinese garden");
        addAttraction(6L, "Hangzhou", "West Lake", "nature", 4.9, "Beautiful lake scenery");
        addAttraction(7L, "Hangzhou", "Lingyin Temple", "cultural", 4.7, "Ancient Buddhist temple");
        addAttraction(8L, "Chengdu", "Dujiangyan", "historic", 4.8, "Ancient irrigation system");
        addAttraction(9L, "Chengdu", "Jinli", "cultural", 4.5, "Ancient street");
        addAttraction(10L, "Xi'an", "Terracotta Army", "historic", 4.9, "World famous museum");
    }

    private void addAttraction(Long id, String city, String name, String type, double rating, String description) {
        Map<String, Object> attraction = new HashMap<>();
        attraction.put("id", id);
        attraction.put("city", city);
        attraction.put("name", name);
        attraction.put("type", type);
        attraction.put("rating", rating);
        attraction.put("description", description);
        attractionStore.put(id, attraction);
    }

    public List<Map<String, Object>> searchAttractions(String city, String keyword) {
        return attractionStore.values().stream()
                .filter(a -> city.equalsIgnoreCase((String) a.get("city")))
                .filter(a -> keyword == null || keyword.isEmpty() ||
                        ((String) a.get("name")).toLowerCase().contains(keyword.toLowerCase()) ||
                        ((String) a.get("description")).toLowerCase().contains(keyword.toLowerCase()))
                .toList();
    }

    public Map<String, Object> getAttractionDetail(Long id) {
        return attractionStore.get(id);
    }
}
