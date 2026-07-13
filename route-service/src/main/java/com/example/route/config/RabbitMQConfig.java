package com.example.route.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String ROUTE_GENERATED_QUEUE = "route.generated.queue";
    public static final String HOT_ROUTE_STATS_QUEUE = "hot.route.stats.queue";
    public static final String HISTORY_SAVE_QUEUE = "history.save.queue";
    
    public static final String ROUTE_EXCHANGE = "route.exchange";
    
    public static final String ROUTE_GENERATED_ROUTING_KEY = "route.generated";
    public static final String HOT_ROUTE_STATS_ROUTING_KEY = "hot.route.stats";
    public static final String HISTORY_SAVE_ROUTING_KEY = "history.save";

    @Bean
    public Queue routeGeneratedQueue() {
        return QueueBuilder.durable(ROUTE_GENERATED_QUEUE).build();
    }

    @Bean
    public Queue hotRouteStatsQueue() {
        return QueueBuilder.durable(HOT_ROUTE_STATS_QUEUE).build();
    }

    @Bean
    public Queue historySaveQueue() {
        return QueueBuilder.durable(HISTORY_SAVE_QUEUE).build();
    }

    @Bean
    public TopicExchange routeExchange() {
        return ExchangeBuilder.topicExchange(ROUTE_EXCHANGE).durable(true).build();
    }

    @Bean
    public Binding routeGeneratedBinding(Queue routeGeneratedQueue, TopicExchange routeExchange) {
        return BindingBuilder.bind(routeGeneratedQueue).to(routeExchange).with(ROUTE_GENERATED_ROUTING_KEY);
    }

    @Bean
    public Binding hotRouteStatsBinding(Queue hotRouteStatsQueue, TopicExchange routeExchange) {
        return BindingBuilder.bind(hotRouteStatsQueue).to(routeExchange).with(HOT_ROUTE_STATS_ROUTING_KEY);
    }

    @Bean
    public Binding historySaveBinding(Queue historySaveQueue, TopicExchange routeExchange) {
        return BindingBuilder.bind(historySaveQueue).to(routeExchange).with(HISTORY_SAVE_ROUTING_KEY);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        return rabbitTemplate;
    }
}