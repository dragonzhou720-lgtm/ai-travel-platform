package com.example.attraction.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.attraction.entity.Attraction;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface AttractionMapper extends BaseMapper<Attraction> {

    @Select("SELECT DISTINCT city FROM attraction ORDER BY city")
    List<String> findAllCities();

    @Select("SELECT * FROM attraction ORDER BY rating DESC LIMIT #{limit}")
    List<Attraction> findHotAttractions(@Param("limit") Integer limit);
}