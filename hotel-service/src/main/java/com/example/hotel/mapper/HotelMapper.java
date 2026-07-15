package com.example.hotel.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.hotel.entity.Hotel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface HotelMapper extends BaseMapper<Hotel> {

    @Select("SELECT DISTINCT city FROM hotel ORDER BY city")
    List<String> findAllCities();

    @Select("SELECT * FROM hotel ORDER BY rating DESC LIMIT #{limit}")
    List<Hotel> findHotHotels(@Param("limit") Integer limit);

    @Select("SELECT * FROM hotel WHERE city = #{city} AND status = 1")
    List<Hotel> findByCity(@Param("city") String city);

    @Select("SELECT * FROM hotel WHERE city = #{city} AND status = 1 AND (name LIKE CONCAT('%', #{keyword}, '%') OR description LIKE CONCAT('%', #{keyword}, '%'))")
    List<Hotel> findByCityAndKeyword(@Param("city") String city, @Param("keyword") String keyword);
}