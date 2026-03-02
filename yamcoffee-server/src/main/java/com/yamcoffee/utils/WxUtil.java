package com.yamcoffee.utils;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class WxUtil {
    
    private static final String JSCODE2SESSION_URL = "https://api.weixin.qq.com/sns/jscode2session?appid={appid}&secret={secret}&js_code={code}&grant_type=authorization_code";
    
    @Value("${wx.appid}")
    private String appid;
    
    @Value("${wx.secret}")
    private String secret;
    
    public WxSessionResult code2Session(String code) {
        String url = JSCODE2SESSION_URL
                .replace("{appid}", appid)
                .replace("{secret}", secret)
                .replace("{code}", code);
        
        try {
            String response = HttpUtil.get(url);
            log.info("WeChat code2Session response: {}", response);
            
            JSONObject json = JSONUtil.parseObj(response);
            Integer errcode = json.getInt("errcode");
            
            if (errcode != null && errcode != 0) {
                log.error("WeChat code2Session failed: errcode={}, errmsg={}", errcode, json.getStr("errmsg"));
                return null;
            }
            
            WxSessionResult result = new WxSessionResult();
            result.setOpenid(json.getStr("openid"));
            result.setSessionKey(json.getStr("session_key"));
            result.setUnionid(json.getStr("unionid"));
            return result;
        } catch (Exception e) {
            log.error("WeChat code2Session error: ", e);
            return null;
        }
    }
    
    public static class WxSessionResult {
        private String openid;
        private String sessionKey;
        private String unionid;
        
        public String getOpenid() {
            return openid;
        }
        
        public void setOpenid(String openid) {
            this.openid = openid;
        }
        
        public String getSessionKey() {
            return sessionKey;
        }
        
        public void setSessionKey(String sessionKey) {
            this.sessionKey = sessionKey;
        }
        
        public String getUnionid() {
            return unionid;
        }
        
        public void setUnionid(String unionid) {
            this.unionid = unionid;
        }
    }
}
