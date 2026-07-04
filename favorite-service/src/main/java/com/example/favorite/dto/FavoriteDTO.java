package com.example.favorite.dto;

import lombok.Data;

@Data
public class FavoriteDTO {

    private Long userId;

    private Long targetId;

    private String targetType;
}