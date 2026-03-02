package com.yamcoffee.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class OrderCreateDTO {
    
    @NotNull(message = "地址ID不能为空")
    private Long addressId;
    
    @NotEmpty(message = "请选择要购买的商品")
    private List<Long> cartItemIds;
    
    private String userNote;
}
