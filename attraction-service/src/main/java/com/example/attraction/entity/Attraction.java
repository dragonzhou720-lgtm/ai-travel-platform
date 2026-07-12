package com.example.attraction.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("attraction")
public class Attraction {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("name")
    private String name;

    @TableField("city")
    private String city;

    @TableField("address")
    private String address;

    @TableField("ticket_price")
    private BigDecimal ticketPrice;

    @TableField("rating")
    private BigDecimal rating;

    @TableField("description")
    private String description;

    @TableField("open_time")
    private String openTime;

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