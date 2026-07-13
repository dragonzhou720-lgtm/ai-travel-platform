package com.example.route.rabbitmq;

import com.example.route.config.RabbitMQConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class RouteMessageConsumer {

    private final Map<String, Integer> hotRouteCache = new ConcurrentHashMap<>();
    private final Map<Long, Map<String, Object>> routeHistory = new ConcurrentHashMap<>();

    @RabbitListener(queues = RabbitMQConfig.ROUTE_GENERATED_QUEUE)
    public void handleRouteGenerated(Map<String, Object> message) {
        log.info("Received route generated message: {}", message);
        Long routeId = ((Number) message.get("routeId")).longValue();
        String city = (String) message.get("city");
        Integer days = (Integer) message.get("days");
        String style = (String) message.get("style");
        
        log.info("Route {} generated for {} - {} days - {} style", routeId, city, days, style);
    }

    @RabbitListener(queues = RabbitMQConfig.HOT_ROUTE_STATS_QUEUE)
    public void handleHotRouteStats(Map<String, Object> message) {
        log.info("Received hot route stats message: {}", message);
        String city = (String) message.get("city");
        Integer days = (Integer) message.get("days");
        String style = (String) message.get("style");
        Integer count = (Integer) message.get("count");
        
        String key = city + ":" + days + ":" + style;
        hotRouteCache.put(key, count);
        
        log.info("Hot route stats updated: {} -> count={}", key, count);
    }

    @RabbitListener(queues = RabbitMQConfig.HISTORY_SAVE_QUEUE)
    public void handleHistorySave(Map<String, Object> message) {
        log.info("Received history save message: {}", message);
        Long routeId = ((Number) message.get("routeId")).longValue();
        
        routeHistory.put(routeId, message);
        
        log.info("Route history saved: routeId={}, userId={}", 
                routeId, message.get("userId"));
    }

    public Map<String, Integer> getHotRouteCache() {
        return hotRouteCache;
    }

    public Map<Long, Map<String, Object>> getRouteHistory() {
        return routeHistory;
    }
}