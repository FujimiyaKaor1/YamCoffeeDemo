package com.yamcoffee.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@TableName("t_payment_config")
public class PaymentConfig implements Serializable {
    
    @TableId(type = IdType.AUTO)
    private Long id;
    
    @TableField("qr_code_url")
    private String qrCodeUrl;
    
    @TableField("wechat_qr_code_url")
    private String wechatQrCodeUrl;
    
    @TableField("alipay_qr_code_url")
    private String alipayQrCodeUrl;
    
    private String description;
    
    private Integer status;
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
