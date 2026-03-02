package com.yamcoffee;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.yamcoffee.mapper")
public class YamcoffeeApplication {

    public static void main(String[] args) {
        SpringApplication.run(YamcoffeeApplication.class, args);
    }
}
