package com.example.route.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@FeignClient(name = "ai-service", url = "http://localhost:8082", fallback = AiClientFallback.class)
public interface AiClient {

    @PostMapping("/api/ai/generate-route")
    Map<String, Object> generateRoute(@RequestBody Map<String, Object> request);
}
