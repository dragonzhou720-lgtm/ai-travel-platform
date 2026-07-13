package com.example.route.client;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class AiClientFallback implements AiClient {

    @Override
    public Map<String, Object> generateRoute(Map<String, Object> request) {
        Map<String, Object> result = new HashMap<>();
        result.put("success", false);
        result.put("message", "AI service unavailable, using fallback route generation");
        result.put("route", generateFallbackRoute(request));
        return result;
    }

    private Map<String, Object> generateFallbackRoute(Map<String, Object> request) {
        Map<String, Object> route = new HashMap<>();
        route.put("city", request.getOrDefault("city", "unknown"));
        route.put("days", request.getOrDefault("days", 3));
        route.put("style", request.getOrDefault("style", "relax"));
        return route;
    }
}
