package com.example.hotel.service;

import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class HotelService {

    private final Map<Long, Map<String, Object>> hotelStore = new ConcurrentHashMap<>();

    public HotelService() {
        initMockData();
    }

    private void initMockData() {
        addHotel(1L, "Beijing", "Beijing Hotel", "5-star", 4.8, "Luxury hotel near Tiananmen");
        addHotel(2L, "Beijing", "Shangri-La", "5-star", 4.7, "International luxury brand");
        addHotel(3L, "Beijing", "Holiday Inn", "4-star", 4.5, "Comfortable business hotel");
        addHotel(4L, "Shanghai", "Peace Hotel", "5-star", 4.9, "Historic landmark");
        addHotel(5L, "Shanghai", "The Peninsula", "5-star", 4.8, "Luxury hotel");
        addHotel(6L, "Shanghai", "Hilton", "4-star", 4.6, "International brand");
        addHotel(7L, "Hangzhou", "West Lake State Guest House", "5-star", 4.9, "Scenic hotel");
        addHotel(8L, "Hangzhou", "Four Seasons", "5-star", 4.8, "Luxury resort");
        addHotel(9L, "Chengdu", "Ritz Carlton", "5-star", 4.7, "Luxury hotel");
        addHotel(10L, "Chengdu", "InterContinental", "5-star", 4.6, "International brand");
    }

    private void addHotel(Long id, String city, String name, String star, double rating, String description) {
        Map<String, Object> hotel = new HashMap<>();
        hotel.put("id", id);
        hotel.put("city", city);
        hotel.put("name", name);
        hotel.put("star", star);
        hotel.put("rating", rating);
        hotel.put("description", description);
        hotelStore.put(id, hotel);
    }

    public List<Map<String, Object>> searchHotels(String city, String keyword) {
        return hotelStore.values().stream()
                .filter(h -> city.equalsIgnoreCase((String) h.get("city")))
                .filter(h -> keyword == null || keyword.isEmpty() ||
                        ((String) h.get("name")).toLowerCase().contains(keyword.toLowerCase()) ||
                        ((String) h.get("description")).toLowerCase().contains(keyword.toLowerCase()))
                .toList();
    }

    public Map<String, Object> getHotelDetail(Long id) {
        return hotelStore.get(id);
    }
}
