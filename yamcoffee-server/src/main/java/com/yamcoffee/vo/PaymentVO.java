package com.yamcoffee.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class PaymentVO implements Serializable {
    
    private String qrCodeUrl;
    
    private String wechatQrCodeUrl;
    
    private String alipayQrCodeUrl;
    
    private String description;
}
