package com.example.route.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("route_day")
public class RouteDay {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long routeId;
    private Integer dayNo;
    private String title;
    private String scheduleDetail;
    private String attractionIds;
    private Long hotelId;
}