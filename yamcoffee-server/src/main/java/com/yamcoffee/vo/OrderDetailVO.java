package com.yamcoffee.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderDetailVO {
    
    private Long id;
    
    private String orderNo;
    
    private Integer status;
    
    private String statusText;
    
    private BigDecimal totalAmount;
    
    private BigDecimal freightAmount;
    
    private BigDecimal discountAmount;
    
    private BigDecimal payAmount;
    
    private String receiverName;
    
    private String receiverPhone;
    
    private String receiverProvince;
    
    private String receiverCity;
    
    private String receiverDistrict;
    
    private String receiverAddress;
    
    private String fullAddress;
    
    private LocalDateTime createTime;
    
    private LocalDateTime payTime;
    
    private LocalDateTime deliveryTime;
    
    private LocalDateTime receiveTime;
    
    private String deliveryCompany;
    
    private String deliveryNo;
    
    private String userNote;
    
    private List<OrderItemVO> items;
    
    public String getStatusText() {
        if (status == null) {
            return "";
        }
        switch (status) {
            case 0:
                return "待付款";
            case 1:
                return "待发货";
            case 2:
                return "待收货";
            case 3:
                return "已完成";
            case 4:
                return "已取消";
            case 5:
                return "已支付待确认";
            default:
                return "未知状态";
        }
    }
    
    public String getFullAddress() {
        return receiverProvince + receiverCity + receiverDistrict + receiverAddress;
    }
}
