package com.example.ai.service;

import com.example.ai.dto.TravelPlanRequest;
import com.example.ai.dto.TravelPlanResponse;

public interface TravelPlanService {
    TravelPlanResponse generatePlan(TravelPlanRequest request);
}
