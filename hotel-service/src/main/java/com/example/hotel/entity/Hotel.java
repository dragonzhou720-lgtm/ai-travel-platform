package com.example.hotel.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("hotel")
public class Hotel {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("name")
    private String name;

    @TableField("city")
    private String city;

    @TableField("address")
    private String address;

    @TableField("price_per_night")
    private BigDecimal pricePerNight;

    @TableField("rating")
    private BigDecimal rating;

    @TableField("star_level")
    private Integer starLevel;

    @TableField("description")
    private String description;

    @TableField("tags")
    private String tags;

    @TableField("cover_image")
    private String coverImage;

    @TableField("status")
    private Integer status;

    @TableField("created_at")
    private LocalDateTime createdAt;

    @TableField("updated_at")
    private LocalDateTime updatedAt;
}