package com.example.route.service;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.example.route.client.AiClient;
import com.example.route.client.AttractionClient;
import com.example.route.client.HotelClient;
import com.example.route.config.SentinelConfig;
import com.example.route.dto.RouteRequest;
import com.example.route.dto.RouteResponse;
import com.example.route.rabbitmq.RouteMessageProducer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
@Service
public class RouteService {

    private final AttractionClient attractionClient;
    private final HotelClient hotelClient;
    private final AiClient aiClient;
    private final RouteMessageProducer routeMessageProducer;

    private final Map<Long, RouteResponse> routeStore = new ConcurrentHashMap<>();
    private final Map<String, Integer> hotRouteCounters = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    public RouteService(AttractionClient attractionClient, HotelClient hotelClient, AiClient aiClient,
                        RouteMessageProducer routeMessageProducer) {
        this.attractionClient = attractionClient;
        this.hotelClient = hotelClient;
        this.aiClient = aiClient;
        this.routeMessageProducer = routeMessageProducer;
    }

    public RouteResponse generateRoute(RouteRequest request) {
        Entry entry = null;
        try {
            entry = SphU.entry(SentinelConfig.ROUTE_GENERATE_RESOURCE);
        } catch (BlockException e) {
            log.warn("Route generation blocked by Sentinel");
            return generateFallbackRoute(request);
        }

        try {
            String city = request.getCity();
            Integer days = request.getDays() != null ? request.getDays() : 3;
            String style = request.getStyle() != null ? request.getStyle() : "relax";

            Map<String, Object> aiRequest = new HashMap<>();
            aiRequest.put("city", city);
            aiRequest.put("days", days);
            aiRequest.put("style", style);

            Map<String, Object> aiResponse = null;
            try {
                Entry aiEntry = SphU.entry(SentinelConfig.AI_SERVICE_RESOURCE);
                try {
                    aiResponse = aiClient.generateRoute(aiRequest);
                } finally {
                    aiEntry.exit();
                }
            } catch (BlockException e) {
                log.warn("AI service blocked by Sentinel, using fallback");
            }

            List<Map<String, Object>> attractions = null;
            try {
                Entry attrEntry = SphU.entry(SentinelConfig.ATTRACTION_SERVICE_RESOURCE);
                try {
                    attractions = attractionClient.searchAttractions(city, null);
                } finally {
                    attrEntry.exit();
                }
            } catch (BlockException e) {
                log.warn("Attraction service blocked by Sentinel, using fallback");
                attractions = generateFallbackAttractions(city);
            }

            List<Map<String, Object>> hotels = null;
            try {
                Entry hotelEntry = SphU.entry(SentinelConfig.HOTEL_SERVICE_RESOURCE);
                try {
                    hotels = hotelClient.searchHotels(city, null);
                } finally {
                    hotelEntry.exit();
                }
            } catch (BlockException e) {
                log.warn("Hotel service blocked by Sentinel, using fallback");
                hotels = generateFallbackHotels(city);
            }

            List<Map<String, Object>> itinerary = generateItinerary(days, attractions != null ? attractions : new ArrayList<>(), 
                    hotels != null ? hotels : new ArrayList<>(), style);

            RouteResponse route = RouteResponse.builder()
                    .id(idGenerator.incrementAndGet())
                    .city(city)
                    .days(days)
                    .style(style)
                    .itinerary(itinerary)
                    .attractions(attractions != null && attractions.size() > 5 ? attractions.subList(0, 5) : attractions)
                    .hotels(hotels != null && hotels.size() > 3 ? hotels.subList(0, 3) : hotels)
                    .createdAt(System.currentTimeMillis())
                    .build();

            routeStore.put(route.getId(), route);

            String key = city + ":" + days + ":" + style;
            Integer count = hotRouteCounters.merge(key, 1, Integer::sum);

            routeMessageProducer.sendRouteGenerated(route);
            routeMessageProducer.sendHotRouteStats(city, days, style, count);
            routeMessageProducer.sendHistorySave(route, "default-user");

            return route;
        } finally {
            if (entry != null) {
                entry.exit();
            }
        }
    }

    private RouteResponse generateFallbackRoute(RouteRequest request) {
        String city = request.getCity();
        Integer days = request.getDays() != null ? request.getDays() : 3;
        String style = request.getStyle() != null ? request.getStyle() : "relax";

        List<Map<String, Object>> itinerary = new ArrayList<>();
        for (int day = 1; day <= days; day++) {
            Map<String, Object> dayPlan = new HashMap<>();
            dayPlan.put("day", day);
            dayPlan.put("morning", List.of("限流保护: 请稍后重试"));
            dayPlan.put("afternoon", List.of("限流保护: 请稍后重试"));
            dayPlan.put("evening", List.of("限流保护: 请稍后重试"));
            itinerary.add(dayPlan);
        }

        return RouteResponse.builder()
                .id(idGenerator.incrementAndGet())
                .city(city)
                .days(days)
                .style(style)
                .itinerary(itinerary)
                .attractions(new ArrayList<>())
                .hotels(new ArrayList<>())
                .createdAt(System.currentTimeMillis())
                .build();
    }

    private List<Map<String, Object>> generateFallbackAttractions(String city) {
        List<Map<String, Object>> attractions = new ArrayList<>();
        Map<String, Object> attr = new HashMap<>();
        attr.put("id", 999);
        attr.put("name", city + "热门景点");
        attr.put("city", city);
        attr.put("rating", 4.5);
        attr.put("description", "服务限流中，展示推荐景点");
        attractions.add(attr);
        return attractions;
    }

    private List<Map<String, Object>> generateFallbackHotels(String city) {
        List<Map<String, Object>> hotels = new ArrayList<>();
        Map<String, Object> hotel = new HashMap<>();
        hotel.put("id", 999);
        hotel.put("name", city + "推荐酒店");
        hotel.put("city", city);
        hotel.put("star", "4-star");
        hotel.put("rating", 4.5);
        hotel.put("description", "服务限流中，展示推荐酒店");
        hotels.add(hotel);
        return hotels;
    }

    private List<Map<String, Object>> generateItinerary(Integer days, List<Map<String, Object>> attractions,
                                                         List<Map<String, Object>> hotels, String style) {
        List<Map<String, Object>> itinerary = new ArrayList<>();
        Random random = new Random();

        for (int day = 1; day <= days; day++) {
            Map<String, Object> dayPlan = new HashMap<>();
            dayPlan.put("day", day);

            List<Map<String, Object>> morning = new ArrayList<>();
            List<Map<String, Object>> afternoon = new ArrayList<>();
            List<Map<String, Object>> evening = new ArrayList<>();

            if (!attractions.isEmpty()) {
                Map<String, Object> morningAttraction = attractions.get(random.nextInt(attractions.size()));
                morning.add(morningAttraction);

                Map<String, Object> afternoonAttraction = attractions.get(random.nextInt(attractions.size()));
                afternoon.add(afternoonAttraction);
            }

            if (!hotels.isEmpty()) {
                Map<String, Object> eveningPlan = new HashMap<>();
                eveningPlan.put("type", "hotel");
                eveningPlan.put("name", hotels.get(random.nextInt(hotels.size())).get("name"));
                evening.add(eveningPlan);
            }

            dayPlan.put("morning", morning);
            dayPlan.put("afternoon", afternoon);
            dayPlan.put("evening", evening);
            itinerary.add(dayPlan);
        }
        return itinerary;
    }

    public RouteResponse getRouteById(Long id) {
        return routeStore.get(id);
    }

    public List<RouteResponse> getAllRoutes() {
        return new ArrayList<>(routeStore.values());
    }

    public List<Map<String, Object>> getHotRoutes(int limit) {
        return hotRouteCounters.entrySet().stream()
                .sorted((a, b) -> b.getValue().compareTo(a.getValue()))
                .limit(limit)
                .map(entry -> {
                    Map<String, Object> result = new HashMap<>();
                    String[] parts = entry.getKey().split(":");
                    result.put("city", parts[0]);
                    result.put("days", Integer.parseInt(parts[1]));
                    result.put("style", parts[2]);
                    result.put("count", entry.getValue());
                    return result;
                })
                .toList();
    }

    public Integer getHotRouteCount(String city, Integer days, String style) {
        String key = city + ":" + days + ":" + style;
        return hotRouteCounters.getOrDefault(key, 0);
    }
}
