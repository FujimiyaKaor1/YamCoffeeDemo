package com.yamcoffee.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yamcoffee.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
