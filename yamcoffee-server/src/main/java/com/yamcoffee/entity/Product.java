package com.yamcoffee.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("t_product")
public class Product implements Serializable {
    
    @TableId(type = IdType.AUTO)
    private Long id;
    
    @TableField("category_id")
    private Long categoryId;
    
    private String name;
    
    private String subtitle;
    
    @TableField("main_image")
    private String mainImage;
    
    @TableField("sub_images")
    private String subImages;
    
    private String detail;
    
    private BigDecimal price;
    
    @TableField("original_price")
    private BigDecimal originalPrice;
    
    private Integer stock;
    
    private Integer sales;
    
    private String unit;
    
    @TableField("sort_order")
    private Integer sortOrder;
    
    private Integer status;
    
    @TableField("is_hot")
    private Integer isHot;
    
    @TableField("is_new")
    private Integer isNew;
    
    @TableField("is_recommend")
    private Integer isRecommend;
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
