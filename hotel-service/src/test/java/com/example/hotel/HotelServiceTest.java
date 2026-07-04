package com.example.hotel;

import com.example.hotel.dto.HotelDTO;
import com.example.hotel.entity.Hotel;
import com.example.hotel.service.HotelService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class HotelServiceTest {

    @Autowired
    private HotelService hotelService;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Test
    void testCreateHotel() {
        HotelDTO dto = new HotelDTO();
        dto.setName("测试酒店");
        dto.setCity("北京");
        dto.setPrice(BigDecimal.valueOf(500.00));
        dto.setRating(BigDecimal.valueOf(4.5));
        dto.setAddress("北京市朝阳区");
        dto.setImageUrl("https://example.com/test.jpg");

        Hotel hotel = hotelService.create(dto);

        assertNotNull(hotel);
        assertEquals("测试酒店", hotel.getName());
        assertEquals("北京", hotel.getCity());
        assertEquals(BigDecimal.valueOf(500.00), hotel.getPrice());
    }

    @Test
    void testGetById() {
        HotelDTO dto = new HotelDTO();
        dto.setName("测试酒店2");
        dto.setCity("上海");
        dto.setPrice(BigDecimal.valueOf(800.00));
        dto.setRating(BigDecimal.valueOf(4.8));
        dto.setAddress("上海市浦东新区");

        Hotel created = hotelService.create(dto);
        Hotel found = hotelService.getById(created.getId());

        assertNotNull(found);
        assertEquals("测试酒店2", found.getName());
        assertEquals("上海", found.getCity());
    }

    @Test
    void testUpdateHotel() {
        HotelDTO dto = new HotelDTO();
        dto.setName("测试酒店3");
        dto.setCity("广州");
        dto.setPrice(BigDecimal.valueOf(600.00));
        dto.setRating(BigDecimal.valueOf(4.6));
        dto.setAddress("广州市天河区");

        Hotel created = hotelService.create(dto);

        HotelDTO updateDto = new HotelDTO();
        updateDto.setName("测试酒店3-修改");
        updateDto.setCity("深圳");
        updateDto.setPrice(BigDecimal.valueOf(700.00));
        updateDto.setRating(BigDecimal.valueOf(4.7));
        updateDto.setAddress("深圳市南山区");

        Hotel updated = hotelService.update(created.getId(), updateDto);

        assertNotNull(updated);
        assertEquals("测试酒店3-修改", updated.getName());
        assertEquals("深圳", updated.getCity());
        assertEquals(BigDecimal.valueOf(700.00), updated.getPrice());
    }

    @Test
    void testDeleteHotel() {
        HotelDTO dto = new HotelDTO();
        dto.setName("测试酒店4");
        dto.setCity("成都");
        dto.setPrice(BigDecimal.valueOf(400.00));
        dto.setRating(BigDecimal.valueOf(4.5));

        Hotel created = hotelService.create(dto);
        Long id = created.getId();

        hotelService.delete(id);

        assertNull(hotelService.getById(id));
    }

    @Test
    void testGetAllCities() {
        redisTemplate.delete("hotel:cities");

        HotelDTO dto1 = new HotelDTO();
        dto1.setName("酒店A");
        dto1.setCity("北京");
        hotelService.create(dto1);

        HotelDTO dto2 = new HotelDTO();
        dto2.setName("酒店B");
        dto2.setCity("上海");
        hotelService.create(dto2);

        List<String> cities = hotelService.getAllCities();

        assertNotNull(cities);
        assertTrue(cities.size() >= 2);
    }

    @Test
    void testGetHotHotels() {
        redisTemplate.delete("hotel:hot");

        for (int i = 0; i < 5; i++) {
            HotelDTO dto = new HotelDTO();
            dto.setName("热门酒店" + i);
            dto.setCity("杭州");
            dto.setPrice(BigDecimal.valueOf(500 + i * 100));
            dto.setRating(BigDecimal.valueOf(4.0 + i * 0.2));
            hotelService.create(dto);
        }

        List<Hotel> hotHotels = hotelService.getHotHotels(3);

        assertNotNull(hotHotels);
        assertEquals(3, hotHotels.size());
    }

    @Test
    void testRecommend() {
        for (int i = 0; i < 4; i++) {
            HotelDTO dto = new HotelDTO();
            dto.setName("推荐酒店" + i);
            dto.setCity("杭州");
            dto.setPrice(BigDecimal.valueOf(600 + i * 100));
            dto.setRating(BigDecimal.valueOf(4.2 + i * 0.15));
            hotelService.create(dto);
        }

        List<Hotel> recommended = hotelService.recommend("杭州", 3);

        assertNotNull(recommended);
        assertEquals(3, recommended.size());
    }

    @Test
    void testRedisCache() {
        HotelDTO dto = new HotelDTO();
        dto.setName("缓存测试酒店");
        dto.setCity("南京");
        dto.setPrice(BigDecimal.valueOf(550.00));
        dto.setRating(BigDecimal.valueOf(4.5));

        hotelService.create(dto);

        hotelService.getAllCities();
        Boolean exists = redisTemplate.hasKey("hotel:cities");
        assertTrue(exists);

        redisTemplate.delete("hotel:cities");
        exists = redisTemplate.hasKey("hotel:cities");
        assertFalse(exists);

        hotelService.getAllCities();
        exists = redisTemplate.hasKey("hotel:cities");
        assertTrue(exists);
    }
}