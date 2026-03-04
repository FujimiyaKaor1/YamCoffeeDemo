package com.yamcoffee.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("t_order")
public class Order implements Serializable {
    
    @TableId(type = IdType.AUTO)
    private Long id;
    
    @TableField("order_no")
    private String orderNo;
    
    @TableField("user_id")
    private Long userId;
    
    @TableField("receiver_name")
    private String receiverName;
    
    @TableField("receiver_phone")
    private String receiverPhone;
    
    @TableField("receiver_province")
    private String receiverProvince;
    
    @TableField("receiver_city")
    private String receiverCity;
    
    @TableField("receiver_district")
    private String receiverDistrict;
    
    @TableField("receiver_address")
    private String receiverAddress;
    
    @TableField("total_amount")
    private BigDecimal totalAmount;
    
    @TableField("freight_amount")
    private BigDecimal freightAmount;
    
    @TableField("discount_amount")
    private BigDecimal discountAmount;
    
    @TableField("pay_amount")
    private BigDecimal payAmount;
    
    private Integer status;
    
    @TableField("pay_status")
    private Integer payStatus;
    
    @TableField("pay_type")
    private Integer payType;
    
    @TableField("pay_time")
    private LocalDateTime payTime;
    
    @TableField("user_pay_time")
    private LocalDateTime userPayTime;
    
    @TableField("confirm_pay_time")
    private LocalDateTime confirmPayTime;
    
    @TableField("delivery_type")
    private Integer deliveryType;
    
    @TableField("delivery_company")
    private String deliveryCompany;
    
    @TableField("delivery_no")
    private String deliveryNo;
    
    @TableField("delivery_time")
    private LocalDateTime deliveryTime;
    
    @TableField("receive_time")
    private LocalDateTime receiveTime;
    
    @TableField("estimated_arrival_time")
    private LocalDateTime estimatedArrivalTime;
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    
    @TableField("cancel_time")
    private LocalDateTime cancelTime;
    
    @TableField("cancel_reason")
    private String cancelReason;
    
    @TableField("user_note")
    private String userNote;
}
