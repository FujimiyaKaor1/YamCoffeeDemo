package com.yamcoffee.dto;

import lombok.Data;

import javax.validation.constraints.Min;

@Data
public class CartUpdateDTO {
    
    @Min(value = 1, message = "数量必须大于0")
    private Integer quantity;
    
    private Integer selected;
}
