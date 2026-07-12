package com.example.attraction.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.attraction.dto.AttractionDTO;
import com.example.attraction.dto.AttractionQueryDTO;
import com.example.attraction.entity.Attraction;
import com.example.attraction.mapper.AttractionMapper;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class AttractionService {

    private final AttractionMapper attractionMapper;
    private final RedisTemplate<String, Object> redisTemplate;

    private static final String HOT_ATTRACTION_KEY = "attraction:hot";
    private static final String ALL_CITIES_KEY = "attraction:cities";
    private static final String ATTRACTION_DETAIL_KEY = "attraction:detail:";
    private static final long CACHE_EXPIRE_TIME = 30;

    public AttractionService(AttractionMapper attractionMapper, RedisTemplate<String, Object> redisTemplate) {
        this.attractionMapper = attractionMapper;
        this.redisTemplate = redisTemplate;
    }

    @Transactional
    public Attraction create(AttractionDTO dto) {
        Attraction attraction = new Attraction();
        attraction.setName(dto.getName());
        attraction.setCity(dto.getCity());
        attraction.setAddress(dto.getAddress());
        attraction.setTicketPrice(dto.getTicketPrice());
        attraction.setRating(dto.getRating());
        attraction.setDescription(dto.getDescription());
        attraction.setOpenTime(dto.getOpenTime());
        attraction.setTags(dto.getTags());
        attraction.setCoverImage(dto.getCoverImage());
        attraction.setStatus(1);
        attractionMapper.insert(attraction);
        clearCache();
        return attraction;
    }

    @Transactional
    public Attraction update(Long id, AttractionDTO dto) {
        Attraction attraction = attractionMapper.selectById(id);
        if (attraction != null) {
            attraction.setName(dto.getName());
            attraction.setCity(dto.getCity());
            attraction.setAddress(dto.getAddress());
            attraction.setTicketPrice(dto.getTicketPrice());
            attraction.setRating(dto.getRating());
            attraction.setDescription(dto.getDescription());
            attraction.setOpenTime(dto.getOpenTime());
            attraction.setTags(dto.getTags());
            attraction.setCoverImage(dto.getCoverImage());
            attractionMapper.updateById(attraction);
            clearCache();
            redisTemplate.delete(ATTRACTION_DETAIL_KEY + id);
        }
        return attraction;
    }

    @Transactional
    public void delete(Long id) {
        attractionMapper.deleteById(id);
        clearCache();
        redisTemplate.delete(ATTRACTION_DETAIL_KEY + id);
    }

    public Attraction getById(Long id) {
        String key = ATTRACTION_DETAIL_KEY + id;
        if (Boolean.TRUE.equals(redisTemplate.hasKey(key))) {
            Object cached = redisTemplate.opsForValue().get(key);
            if (cached instanceof Attraction) {
                return (Attraction) cached;
            }
        }
        Attraction attraction = attractionMapper.selectById(id);
        if (attraction != null) {
            redisTemplate.opsForValue().set(key, attraction, CACHE_EXPIRE_TIME, TimeUnit.MINUTES);
        }
        return attraction;
    }

    public List<String> getAllCities() {
        if (Boolean.TRUE.equals(redisTemplate.hasKey(ALL_CITIES_KEY))) {
            Object cached = redisTemplate.opsForValue().get(ALL_CITIES_KEY);
            if (cached instanceof List) {
                return (List<String>) cached;
            }
        }
        List<String> cities = attractionMapper.findAllCities();
        redisTemplate.opsForValue().set(ALL_CITIES_KEY, cities, CACHE_EXPIRE_TIME, TimeUnit.MINUTES);
        return cities;
    }

    public List<Attraction> getHotAttractions(int limit) {
        if (Boolean.TRUE.equals(redisTemplate.hasKey(HOT_ATTRACTION_KEY))) {
            Object cached = redisTemplate.opsForValue().get(HOT_ATTRACTION_KEY);
            if (cached instanceof List) {
                return (List<Attraction>) cached;
            }
        }
        List<Attraction> attractions = attractionMapper.findHotAttractions(limit);
        redisTemplate.opsForValue().set(HOT_ATTRACTION_KEY, attractions, CACHE_EXPIRE_TIME, TimeUnit.MINUTES);
        return attractions;
    }

    public Page<Attraction> query(AttractionQueryDTO queryDTO, int pageNum, int pageSize) {
        LambdaQueryWrapper<Attraction> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Attraction::getStatus, 1);

        if (queryDTO.getCity() != null && !queryDTO.getCity().isEmpty()) {
            wrapper.eq(Attraction::getCity, queryDTO.getCity());
        }

        if (queryDTO.getMinPrice() != null) {
            wrapper.ge(Attraction::getTicketPrice, queryDTO.getMinPrice());
        }

        if (queryDTO.getMaxPrice() != null) {
            wrapper.le(Attraction::getTicketPrice, queryDTO.getMaxPrice());
        }

        if (queryDTO.getSortBy() != null) {
            if ("rating".equals(queryDTO.getSortBy())) {
                if ("asc".equalsIgnoreCase(queryDTO.getSortOrder())) {
                    wrapper.orderByAsc(Attraction::getRating);
                } else {
                    wrapper.orderByDesc(Attraction::getRating);
                }
            } else if ("price".equals(queryDTO.getSortBy())) {
                if ("asc".equalsIgnoreCase(queryDTO.getSortOrder())) {
                    wrapper.orderByAsc(Attraction::getTicketPrice);
                } else {
                    wrapper.orderByDesc(Attraction::getTicketPrice);
                }
            }
        } else {
            wrapper.orderByDesc(Attraction::getRating);
        }

        Page<Attraction> page = new Page<>(pageNum, pageSize);
        return attractionMapper.selectPage(page, wrapper);
    }

    private void clearCache() {
        redisTemplate.delete(HOT_ATTRACTION_KEY);
        redisTemplate.delete(ALL_CITIES_KEY);
    }
}