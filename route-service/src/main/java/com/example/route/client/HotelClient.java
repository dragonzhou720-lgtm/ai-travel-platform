package com.example.route.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@FeignClient(name = "hotel-service", url = "http://localhost:8085", fallback = HotelClientFallback.class)
public interface HotelClient {

    @GetMapping("/api/hotel/search")
    List<Map<String, Object>> searchHotels(@RequestParam String city, @RequestParam(required = false) String keyword);

    @GetMapping("/api/hotel/detail")
    Map<String, Object> getHotelDetail(@RequestParam Long id);
}
