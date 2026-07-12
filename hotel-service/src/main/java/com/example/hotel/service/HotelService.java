package com.example.hotel.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.hotel.dto.HotelDTO;
import com.example.hotel.dto.HotelQueryDTO;
import com.example.hotel.entity.Hotel;
import com.example.hotel.mapper.HotelMapper;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class HotelService {

    private final HotelMapper hotelMapper;
    private final RedisTemplate<String, Object> redisTemplate;

    private static final String HOT_HOTEL_KEY = "hotel:hot";
    private static final String ALL_CITIES_KEY = "hotel:cities";
    private static final String HOTEL_DETAIL_KEY = "hotel:detail:";
    private static final long CACHE_EXPIRE_TIME = 30;

    public HotelService(HotelMapper hotelMapper, RedisTemplate<String, Object> redisTemplate) {
        this.hotelMapper = hotelMapper;
        this.redisTemplate = redisTemplate;
    }

    @Transactional
    public Hotel create(HotelDTO dto) {
        Hotel hotel = new Hotel();
        hotel.setName(dto.getName());
        hotel.setCity(dto.getCity());
        hotel.setAddress(dto.getAddress());
        hotel.setPricePerNight(dto.getPricePerNight());
        hotel.setRating(dto.getRating());
        hotel.setStarLevel(dto.getStarLevel());
        hotel.setDescription(dto.getDescription());
        hotel.setTags(dto.getTags());
        hotel.setCoverImage(dto.getCoverImage());
        hotel.setStatus(1);
        hotelMapper.insert(hotel);
        clearCache();
        return hotel;
    }

    @Transactional
    public Hotel update(Long id, HotelDTO dto) {
        Hotel hotel = hotelMapper.selectById(id);
        if (hotel != null) {
            hotel.setName(dto.getName());
            hotel.setCity(dto.getCity());
            hotel.setAddress(dto.getAddress());
            hotel.setPricePerNight(dto.getPricePerNight());
            hotel.setRating(dto.getRating());
            hotel.setStarLevel(dto.getStarLevel());
            hotel.setDescription(dto.getDescription());
            hotel.setTags(dto.getTags());
            hotel.setCoverImage(dto.getCoverImage());
            hotelMapper.updateById(hotel);
            clearCache();
            redisTemplate.delete(HOTEL_DETAIL_KEY + id);
        }
        return hotel;
    }

    @Transactional
    public void delete(Long id) {
        hotelMapper.deleteById(id);
        clearCache();
        redisTemplate.delete(HOTEL_DETAIL_KEY + id);
    }

    @SuppressWarnings("unchecked")
    public Hotel getById(Long id) {
        String key = HOTEL_DETAIL_KEY + id;
        if (redisTemplate.hasKey(key)) {
            return (Hotel) redisTemplate.opsForValue().get(key);
        }
        Hotel hotel = hotelMapper.selectById(id);
        if (hotel != null) {
            redisTemplate.opsForValue().set(key, hotel, CACHE_EXPIRE_TIME, TimeUnit.MINUTES);
        }
        return hotel;
    }

    @SuppressWarnings("unchecked")
    public List<String> getAllCities() {
        if (redisTemplate.hasKey(ALL_CITIES_KEY)) {
            return (List<String>) redisTemplate.opsForValue().get(ALL_CITIES_KEY);
        }
        List<String> cities = hotelMapper.findAllCities();
        redisTemplate.opsForValue().set(ALL_CITIES_KEY, cities, CACHE_EXPIRE_TIME, TimeUnit.MINUTES);
        return cities;
    }

    @SuppressWarnings("unchecked")
    public List<Hotel> getHotHotels(int limit) {
        if (redisTemplate.hasKey(HOT_HOTEL_KEY)) {
            return (List<Hotel>) redisTemplate.opsForValue().get(HOT_HOTEL_KEY);
        }
        List<Hotel> hotels = hotelMapper.findHotHotels(limit);
        redisTemplate.opsForValue().set(HOT_HOTEL_KEY, hotels, CACHE_EXPIRE_TIME, TimeUnit.MINUTES);
        return hotels;
    }

    public List<Hotel> recommend(String city, int limit) {
        LambdaQueryWrapper<Hotel> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Hotel::getStatus, 1);
        if (city != null && !city.isEmpty()) {
            wrapper.eq(Hotel::getCity, city);
        }
        wrapper.orderByDesc(Hotel::getRating);
        wrapper.last("LIMIT " + limit);
        return hotelMapper.selectList(wrapper);
    }

    public Page<Hotel> query(HotelQueryDTO queryDTO, int pageNum, int pageSize) {
        LambdaQueryWrapper<Hotel> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Hotel::getStatus, 1);

        if (queryDTO.getCity() != null && !queryDTO.getCity().isEmpty()) {
            wrapper.eq(Hotel::getCity, queryDTO.getCity());
        }

        if (queryDTO.getMinPrice() != null) {
            wrapper.ge(Hotel::getPricePerNight, queryDTO.getMinPrice());
        }

        if (queryDTO.getMaxPrice() != null) {
            wrapper.le(Hotel::getPricePerNight, queryDTO.getMaxPrice());
        }

        if (queryDTO.getSortBy() != null) {
            if ("rating".equals(queryDTO.getSortBy())) {
                if ("asc".equalsIgnoreCase(queryDTO.getSortOrder())) {
                    wrapper.orderByAsc(Hotel::getRating);
                } else {
                    wrapper.orderByDesc(Hotel::getRating);
                }
            } else if ("price".equals(queryDTO.getSortBy())) {
                if ("asc".equalsIgnoreCase(queryDTO.getSortOrder())) {
                    wrapper.orderByAsc(Hotel::getPricePerNight);
                } else {
                    wrapper.orderByDesc(Hotel::getPricePerNight);
                }
            }
        } else {
            wrapper.orderByDesc(Hotel::getRating);
        }

        Page<Hotel> page = new Page<>(pageNum, pageSize);
        return hotelMapper.selectPage(page, wrapper);
    }

    private void clearCache() {
        redisTemplate.delete(HOT_HOTEL_KEY);
        redisTemplate.delete(ALL_CITIES_KEY);
    }
}