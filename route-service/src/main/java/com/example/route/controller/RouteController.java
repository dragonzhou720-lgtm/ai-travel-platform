package com.example.route.controller;

import com.example.route.dto.RouteRequest;
import com.example.route.dto.RouteResponse;
import com.example.route.service.RouteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/route")
public class RouteController {

    private final RouteService routeService;

    public RouteController(RouteService routeService) {
        this.routeService = routeService;
    }

    @PostMapping("/generate")
    public ResponseEntity<RouteResponse> generateRoute(@RequestBody RouteRequest request) {
        RouteResponse route = routeService.generateRoute(request);
        return ResponseEntity.ok(route);
    }

    @PostMapping("/sentinel-test")
    public ResponseEntity<RouteResponse> sentinelTest(@RequestBody RouteRequest request) {
        RouteResponse route = routeService.generateRoute(request);
        return ResponseEntity.ok(route);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RouteResponse> getRoute(@PathVariable Long id) {
        RouteResponse route = routeService.getRouteById(id);
        if (route == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(route);
    }

    @GetMapping
    public ResponseEntity<List<RouteResponse>> getAllRoutes() {
        return ResponseEntity.ok(routeService.getAllRoutes());
    }

    @GetMapping("/hot")
    public ResponseEntity<List<Map<String, Object>>> getHotRoutes(
            @RequestParam(defaultValue = "10") int limit) {
        return ResponseEntity.ok(routeService.getHotRoutes(limit));
    }

    @GetMapping("/hot/count")
    public ResponseEntity<Map<String, Object>> getHotRouteCount(
            @RequestParam String city,
            @RequestParam Integer days,
            @RequestParam String style) {
        Integer count = routeService.getHotRouteCount(city, days, style);
        return ResponseEntity.ok(Map.of("city", city, "days", days, "style", style, "count", count));
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> health() {
        return ResponseEntity.ok(Map.of("status", "UP", "service", "route-service"));
    }
}
