package com.yamcoffee.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("t_order_item")
public class OrderItem implements Serializable {
    
    @TableId(type = IdType.AUTO)
    private Long id;
    
    @TableField("order_id")
    private Long orderId;
    
    @TableField("order_no")
    private String orderNo;
    
    @TableField("product_id")
    private Long productId;
    
    @TableField("product_name")
    private String productName;
    
    @TableField("product_image")
    private String productImage;
    
    @TableField("product_price")
    private BigDecimal productPrice;
    
    private Integer quantity;
    
    @TableField("total_amount")
    private BigDecimal totalAmount;
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
