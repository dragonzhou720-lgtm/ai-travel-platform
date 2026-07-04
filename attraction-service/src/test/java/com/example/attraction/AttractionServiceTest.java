package com.example.attraction;

import com.example.attraction.dto.AttractionDTO;
import com.example.attraction.entity.Attraction;
import com.example.attraction.service.AttractionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AttractionServiceTest {

    @Autowired
    private AttractionService attractionService;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Test
    void testCreateAttraction() {
        AttractionDTO dto = new AttractionDTO();
        dto.setName("测试景点");
        dto.setCity("北京");
        dto.setPrice(BigDecimal.valueOf(50.00));
        dto.setRating(BigDecimal.valueOf(4.5));
        dto.setDescription("测试景点描述");
        dto.setImageUrl("https://example.com/test.jpg");

        Attraction attraction = attractionService.create(dto);

        assertNotNull(attraction);
        assertEquals("测试景点", attraction.getName());
        assertEquals("北京", attraction.getCity());
        assertEquals(BigDecimal.valueOf(50.00), attraction.getPrice());
    }

    @Test
    void testGetById() {
        AttractionDTO dto = new AttractionDTO();
        dto.setName("测试景点2");
        dto.setCity("上海");
        dto.setPrice(BigDecimal.valueOf(100.00));
        dto.setRating(BigDecimal.valueOf(4.8));
        dto.setDescription("测试景点描述2");

        Attraction created = attractionService.create(dto);
        redisTemplate.delete("attraction:detail:" + created.getId());
        Attraction found = attractionService.getById(created.getId());

        assertNotNull(found);
        assertEquals("测试景点2", found.getName());
        assertEquals("上海", found.getCity());
    }

    @Test
    void testUpdateAttraction() {
        AttractionDTO dto = new AttractionDTO();
        dto.setName("测试景点3");
        dto.setCity("广州");
        dto.setPrice(BigDecimal.valueOf(80.00));
        dto.setRating(BigDecimal.valueOf(4.6));

        Attraction created = attractionService.create(dto);

        AttractionDTO updateDto = new AttractionDTO();
        updateDto.setName("测试景点3-修改");
        updateDto.setCity("深圳");
        updateDto.setPrice(BigDecimal.valueOf(90.00));
        updateDto.setRating(BigDecimal.valueOf(4.7));

        Attraction updated = attractionService.update(created.getId(), updateDto);

        assertNotNull(updated);
        assertEquals("测试景点3-修改", updated.getName());
        assertEquals("深圳", updated.getCity());
        assertEquals(BigDecimal.valueOf(90.00), updated.getPrice());
    }

    @Test
    void testDeleteAttraction() {
        AttractionDTO dto = new AttractionDTO();
        dto.setName("测试景点4");
        dto.setCity("成都");
        dto.setPrice(BigDecimal.valueOf(60.00));
        dto.setRating(BigDecimal.valueOf(4.5));

        Attraction created = attractionService.create(dto);
        Long id = created.getId();

        attractionService.delete(id);

        assertNull(attractionService.getById(id));
    }

    @Test
    void testGetAllCities() {
        redisTemplate.delete("attraction:cities");

        AttractionDTO dto1 = new AttractionDTO();
        dto1.setName("景点A");
        dto1.setCity("北京");
        attractionService.create(dto1);

        AttractionDTO dto2 = new AttractionDTO();
        dto2.setName("景点B");
        dto2.setCity("上海");
        attractionService.create(dto2);

        List<String> cities = attractionService.getAllCities();

        assertNotNull(cities);
        assertTrue(cities.size() >= 2);
    }

    @Test
    void testGetHotAttractions() {
        redisTemplate.delete("attraction:hot");

        for (int i = 0; i < 5; i++) {
            AttractionDTO dto = new AttractionDTO();
            dto.setName("热门景点" + i);
            dto.setCity("杭州");
            dto.setPrice(BigDecimal.valueOf(100 + i));
            dto.setRating(BigDecimal.valueOf(4.0 + i * 0.2));
            attractionService.create(dto);
        }

        List<Attraction> hotAttractions = attractionService.getHotAttractions(3);

        assertNotNull(hotAttractions);
        assertEquals(3, hotAttractions.size());
    }

    @Test
    void testRedisCache() {
        AttractionDTO dto = new AttractionDTO();
        dto.setName("缓存测试景点");
        dto.setCity("南京");
        dto.setPrice(BigDecimal.valueOf(70.00));
        dto.setRating(BigDecimal.valueOf(4.5));

        attractionService.create(dto);

        attractionService.getAllCities();
        Boolean exists = redisTemplate.hasKey("attraction:cities");
        assertTrue(exists);

        redisTemplate.delete("attraction:cities");
        exists = redisTemplate.hasKey("attraction:cities");
        assertFalse(exists);

        attractionService.getAllCities();
        exists = redisTemplate.hasKey("attraction:cities");
        assertTrue(exists);
    }
}