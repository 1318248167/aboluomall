package com.aboluo.amall.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@MapperScan(basePackages = "com.aboluo.amall.user.mapper")
public class AmallUserServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AmallUserServiceApplication.class, args);
    }

}
