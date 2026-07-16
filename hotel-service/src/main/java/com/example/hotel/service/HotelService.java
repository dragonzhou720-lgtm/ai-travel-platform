package com.example.hotel.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.hotel.entity.Hotel;
import com.example.hotel.mapper.HotelMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class HotelService {

    private final HotelMapper hotelMapper;

    public HotelService(HotelMapper hotelMapper) {
        this.hotelMapper = hotelMapper;
    }

    public List<Map<String, Object>> searchHotels(String city, String keyword) {
        log.info("Searching hotels for city: {}, keyword: {}", city, keyword);
        
        LambdaQueryWrapper<Hotel> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Hotel::getStatus, 1);
        
        if (city != null && !city.isEmpty()) {
            wrapper.eq(Hotel::getCity, city);
            log.info("Adding city filter: {}", city);
        }
        
        if (keyword != null && !keyword.isEmpty()) {
            wrapper.and(w -> w.like(Hotel::getName, keyword).or().like(Hotel::getDescription, keyword));
            log.info("Adding keyword filter: {}", keyword);
        }
        
        List<Hotel> hotels = hotelMapper.selectList(wrapper);
        log.info("Found {} hotels", hotels.size());
        
        return hotels.stream().map(this::toMap).toList();
    }

    public Map<String, Object> getHotelDetail(Long id) {
        Hotel hotel = hotelMapper.selectById(id);
        return hotel != null ? toMap(hotel) : null;
    }

    private Map<String, Object> toMap(Hotel hotel) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", hotel.getId());
        map.put("name", hotel.getName());
        map.put("city", hotel.getCity());
        map.put("address", hotel.getAddress());
        map.put("pricePerNight", hotel.getPricePerNight());
        map.put("rating", hotel.getRating());
        map.put("star", hotel.getStarLevel() + "-star");
        map.put("description", hotel.getDescription());
        String tags = hotel.getTags();
        if (tags != null && !tags.isEmpty()) {
            map.put("tags", tags.split(","));
        } else {
            map.put("tags", new String[0]);
        }
        map.put("coverImage", hotel.getCoverImage());
        map.put("status", hotel.getStatus());
        return map;
    }
}