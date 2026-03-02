package com.yamcoffee.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WxConfig {
    
    @Value("${wx.appid}")
    private String appid;
    
    @Value("${wx.secret}")
    private String secret;
    
    public String getAppid() {
        return appid;
    }
    
    public String getSecret() {
        return secret;
    }
}
