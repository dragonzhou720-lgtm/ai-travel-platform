package com.example.route.config;

import com.example.route.entity.Route;
import com.example.route.mapper.RouteMapper;
import com.example.route.service.RouteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class RouteInitializer implements CommandLineRunner {

    private final RouteMapper routeMapper;
    private final RouteService routeService;

    public RouteInitializer(RouteMapper routeMapper, RouteService routeService) {
        this.routeMapper = routeMapper;
        this.routeService = routeService;
    }

    @Override
    public void run(String... args) {
        log.info("Initializing hot route counters from database...");
        
        List<Route> routes = routeMapper.findAllRoutes();
        for (Route route : routes) {
            routeService.incrementHotRouteCount(route.getDestination(), route.getDays(), route.getPreference());
        }
        
        log.info("Hot route counters initialized. Total unique route combinations: {}", 
                routeService.getHotRoutes(100).size());
    }
}