package com.example.route.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@FeignClient(name = "attraction-service", url = "http://localhost:8084", fallback = AttractionClientFallback.class)
public interface AttractionClient {

    @GetMapping("/api/attraction/search")
    List<Map<String, Object>> searchAttractions(@RequestParam String city, @RequestParam(required = false) String keyword);

    @GetMapping("/api/attraction/detail")
    Map<String, Object> getAttractionDetail(@RequestParam Long id);
}
