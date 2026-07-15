package com.example.favorite.controller;

import com.example.favorite.dto.FavoriteDTO;
import com.example.favorite.entity.Favorite;
import com.example.favorite.service.FavoriteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/favorites")
public class FavoriteController {

    private final FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @PostMapping
    public ResponseEntity<Favorite> add(@RequestBody FavoriteDTO dto) {
        Favorite favorite = favoriteService.add(dto);
        if (favorite == null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(favorite);
    }

    @DeleteMapping
    public ResponseEntity<Void> remove(
            @RequestParam Long userId,
            @RequestParam Long targetId,
            @RequestParam String targetType) {
        favoriteService.remove(userId, targetId, targetType);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Favorite>> getFavorites(@PathVariable Long userId) {
        List<Favorite> favorites = favoriteService.getFavorites(userId);
        return ResponseEntity.ok(favorites);
    }

    @GetMapping("/user/{userId}/type/{targetType}")
    public ResponseEntity<List<Favorite>> getFavoritesByType(
            @PathVariable Long userId,
            @PathVariable String targetType) {
        List<Favorite> favorites = favoriteService.getFavoritesByType(userId, targetType);
        return ResponseEntity.ok(favorites);
    }

    @GetMapping("/check")
    public ResponseEntity<Map<String, Boolean>> check(
            @RequestParam Long userId,
            @RequestParam Long targetId,
            @RequestParam String targetType) {
        boolean isFavorite = favoriteService.isFavorite(userId, targetId, targetType);
        Map<String, Boolean> result = new HashMap<>();
        result.put("isFavorite", isFavorite);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> health() {
        return ResponseEntity.ok(Map.of("status", "UP", "service", "favorite-service"));
    }
}