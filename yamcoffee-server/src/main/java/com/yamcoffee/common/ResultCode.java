package com.yamcoffee.common;

import lombok.Getter;

@Getter
public enum ResultCode {
    
    SUCCESS(200, "操作成功"),
    ERROR(500, "操作失败"),
    
    PARAM_ERROR(400, "参数错误"),
    UNAUTHORIZED(401, "未授权"),
    FORBIDDEN(403, "禁止访问"),
    NOT_FOUND(404, "资源不存在"),
    
    USER_NOT_LOGIN(1001, "用户未登录"),
    USER_NOT_EXIST(1002, "用户不存在"),
    USER_DISABLED(1003, "用户已被禁用"),
    LOGIN_FAILED(1004, "登录失败"),
    
    PRODUCT_NOT_EXIST(2001, "商品不存在"),
    PRODUCT_OFF_SHELF(2002, "商品已下架"),
    PRODUCT_STOCK_NOT_ENOUGH(2003, "商品库存不足"),
    
    CART_EMPTY(3001, "购物车为空"),
    CART_ITEM_NOT_EXIST(3002, "购物车商品不存在"),
    
    ORDER_NOT_EXIST(4001, "订单不存在"),
    ORDER_STATUS_ERROR(4002, "订单状态错误"),
    ORDER_ALREADY_PAID(4003, "订单已支付"),
    ORDER_ALREADY_CANCELLED(4004, "订单已取消"),
    
    ADDRESS_NOT_EXIST(5001, "地址不存在"),
    ADDRESS_LIMIT_EXCEEDED(5002, "地址数量超过限制");
    
    private final Integer code;
    private final String message;
    
    ResultCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
