package com.yamcoffee.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductVO {
    
    private Long id;
    
    private Long categoryId;
    
    private String name;
    
    private String subtitle;
    
    private String mainImage;
    
    private BigDecimal price;
    
    private BigDecimal originalPrice;
    
    private Integer stock;
    
    private Integer sales;
    
    private String unit;
    
    private Integer isHot;
    
    private Integer isNew;
    
    private Integer isRecommend;
}
