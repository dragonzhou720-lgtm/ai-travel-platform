package com.example.hotel.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.hotel.dto.HotelDTO;
import com.example.hotel.dto.HotelQueryDTO;
import com.example.hotel.entity.Hotel;
import com.example.hotel.service.HotelService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/hotels")
public class HotelController {

    private final HotelService hotelService;

    public HotelController(HotelService hotelService) {
        this.hotelService = hotelService;
    }

    @PostMapping
    public ResponseEntity<Hotel> create(@RequestBody HotelDTO dto) {
        Hotel hotel = hotelService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(hotel);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Hotel> update(@PathVariable Long id, @RequestBody HotelDTO dto) {
        Hotel hotel = hotelService.update(id, dto);
        if (hotel == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(hotel);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        hotelService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Hotel> getById(@PathVariable Long id) {
        Hotel hotel = hotelService.getById(id);
        if (hotel == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(hotel);
    }

    @GetMapping("/hot")
    public ResponseEntity<List<Hotel>> getHotHotels(@RequestParam(defaultValue = "10") int limit) {
        List<Hotel> hotels = hotelService.getHotHotels(limit);
        return ResponseEntity.ok(hotels);
    }

    @GetMapping("/cities")
    public ResponseEntity<List<String>> getAllCities() {
        List<String> cities = hotelService.getAllCities();
        return ResponseEntity.ok(cities);
    }

    @GetMapping("/recommend")
    public ResponseEntity<List<Hotel>> recommend(
            @RequestParam(required = false) String city,
            @RequestParam(defaultValue = "5") int limit) {
        List<Hotel> hotels = hotelService.recommend(city, limit);
        return ResponseEntity.ok(hotels);
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

        HotelQueryDTO queryDTO = new HotelQueryDTO();
        queryDTO.setCity(city);
        if (minPrice != null) {
            queryDTO.setMinPrice(BigDecimal.valueOf(minPrice));
        }
        if (maxPrice != null) {
            queryDTO.setMaxPrice(BigDecimal.valueOf(maxPrice));
        }
        queryDTO.setSortBy(sortBy);
        queryDTO.setSortOrder(sortOrder);

        Page<Hotel> page = hotelService.query(queryDTO, pageNum, pageSize);

        Map<String, Object> result = new HashMap<>();
        result.put("data", page.getRecords());
        result.put("total", page.getTotal());
        result.put("page", pageNum);
        result.put("size", pageSize);

        return ResponseEntity.ok(result);
    }
}