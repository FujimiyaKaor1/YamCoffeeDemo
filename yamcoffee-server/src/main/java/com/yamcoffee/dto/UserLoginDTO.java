package com.yamcoffee.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class UserLoginDTO {
    
    @NotBlank(message = "登录code不能为空")
    private String code;
}
