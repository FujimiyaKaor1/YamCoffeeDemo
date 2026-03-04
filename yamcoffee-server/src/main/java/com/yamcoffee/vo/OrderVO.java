package com.yamcoffee.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderVO {
    
    private Long id;
    
    private String orderNo;
    
    private Integer status;
    
    private String statusText;
    
    private BigDecimal payAmount;
    
    private Integer itemCount;
    
    private String firstProductImage;
    
    private LocalDateTime createTime;
    
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
}
