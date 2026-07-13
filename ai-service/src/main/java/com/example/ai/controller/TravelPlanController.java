package com.example.ai.controller;

import com.example.ai.dto.TravelPlanRequest;
import com.example.ai.dto.TravelPlanResponse;
import com.example.ai.service.TravelPlanService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ai")
public class TravelPlanController {

    private final TravelPlanService travelPlanService;

    public TravelPlanController(TravelPlanService travelPlanService) {
        this.travelPlanService = travelPlanService;
    }

    @PostMapping("/plan")
    public TravelPlanResponse generatePlan(@RequestBody TravelPlanRequest request) {
        return travelPlanService.generatePlan(request);
    }
}
