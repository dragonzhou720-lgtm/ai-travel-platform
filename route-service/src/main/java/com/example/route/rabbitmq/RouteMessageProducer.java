package com.example.route.rabbitmq;

import com.example.route.config.RabbitMQConfig;
import com.example.route.dto.RouteResponse;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class RouteMessageProducer {

    private final RabbitTemplate rabbitTemplate;

    public RouteMessageProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendRouteGenerated(RouteResponse route) {
        Map<String, Object> message = new HashMap<>();
        message.put("routeId", route.getId());
        message.put("city", route.getCity());
        message.put("days", route.getDays());
        message.put("style", route.getStyle());
        message.put("createdAt", route.getCreatedAt());
        
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.ROUTE_EXCHANGE,
                RabbitMQConfig.ROUTE_GENERATED_ROUTING_KEY,
                message
        );
    }

    public void sendHotRouteStats(String city, Integer days, String style, Integer count) {
        Map<String, Object> message = new HashMap<>();
        message.put("city", city);
        message.put("days", days);
        message.put("style", style);
        message.put("count", count);
        message.put("timestamp", System.currentTimeMillis());
        
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.ROUTE_EXCHANGE,
                RabbitMQConfig.HOT_ROUTE_STATS_ROUTING_KEY,
                message
        );
    }

    public void sendHistorySave(RouteResponse route, String userId) {
        Map<String, Object> message = new HashMap<>();
        message.put("routeId", route.getId());
        message.put("city", route.getCity());
        message.put("days", route.getDays());
        message.put("style", route.getStyle());
        message.put("userId", userId);
        message.put("createdAt", route.getCreatedAt());
        
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.ROUTE_EXCHANGE,
                RabbitMQConfig.HISTORY_SAVE_ROUTING_KEY,
                message
        );
    }
}