package com.aboluo.amall;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@MapperScan(basePackages = "com.aboluo.amall.cart.mapper")
public class AmallCartServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AmallCartServiceApplication.class, args);
    }

}
