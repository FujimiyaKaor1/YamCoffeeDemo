package com.yamcoffee.interceptor;

import com.yamcoffee.common.Constants;
import com.yamcoffee.common.ResultCode;
import com.yamcoffee.exception.BusinessException;
import com.yamcoffee.utils.JwtUtil;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class AuthInterceptor implements HandlerInterceptor {
    
    private final JwtUtil jwtUtil;
    
    public AuthInterceptor(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }
        
        String token = request.getHeader(Constants.TOKEN_HEADER);
        if (!StringUtils.hasText(token)) {
            throw new BusinessException(ResultCode.USER_NOT_LOGIN);
        }
        
        if (token.startsWith(Constants.TOKEN_PREFIX)) {
            token = token.substring(Constants.TOKEN_PREFIX.length());
        }
        
        if (!jwtUtil.validateToken(token)) {
            throw new BusinessException(ResultCode.USER_NOT_LOGIN);
        }
        
        Long userId = jwtUtil.getUserIdFromToken(token);
        request.setAttribute(Constants.USER_ID_KEY, userId);
        return true;
    }
}
