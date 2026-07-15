package com.example.route.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.route.entity.RouteDay;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface RouteDayMapper extends BaseMapper<RouteDay> {

    @Select("SELECT * FROM route_day WHERE route_id = #{routeId} ORDER BY day_no")
    List<RouteDay> findByRouteId(@Param("routeId") Long routeId);
}