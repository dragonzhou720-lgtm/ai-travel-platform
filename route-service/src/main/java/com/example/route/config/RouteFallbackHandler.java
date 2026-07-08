package com.example.route.config;

import com.example.route.dto.RouteCreateRequest;
import com.example.route.dto.RouteResponse;
import org.springframework.stereotype.Component;

@Component
public class RouteFallbackHandler {

    public RouteResponse fallback(RouteCreateRequest request, Throwable throwable) {
        RouteResponse response = new RouteResponse();
        response.setRouteId(-1L);
        response.setName(request.getName());
        response.setDestination(request.getDestination());
        response.setDays(request.getDays());
        response.setBudget(request.getBudget());
        response.setPreference(request.getPreference());
        response.setAiSummary("系统当前繁忙，已返回预设路线方案。");
        response.setItinerary(request.getItinerary());
        return response;
    }
}
