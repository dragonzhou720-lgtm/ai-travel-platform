package com.example.ai.config;

import com.example.ai.dto.TravelPlanRequest;
import com.example.ai.dto.TravelPlanResponse;

import java.util.List;

public class AiTravelPlanFallback {

    public TravelPlanResponse fallback(TravelPlanRequest request) {
        TravelPlanResponse response = new TravelPlanResponse();
        response.setDestination(request.getDestination());
        response.setDays(request.getDays());
        response.setBudget(request.getBudget());
        response.setPreference(request.getPreference());
        response.setSummary("AI 服务当前繁忙，已返回预设旅行方案，请稍后重试。");
        response.setRecommendations(List.of(
                "优先选择城市核心景点",
                "预算充足时可安排高评分酒店",
                "建议保留自由活动时间"
        ));
        TravelPlanResponse.DailyPlan dailyPlan = new TravelPlanResponse.DailyPlan();
        dailyPlan.setDay(1);
        dailyPlan.setTitle("默认行程");
        dailyPlan.setDetail("AI 降级兜底方案：可先游览城市地标并安排休闲活动。");
        response.setItinerary(List.of(dailyPlan));
        return response;
    }
}
