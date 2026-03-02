package com.yamcoffee.common;

public class Constants {
    
    private Constants() {
    }
    
    public static final String TOKEN_HEADER = "Authorization";
    public static final String TOKEN_PREFIX = "Bearer ";
    
    public static final String USER_ID_KEY = "userId";
    
    public static final Integer DEFAULT_PAGE_NUM = 1;
    public static final Integer DEFAULT_PAGE_SIZE = 10;
    public static final Integer MAX_PAGE_SIZE = 100;
    
    public static final Integer ORDER_STATUS_PENDING_PAYMENT = 0;
    public static final Integer ORDER_STATUS_PENDING_SHIPMENT = 1;
    public static final Integer ORDER_STATUS_PENDING_RECEIPT = 2;
    public static final Integer ORDER_STATUS_COMPLETED = 3;
    public static final Integer ORDER_STATUS_CANCELLED = 4;
    
    public static final Integer PAY_STATUS_UNPAID = 0;
    public static final Integer PAY_STATUS_PAID = 1;
    public static final Integer PAY_STATUS_REFUNDED = 2;
    
    public static final Integer ADDRESS_MAX_COUNT = 20;
}
