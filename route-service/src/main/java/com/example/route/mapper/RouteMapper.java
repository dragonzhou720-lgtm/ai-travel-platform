package com.example.route.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.route.entity.Route;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface RouteMapper extends BaseMapper<Route> {

    @Select("SELECT * FROM route WHERE status = 1 ORDER BY created_at DESC")
    List<Route> findAllRoutes();

    @Select("SELECT * FROM route WHERE destination = #{city} AND status = 1 ORDER BY created_at DESC")
    List<Route> findByDestination(@Param("city") String city);

    @Select("SELECT r.* FROM route r JOIN route_day rd ON r.id = rd.route_id WHERE rd.attraction_ids LIKE CONCAT('%', #{attractionId}, '%') AND r.status = 1 ORDER BY r.created_at DESC")
    List<Route> findRoutesContainingAttraction(@Param("attractionId") Long attractionId);
}