package com.yamcoffee.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CartVO {
    
    private Long id;
    
    private Long productId;
    
    private String productName;
    
    private String productImage;
    
    private BigDecimal productPrice;
    
    private Integer quantity;
    
    private Integer selected;
    
    private Integer stock;
    
    private BigDecimal subtotal;
}
