package com.example.favorite.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.favorite.entity.Favorite;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface FavoriteMapper extends BaseMapper<Favorite> {

    @Select("SELECT * FROM favorite WHERE user_id = #{userId} ORDER BY created_at DESC")
    List<Favorite> findByUserId(@Param("userId") Long userId);

    @Select("SELECT * FROM favorite WHERE user_id = #{userId} AND target_type = #{targetType} ORDER BY created_at DESC")
    List<Favorite> findByUserIdAndType(@Param("userId") Long userId, @Param("targetType") String targetType);

    @Select("SELECT COUNT(*) FROM favorite WHERE user_id = #{userId} AND target_id = #{targetId} AND target_type = #{targetType}")
    int countByUserAndTarget(@Param("userId") Long userId, @Param("targetId") Long targetId, @Param("targetType") String targetType);

    @Delete("DELETE FROM favorite WHERE user_id = #{userId} AND target_id = #{targetId} AND target_type = #{targetType}")
    void deleteByUserAndTarget(@Param("userId") Long userId, @Param("targetId") Long targetId, @Param("targetType") String targetType);
}