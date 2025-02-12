package com.tiktok.mapper;

import com.tiktok.entity.Category;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CategoryMapper {

    /**
     * 查询获得分类列表
     * @return
     */
    List<Category> list();
}
