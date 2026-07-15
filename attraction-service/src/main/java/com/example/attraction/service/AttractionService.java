package com.example.attraction.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.attraction.entity.Attraction;
import com.example.attraction.mapper.AttractionMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class AttractionService {

    private final AttractionMapper attractionMapper;

    public AttractionService(AttractionMapper attractionMapper) {
        this.attractionMapper = attractionMapper;
    }

    public List<Map<String, Object>> searchAttractions(String city, String keyword) {
        log.info("Searching attractions for city: {}, keyword: {}", city, keyword);
        
        LambdaQueryWrapper<Attraction> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Attraction::getStatus, 1);
        
        if (city != null && !city.isEmpty()) {
            wrapper.eq(Attraction::getCity, city);
            log.info("Adding city filter: {}", city);
        }
        
        if (keyword != null && !keyword.isEmpty()) {
            wrapper.and(w -> w.like(Attraction::getName, keyword).or().like(Attraction::getDescription, keyword));
            log.info("Adding keyword filter: {}", keyword);
        }
        
        List<Attraction> attractions = attractionMapper.selectList(wrapper);
        log.info("Found {} attractions", attractions.size());
        
        return attractions.stream().map(this::toMap).toList();
    }

    public Map<String, Object> getAttractionDetail(Long id) {
        Attraction attraction = attractionMapper.selectById(id);
        return attraction != null ? toMap(attraction) : null;
    }

    public List<Map<String, Object>> getAllAttractions() {
        log.info("Getting all attractions");
        LambdaQueryWrapper<Attraction> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Attraction::getStatus, 1);
        List<Attraction> attractions = attractionMapper.selectList(wrapper);
        log.info("Found {} attractions total", attractions.size());
        return attractions.stream().map(this::toMap).toList();
    }

    private Map<String, Object> toMap(Attraction attraction) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", attraction.getId());
        map.put("name", attraction.getName());
        map.put("city", attraction.getCity());
        map.put("address", attraction.getAddress());
        map.put("ticketPrice", attraction.getTicketPrice());
        map.put("rating", attraction.getRating());
        map.put("description", attraction.getDescription());
        map.put("openTime", attraction.getOpenTime());
        map.put("tags", attraction.getTags());
        map.put("coverImage", attraction.getCoverImage());
        map.put("status", attraction.getStatus());
        return map;
    }
}