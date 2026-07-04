package com.example.attraction.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.attraction.dto.AttractionDTO;
import com.example.attraction.dto.AttractionQueryDTO;
import com.example.attraction.entity.Attraction;
import com.example.attraction.service.AttractionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/attractions")
public class AttractionController {

    private final AttractionService attractionService;

    public AttractionController(AttractionService attractionService) {
        this.attractionService = attractionService;
    }

    @PostMapping
    public ResponseEntity<Attraction> create(@RequestBody AttractionDTO dto) {
        Attraction attraction = attractionService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(attraction);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Attraction> update(@PathVariable Long id, @RequestBody AttractionDTO dto) {
        Attraction attraction = attractionService.update(id, dto);
        if (attraction == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(attraction);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        attractionService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Attraction> getById(@PathVariable Long id) {
        Attraction attraction = attractionService.getById(id);
        if (attraction == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(attraction);
    }

    @GetMapping("/hot")
    public ResponseEntity<List<Attraction>> getHotAttractions(@RequestParam(defaultValue = "10") int limit) {
        List<Attraction> attractions = attractionService.getHotAttractions(limit);
        return ResponseEntity.ok(attractions);
    }

    @GetMapping("/cities")
    public ResponseEntity<List<String>> getAllCities() {
        List<String> cities = attractionService.getAllCities();
        return ResponseEntity.ok(cities);
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> query(
            @RequestParam(required = false) String city,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false, defaultValue = "desc") String sortOrder,
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize) {

        AttractionQueryDTO queryDTO = new AttractionQueryDTO();
        queryDTO.setCity(city);
        if (minPrice != null) {
            queryDTO.setMinPrice(BigDecimal.valueOf(minPrice));
        }
        if (maxPrice != null) {
            queryDTO.setMaxPrice(BigDecimal.valueOf(maxPrice));
        }
        queryDTO.setSortBy(sortBy);
        queryDTO.setSortOrder(sortOrder);

        Page<Attraction> page = attractionService.query(queryDTO, pageNum, pageSize);

        Map<String, Object> result = new HashMap<>();
        result.put("data", page.getRecords());
        result.put("total", page.getTotal());
        result.put("page", pageNum);
        result.put("size", pageSize);

        return ResponseEntity.ok(result);
    }
}