package com.example.ai.service;

import com.example.ai.config.DeepSeekProperties;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AiService {

    private final DeepSeekProperties deepSeekProperties;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Autowired
    public AiService(DeepSeekProperties deepSeekProperties, RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.deepSeekProperties = deepSeekProperties;
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
        initFromEnv();
    }

    private void initFromEnv() {
        try {
            Dotenv dotenv = Dotenv.load();
            if (deepSeekProperties.getApiKey() == null || deepSeekProperties.getApiKey().isBlank()) {
                deepSeekProperties.setApiKey(dotenv.get("DEEPSEEK_API_KEY"));
            }
            if (deepSeekProperties.getBaseUrl() == null || deepSeekProperties.getBaseUrl().isBlank()) {
                deepSeekProperties.setBaseUrl(dotenv.get("DEEPSEEK_BASE_URL", "https://api.deepseek.com/v1"));
            }
            if (deepSeekProperties.getModel() == null || deepSeekProperties.getModel().isBlank()) {
                deepSeekProperties.setModel(dotenv.get("DEEPSEEK_MODEL", "deepseek-chat"));
            }
        } catch (Exception e) {
            // ignore - use defaults
        }
    }

    public Map<String, Object> generateRoute(Map<String, Object> request) {
        String city = (String) request.get("city");
        Integer days = (Integer) request.get("days");
        String style = (String) request.get("style");
        String budget = (String) request.get("budget");

        Map<String, Object> response = new HashMap<>();
        
        if (deepSeekProperties.getApiKey() == null || deepSeekProperties.getApiKey().isBlank()) {
            return generateMockRoute(city, days, style);
        }

        try {
            String aiResponse = callDeepSeekApi(city, days, style, budget);
            response = parseAiResponse(aiResponse, city, days, style);
        } catch (Exception e) {
            response = generateMockRoute(city, days, style);
            response.put("fallback", true);
            response.put("error", e.getMessage());
        }

        return response;
    }

    private String callDeepSeekApi(String city, Integer days, String style, String budget) {
        String url = deepSeekProperties.getBaseUrl() + "/chat/completions";
        
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", deepSeekProperties.getModel());
        
        List<Map<String, Object>> messages = new ArrayList<>();
        Map<String, Object> systemMessage = new HashMap<>();
        systemMessage.put("role", "system");
        systemMessage.put("content", "你是一个专业的旅游规划师，请根据用户的需求生成详细的旅游路线。");
        messages.add(systemMessage);
        
        Map<String, Object> userMessage = new HashMap<>();
        StringBuilder prompt = new StringBuilder();
        prompt.append("请为我规划一个").append(city).append("的").append(days).append("日游路线。");
        prompt.append("旅游风格：").append(style).append("。");
        if (budget != null && !budget.isBlank()) {
            prompt.append("预算：").append(budget).append("。");
        }
        prompt.append("请返回JSON格式的行程安排，包含每天的上午、下午、晚上的活动建议。");
        userMessage.put("role", "user");
        userMessage.put("content", prompt.toString());
        messages.add(userMessage);
        
        requestBody.put("messages", messages);
        requestBody.put("temperature", 0.7);
        requestBody.put("max_tokens", 2000);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + deepSeekProperties.getApiKey());

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<String> result = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
        
        return result.getBody();
    }

    private Map<String, Object> parseAiResponse(String aiResponse, String city, Integer days, String style) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Route generated successfully");
        
        try {
            JsonNode root = objectMapper.readTree(aiResponse);
            JsonNode choices = root.get("choices");
            if (choices != null && choices.isArray() && choices.size() > 0) {
                String content = choices.get(0).get("message").get("content").asText();
                response.put("ai_response", content);
                
                try {
                    JsonNode routeJson = objectMapper.readTree(content);
                    response.put("route", routeJson);
                } catch (Exception e) {
                    Map<String, Object> route = new HashMap<>();
                    route.put("city", city);
                    route.put("days", days);
                    route.put("style", style);
                    route.put("ai_suggestion", content);
                    response.put("route", route);
                }
            }
        } catch (Exception e) {
            response.put("ai_response", aiResponse);
            Map<String, Object> route = new HashMap<>();
            route.put("city", city);
            route.put("days", days);
            route.put("style", style);
            response.put("route", route);
        }
        
        return response;
    }

    private Map<String, Object> generateMockRoute(String city, Integer days, String style) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Route generated successfully (mock)");
        response.put("fallback", true);

        Map<String, Object> route = new HashMap<>();
        route.put("city", city);
        route.put("days", days);
        route.put("style", style);
        route.put("optimization", "Based on " + style + " travel style");
        route.put("ai_suggestion", getAiSuggestion(city, style));

        List<Map<String, Object>> itinerary = new ArrayList<>();
        for (int day = 1; day <= days; day++) {
            Map<String, Object> dayPlan = new HashMap<>();
            dayPlan.put("day", day);
            dayPlan.put("morning", getMorningActivity(city, style, day));
            dayPlan.put("afternoon", getAfternoonActivity(city, style, day));
            dayPlan.put("evening", getEveningActivity(city, style, day));
            itinerary.add(dayPlan);
        }
        route.put("itinerary", itinerary);

        response.put("route", route);
        return response;
    }

    private List<String> getMorningActivity(String city, String style, int day) {
        List<String> activities = new ArrayList<>();
        switch (style.toLowerCase()) {
            case "adventure" -> activities.add("Day " + day + ": Morning hiking in " + city);
            case "relax" -> activities.add("Day " + day + ": Morning yoga at hotel");
            case "culture" -> activities.add("Day " + day + ": Visit " + city + " museum");
            case "shopping" -> activities.add("Day " + day + ": Morning shopping tour");
            default -> activities.add("Day " + day + ": Morning sightseeing");
        }
        return activities;
    }

    private List<String> getAfternoonActivity(String city, String style, int day) {
        List<String> activities = new ArrayList<>();
        switch (style.toLowerCase()) {
            case "adventure" -> activities.add("Day " + day + ": Afternoon biking tour");
            case "relax" -> activities.add("Day " + day + ": Afternoon spa treatment");
            case "culture" -> activities.add("Day " + day + ": Explore local cuisine");
            case "shopping" -> activities.add("Day " + day + ": Afternoon market visit");
            default -> activities.add("Day " + day + ": Afternoon exploration");
        }
        return activities;
    }

    private List<String> getEveningActivity(String city, String style, int day) {
        List<String> activities = new ArrayList<>();
        switch (style.toLowerCase()) {
            case "adventure" -> activities.add("Day " + day + ": Evening campfire");
            case "relax" -> activities.add("Day " + day + ": Evening sunset viewing");
            case "culture" -> activities.add("Day " + day + ": Evening traditional show");
            case "shopping" -> activities.add("Day " + day + ": Evening dinner and shopping");
            default -> activities.add("Day " + day + ": Evening dinner");
        }
        return activities;
    }

    private String getAiSuggestion(String city, String style) {
        return switch (style.toLowerCase()) {
            case "adventure" -> "Recommended adventure activities in " + city + ": hiking, biking, and outdoor exploration";
            case "relax" -> "Recommended relaxation spots in " + city + ": spas, parks, and scenic views";
            case "culture" -> "Recommended cultural experiences in " + city + ": museums, historic sites, and local cuisine";
            case "shopping" -> "Recommended shopping areas in " + city + ": malls, markets, and specialty shops";
            default -> "Recommended attractions in " + city + " based on your preferences";
        };
    }
}