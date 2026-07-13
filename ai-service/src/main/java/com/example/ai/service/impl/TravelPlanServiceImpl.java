package com.example.ai.service.impl;

import com.example.ai.config.AiTravelPlanFallback;
import com.example.ai.config.DeepSeekProperties;
import com.example.ai.dto.TravelPlanRequest;
import com.example.ai.dto.TravelPlanResponse;
import com.example.ai.service.TravelPlanService;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class TravelPlanServiceImpl implements TravelPlanService {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    private final AiTravelPlanFallback fallback;
    private final DeepSeekProperties deepSeekProperties;
    private final RestTemplate restTemplate;

    public TravelPlanServiceImpl(AiTravelPlanFallback fallback, DeepSeekProperties deepSeekProperties, RestTemplate restTemplate) {
        this.fallback = fallback;
        this.deepSeekProperties = deepSeekProperties;
        this.restTemplate = restTemplate;
    }

    @Override
    public TravelPlanResponse generatePlan(TravelPlanRequest request) {
        try {
            if (!hasDeepSeekConfig()) {
                return buildLocalPlan(request);
            }
            return callDeepSeek(request);
        } catch (Exception ex) {
            return fallback.fallback(request);
        }
    }

    private boolean hasDeepSeekConfig() {
        return deepSeekProperties.getApiKey() != null && !deepSeekProperties.getApiKey().isBlank()
                && deepSeekProperties.getBaseUrl() != null && !deepSeekProperties.getBaseUrl().isBlank()
                && deepSeekProperties.getModel() != null && !deepSeekProperties.getModel().isBlank();
    }

    private TravelPlanResponse callDeepSeek(TravelPlanRequest request) {
        String prompt = buildPrompt(request);

        Map<String, Object> body = Map.of(
                "model", deepSeekProperties.getModel(),
                "messages", List.of(
                        Map.of("role", "system", "content", "You are a helpful travel planner. Return only valid JSON without markdown fences."),
                        Map.of("role", "user", "content", prompt)
                ),
                "temperature", 0.7,
                "stream", false
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(deepSeekProperties.getApiKey());

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);
        Map<?, ?> response = restTemplate.postForObject(deepSeekProperties.getBaseUrl() + "/chat/completions", entity, Map.class);

        String content = extractContent(response);
        return parseDeepSeekResponse(request, content);
    }

    private String buildPrompt(TravelPlanRequest request) {
        return "请根据以下旅游需求生成一份严格 JSON 格式的旅行方案，禁止输出 Markdown、代码块、注释或多余文字。\n"
                + "JSON 必须符合如下结构：\n"
                + "{\n"
                + "  \"summary\": \"...\",\n"
                + "  \"recommendations\": [\"...\", \"...\"],\n"
                + "  \"itinerary\": [\n"
                + "    {\"day\": 1, \"title\": \"...\", \"detail\": \"...\"}\n"
                + "  ]\n"
                + "}\n"
                + "请根据以下信息生成：目的地=" + request.getDestination()
                + "，天数=" + request.getDays()
                + "，预算=" + request.getBudget()
                + "，偏好=" + request.getPreference() + "。";
    }

    @SuppressWarnings("unchecked")
    private String extractContent(Map<?, ?> response) {
        if (response == null) {
            return null;
        }
        Object choicesObj = response.get("choices");
        if (!(choicesObj instanceof List<?> choices) || choices.isEmpty()) {
            return null;
        }
        Object first = choices.get(0);
        if (!(first instanceof Map<?, ?> choice)) {
            return null;
        }
        Object messageObj = choice.get("message");
        if (!(messageObj instanceof Map<?, ?> message)) {
            return null;
        }
        Object content = message.get("content");
        return content == null ? null : content.toString();
    }

    private TravelPlanResponse parseDeepSeekResponse(TravelPlanRequest request, String content) {
        if (content == null || content.isBlank()) {
            return fallback.fallback(request);
        }

        String json = extractJson(content);
        if (json == null || json.isBlank()) {
            return fallback.fallback(request);
        }

        try {
            DeepSeekTravelPlan parsed = OBJECT_MAPPER.readValue(json, DeepSeekTravelPlan.class);
            TravelPlanResponse response = new TravelPlanResponse();
            response.setDestination(request.getDestination());
            response.setDays(request.getDays());
            response.setBudget(request.getBudget());
            response.setPreference(request.getPreference());
            response.setSummary(parsed.getSummary() == null || parsed.getSummary().isBlank() ? content.trim() : parsed.getSummary().trim());
            response.setRecommendations(parsed.getRecommendations() == null ? List.of() : parsed.getRecommendations());
            response.setItinerary(parsed.toItinerary());
            return response;
        } catch (Exception ex) {
            return fallback.fallback(request);
        }
    }

    private String extractJson(String content) {
        String trimmed = content.trim();
        if (trimmed.startsWith("```")) {
            int firstBrace = trimmed.indexOf('{');
            int lastBrace = trimmed.lastIndexOf('}');
            if (firstBrace >= 0 && lastBrace > firstBrace) {
                return trimmed.substring(firstBrace, lastBrace + 1);
            }
            return null;
        }
        int firstBrace = trimmed.indexOf('{');
        int lastBrace = trimmed.lastIndexOf('}');
        if (firstBrace >= 0 && lastBrace > firstBrace) {
            return trimmed.substring(firstBrace, lastBrace + 1);
        }
        return null;
    }

    private TravelPlanResponse buildLocalPlan(TravelPlanRequest request) {
        List<TravelPlanResponse.DailyPlan> itinerary = new ArrayList<>();
        for (int day = 1; day <= request.getDays(); day++) {
            TravelPlanResponse.DailyPlan plan = new TravelPlanResponse.DailyPlan();
            plan.setDay(day);
            plan.setTitle("第" + day + "天行程");
            plan.setDetail(buildDayDetail(request, day));
            itinerary.add(plan);
        }

        TravelPlanResponse response = new TravelPlanResponse();
        response.setDestination(request.getDestination());
        response.setDays(request.getDays());
        response.setBudget(request.getBudget());
        response.setPreference(request.getPreference());
        response.setSummary(buildSummary(request));
        response.setRecommendations(buildRecommendations(request));
        response.setItinerary(itinerary);
        return response;
    }

    private String buildSummary(TravelPlanRequest request) {
        return "为你生成了一份前往" + request.getDestination() + "的" + request.getDays() + "日旅游方案，偏好为"
                + request.getPreference() + "，预算约" + request.getBudget() + "元。";
    }

    private List<String> buildRecommendations(TravelPlanRequest request) {
        List<String> recommendations = new ArrayList<>();
        recommendations.add(request.getDestination() + "热门景点优先安排");
        recommendations.add("根据预算筛选合适酒店");
        recommendations.add("保留半天自由活动时间");
        return recommendations;
    }

    private String buildDayDetail(TravelPlanRequest request, int day) {
        if (day == 1) {
            return "抵达" + request.getDestination() + "后办理入住，下午游览城市地标并体验当地美食。";
        }
        if (day == request.getDays()) {
            return "安排轻松返程行程，可补充购物、休闲或拍照打卡。";
        }
        return "上午安排景点游览，下午安排酒店休息或周边体验活动。";
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class DeepSeekTravelPlan {
        private String summary;
        private List<String> recommendations;
        private List<DeepSeekItineraryItem> itinerary;

        public String getSummary() {
            return summary;
        }

        public void setSummary(String summary) {
            this.summary = summary;
        }

        public List<String> getRecommendations() {
            return recommendations;
        }

        public void setRecommendations(List<String> recommendations) {
            this.recommendations = recommendations;
        }

        public List<DeepSeekItineraryItem> getItinerary() {
            return itinerary;
        }

        public void setItinerary(List<DeepSeekItineraryItem> itinerary) {
            this.itinerary = itinerary;
        }

        public List<TravelPlanResponse.DailyPlan> toItinerary() {
            if (itinerary == null) {
                return List.of();
            }
            List<TravelPlanResponse.DailyPlan> plans = new ArrayList<>();
            for (DeepSeekItineraryItem item : itinerary) {
                TravelPlanResponse.DailyPlan plan = new TravelPlanResponse.DailyPlan();
                plan.setDay(item.getDay());
                plan.setTitle(item.getTitle());
                plan.setDetail(item.getDetail());
                plans.add(plan);
            }
            return plans;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class DeepSeekItineraryItem {
        private Integer day;
        private String title;
        private String detail;

        public Integer getDay() {
            return day;
        }

        public void setDay(Integer day) {
            this.day = day;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDetail() {
            return detail;
        }

        public void setDetail(String detail) {
            this.detail = detail;
        }
    }
}
