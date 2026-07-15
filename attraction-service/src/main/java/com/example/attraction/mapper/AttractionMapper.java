package com.example.attraction.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.attraction.entity.Attraction;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AttractionMapper extends BaseMapper<Attraction> {
}