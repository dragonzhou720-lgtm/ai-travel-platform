package com.example.ai.controller;

import com.example.ai.service.AiService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/ai")
public class AiController {

    private final AiService aiService;

    public AiController(AiService aiService) {
        this.aiService = aiService;
    }

    @PostMapping("/generate-route")
    public ResponseEntity<Map<String, Object>> generateRoute(@RequestBody Map<String, Object> request) {
        return ResponseEntity.ok(aiService.generateRoute(request));
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> health() {
        return ResponseEntity.ok(Map.of("status", "UP", "service", "ai-service"));
    }
}
