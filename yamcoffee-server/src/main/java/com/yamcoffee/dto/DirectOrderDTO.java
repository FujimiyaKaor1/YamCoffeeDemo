package com.yamcoffee.dto;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
public class DirectOrderDTO {
    
    @NotNull(message = "地址ID不能为空")
    private Long addressId;
    
    @NotNull(message = "商品ID不能为空")
    private Long productId;
    
    @NotNull(message = "数量不能为空")
    @Min(value = 1, message = "数量最小为1")
    private Integer quantity;
    
    private String userNote;
}
