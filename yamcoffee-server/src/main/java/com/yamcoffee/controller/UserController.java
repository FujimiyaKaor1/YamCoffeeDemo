package com.yamcoffee.controller;

import com.yamcoffee.common.Constants;
import com.yamcoffee.common.Result;
import com.yamcoffee.dto.UserLoginDTO;
import com.yamcoffee.service.UserService;
import com.yamcoffee.vo.LoginVO;
import com.yamcoffee.vo.UserVO;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {
    
    private final UserService userService;
    
    public UserController(UserService userService) {
        this.userService = userService;
    }
    
    @PostMapping("/login")
    public Result<LoginVO> login(@Valid @RequestBody UserLoginDTO dto) {
        LoginVO loginVO = userService.login(dto);
        return Result.success(loginVO);
    }
    
    @GetMapping("/info")
    public Result<UserVO> getUserInfo(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute(Constants.USER_ID_KEY);
        UserVO userVO = userService.getUserInfo(userId);
        return Result.success(userVO);
    }
    
    @PutMapping("/info")
    public Result<UserVO> updateUserInfo(HttpServletRequest request, @RequestBody UserVO userVO) {
        Long userId = (Long) request.getAttribute(Constants.USER_ID_KEY);
        UserVO result = userService.updateUserInfo(userId, userVO);
        return Result.success(result);
    }
}
