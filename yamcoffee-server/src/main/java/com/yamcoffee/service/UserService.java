package com.yamcoffee.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yamcoffee.common.ResultCode;
import com.yamcoffee.dto.UserLoginDTO;
import com.yamcoffee.entity.User;
import com.yamcoffee.exception.BusinessException;
import com.yamcoffee.mapper.UserMapper;
import com.yamcoffee.utils.JwtUtil;
import com.yamcoffee.utils.WxUtil;
import com.yamcoffee.vo.LoginVO;
import com.yamcoffee.vo.UserVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class UserService {
    
    private final UserMapper userMapper;
    private final JwtUtil jwtUtil;
    private final WxUtil wxUtil;
    
    public UserService(UserMapper userMapper, JwtUtil jwtUtil, WxUtil wxUtil) {
        this.userMapper = userMapper;
        this.jwtUtil = jwtUtil;
        this.wxUtil = wxUtil;
    }
    
    @Transactional
    public LoginVO login(UserLoginDTO dto) {
        WxUtil.WxSessionResult session = wxUtil.code2Session(dto.getCode());
        if (session == null || session.getOpenid() == null) {
            throw new BusinessException(ResultCode.LOGIN_FAILED, "微信登录失败");
        }
        
        String openid = session.getOpenid();
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getOpenid, openid);
        User user = userMapper.selectOne(wrapper);
        
        boolean isNewUser = false;
        if (user == null) {
            user = new User();
            user.setOpenid(openid);
            user.setUnionId(session.getUnionid());
            user.setNickname("微信用户");
            user.setStatus(1);
            userMapper.insert(user);
            isNewUser = true;
        }
        
        if (user.getStatus() == 0) {
            throw new BusinessException(ResultCode.USER_DISABLED);
        }
        
        user.setLastLoginTime(LocalDateTime.now());
        userMapper.updateById(user);
        
        String token = jwtUtil.generateToken(user.getId());
        
        LoginVO loginVO = new LoginVO();
        loginVO.setToken(token);
        loginVO.setIsNewUser(isNewUser);
        
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        loginVO.setUserInfo(userVO);
        
        return loginVO;
    }
    
    public UserVO getUserInfo(Long userId) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException(ResultCode.USER_NOT_EXIST);
        }
        
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        return userVO;
    }
    
    public UserVO updateUserInfo(Long userId, UserVO userVO) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException(ResultCode.USER_NOT_EXIST);
        }
        
        if (userVO.getNickname() != null) {
            user.setNickname(userVO.getNickname());
        }
        if (userVO.getAvatarUrl() != null) {
            user.setAvatarUrl(userVO.getAvatarUrl());
        }
        if (userVO.getPhone() != null) {
            user.setPhone(userVO.getPhone());
        }
        if (userVO.getGender() != null) {
            user.setGender(userVO.getGender());
        }
        
        userMapper.updateById(user);
        
        UserVO result = new UserVO();
        BeanUtils.copyProperties(user, result);
        return result;
    }
}
