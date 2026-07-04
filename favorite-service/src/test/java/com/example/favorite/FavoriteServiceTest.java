package com.example.favorite;

import com.example.favorite.dto.FavoriteDTO;
import com.example.favorite.entity.Favorite;
import com.example.favorite.service.FavoriteService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FavoriteServiceTest {

    @Autowired
    private FavoriteService favoriteService;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Test
    void testAddFavorite() {
        FavoriteDTO dto = new FavoriteDTO();
        dto.setUserId(1L);
        dto.setTargetId(100L);
        dto.setTargetType("ATTRACTION");

        Favorite favorite = favoriteService.add(dto);

        assertNotNull(favorite);
        assertEquals(1L, favorite.getUserId());
        assertEquals(100L, favorite.getTargetId());
        assertEquals("ATTRACTION", favorite.getTargetType());
    }

    @Test
    void testAddDuplicateFavorite() {
        FavoriteDTO dto = new FavoriteDTO();
        dto.setUserId(1L);
        dto.setTargetId(200L);
        dto.setTargetType("ATTRACTION");

        Favorite favorite1 = favoriteService.add(dto);
        assertNotNull(favorite1);

        Favorite favorite2 = favoriteService.add(dto);
        assertNull(favorite2);
    }

    @Test
    void testRemoveFavorite() {
        FavoriteDTO dto = new FavoriteDTO();
        dto.setUserId(2L);
        dto.setTargetId(300L);
        dto.setTargetType("ROUTE");

        favoriteService.add(dto);
        assertTrue(favoriteService.isFavorite(2L, 300L, "ROUTE"));

        favoriteService.remove(2L, 300L, "ROUTE");
        assertFalse(favoriteService.isFavorite(2L, 300L, "ROUTE"));
    }

    @Test
    void testGetFavorites() {
        redisTemplate.delete("favorite:user:3");

        FavoriteDTO dto1 = new FavoriteDTO();
        dto1.setUserId(3L);
        dto1.setTargetId(400L);
        dto1.setTargetType("ATTRACTION");
        favoriteService.add(dto1);

        FavoriteDTO dto2 = new FavoriteDTO();
        dto2.setUserId(3L);
        dto2.setTargetId(401L);
        dto2.setTargetType("ATTRACTION");
        favoriteService.add(dto2);

        FavoriteDTO dto3 = new FavoriteDTO();
        dto3.setUserId(3L);
        dto3.setTargetId(500L);
        dto3.setTargetType("ROUTE");
        favoriteService.add(dto3);

        List<Favorite> favorites = favoriteService.getFavorites(3L);

        assertNotNull(favorites);
        assertEquals(3, favorites.size());
    }

    @Test
    void testGetFavoritesByType() {
        redisTemplate.delete("favorite:user:4");

        FavoriteDTO dto1 = new FavoriteDTO();
        dto1.setUserId(4L);
        dto1.setTargetId(600L);
        dto1.setTargetType("ATTRACTION");
        favoriteService.add(dto1);

        FavoriteDTO dto2 = new FavoriteDTO();
        dto2.setUserId(4L);
        dto2.setTargetId(601L);
        dto2.setTargetType("ATTRACTION");
        favoriteService.add(dto2);

        FavoriteDTO dto3 = new FavoriteDTO();
        dto3.setUserId(4L);
        dto3.setTargetId(700L);
        dto3.setTargetType("ROUTE");
        favoriteService.add(dto3);

        List<Favorite> attractions = favoriteService.getFavoritesByType(4L, "ATTRACTION");
        List<Favorite> routes = favoriteService.getFavoritesByType(4L, "ROUTE");

        assertNotNull(attractions);
        assertNotNull(routes);
        assertEquals(2, attractions.size());
        assertEquals(1, routes.size());
    }

    @Test
    void testIsFavorite() {
        FavoriteDTO dto = new FavoriteDTO();
        dto.setUserId(5L);
        dto.setTargetId(800L);
        dto.setTargetType("ATTRACTION");

        assertFalse(favoriteService.isFavorite(5L, 800L, "ATTRACTION"));

        favoriteService.add(dto);

        assertTrue(favoriteService.isFavorite(5L, 800L, "ATTRACTION"));
        assertFalse(favoriteService.isFavorite(5L, 801L, "ATTRACTION"));
        assertFalse(favoriteService.isFavorite(5L, 800L, "ROUTE"));
    }

    @Test
    void testRedisCache() {
        FavoriteDTO dto = new FavoriteDTO();
        dto.setUserId(6L);
        dto.setTargetId(900L);
        dto.setTargetType("ATTRACTION");

        favoriteService.add(dto);

        favoriteService.getFavorites(6L);
        Boolean exists = redisTemplate.hasKey("favorite:user:6");
        assertTrue(exists);

        redisTemplate.delete("favorite:user:6");
        exists = redisTemplate.hasKey("favorite:user:6");
        assertFalse(exists);

        favoriteService.getFavorites(6L);
        exists = redisTemplate.hasKey("favorite:user:6");
        assertTrue(exists);
    }
}