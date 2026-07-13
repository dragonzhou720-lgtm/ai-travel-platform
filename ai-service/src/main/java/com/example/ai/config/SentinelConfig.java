package com.example.ai.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SentinelConfig {

    @Bean
    public AiTravelPlanFallback aiTravelPlanFallback() {
        return new AiTravelPlanFallback();
    }
}
