package com.yamcoffee.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ThreadLocalRandom;

public class OrderNoGenerator {
    
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");
    
    private OrderNoGenerator() {
    }
    
    public static String generate() {
        String timestamp = LocalDateTime.now().format(FORMATTER);
        String random = String.format("%04d", ThreadLocalRandom.current().nextInt(10000));
        return "YC" + timestamp + random;
    }
}
