package com.yamcoffee.vo;

import lombok.Data;

@Data
public class OrderCountVO {
    
    private Integer pendingPayment;
    
    private Integer pendingShipment;
    
    private Integer pendingReceipt;
    
    private Integer completed;
    
    private Integer cancelled;
}
