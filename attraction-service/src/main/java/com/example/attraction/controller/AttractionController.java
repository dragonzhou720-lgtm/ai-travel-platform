package com.example.attraction.controller;

import com.example.attraction.service.AttractionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/attraction")
public class AttractionController {

    private final AttractionService attractionService;

    public AttractionController(AttractionService attractionService) {
        this.attractionService = attractionService;
    }

    @GetMapping("/search")
    public ResponseEntity<List<Map<String, Object>>> search(
            @RequestParam String city,
            @RequestParam(required = false) String keyword) {
        return ResponseEntity.ok(attractionService.searchAttractions(city, keyword));
    }

    @GetMapping("/detail")
    public ResponseEntity<Map<String, Object>> detail(@RequestParam Long id) {
        Map<String, Object> attraction = attractionService.getAttractionDetail(id);
        if (attraction == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(attraction);
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> health() {
        return ResponseEntity.ok(Map.of("status", "UP", "service", "attraction-service"));
    }

    @GetMapping("/all")
    public ResponseEntity<List<Map<String, Object>>> getAll() {
        return ResponseEntity.ok(attractionService.getAllAttractions());
    }
}
