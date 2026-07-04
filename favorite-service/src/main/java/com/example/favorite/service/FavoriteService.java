package com.example.favorite.service;

import com.example.favorite.dto.FavoriteDTO;
import com.example.favorite.entity.Favorite;
import com.example.favorite.mapper.FavoriteMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class FavoriteService {

    private final FavoriteMapper favoriteMapper;
    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;

    private static final String USER_FAVORITES_KEY = "favorite:user:";
    private static final long CACHE_EXPIRE_TIME = 15;

    public FavoriteService(FavoriteMapper favoriteMapper, StringRedisTemplate redisTemplate, ObjectMapper objectMapper) {
        this.favoriteMapper = favoriteMapper;
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
    }

    @Transactional
    public Favorite add(FavoriteDTO dto) {
        int count = favoriteMapper.countByUserAndTarget(dto.getUserId(), dto.getTargetId(), dto.getTargetType());
        if (count > 0) {
            return null;
        }

        Favorite favorite = new Favorite();
        favorite.setUserId(dto.getUserId());
        favorite.setTargetId(dto.getTargetId());
        favorite.setTargetType(dto.getTargetType());
        favoriteMapper.insert(favorite);
        clearUserCache(dto.getUserId());
        return favorite;
    }

    @Transactional
    public void remove(Long userId, Long targetId, String targetType) {
        favoriteMapper.deleteByUserAndTarget(userId, targetId, targetType);
        clearUserCache(userId);
    }

    public List<Favorite> getFavorites(Long userId) {
        String key = USER_FAVORITES_KEY + userId;
        if (redisTemplate.hasKey(key)) {
            String json = redisTemplate.opsForValue().get(key);
            try {
                return objectMapper.readValue(json, new TypeReference<List<Favorite>>() {});
            } catch (JsonProcessingException e) {
                redisTemplate.delete(key);
            }
        }
        List<Favorite> favorites = favoriteMapper.findByUserId(userId);
        try {
            String json = objectMapper.writeValueAsString(favorites);
            redisTemplate.opsForValue().set(key, json, CACHE_EXPIRE_TIME, TimeUnit.MINUTES);
        } catch (JsonProcessingException e) {
        }
        return favorites;
    }

    public List<Favorite> getFavoritesByType(Long userId, String targetType) {
        String key = USER_FAVORITES_KEY + userId + ":" + targetType;
        if (redisTemplate.hasKey(key)) {
            String json = redisTemplate.opsForValue().get(key);
            try {
                return objectMapper.readValue(json, new TypeReference<List<Favorite>>() {});
            } catch (JsonProcessingException e) {
                redisTemplate.delete(key);
            }
        }
        List<Favorite> favorites = favoriteMapper.findByUserIdAndType(userId, targetType);
        try {
            String json = objectMapper.writeValueAsString(favorites);
            redisTemplate.opsForValue().set(key, json, CACHE_EXPIRE_TIME, TimeUnit.MINUTES);
        } catch (JsonProcessingException e) {
        }
        return favorites;
    }

    public boolean isFavorite(Long userId, Long targetId, String targetType) {
        int count = favoriteMapper.countByUserAndTarget(userId, targetId, targetType);
        return count > 0;
    }

    private void clearUserCache(Long userId) {
        redisTemplate.delete(USER_FAVORITES_KEY + userId);
        redisTemplate.delete(USER_FAVORITES_KEY + userId + ":ATTRACTION");
        redisTemplate.delete(USER_FAVORITES_KEY + userId + ":ROUTE");
    }
}