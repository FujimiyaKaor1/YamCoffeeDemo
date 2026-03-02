package com.yamcoffee.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yamcoffee.entity.Category;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CategoryMapper extends BaseMapper<Category> {
}
