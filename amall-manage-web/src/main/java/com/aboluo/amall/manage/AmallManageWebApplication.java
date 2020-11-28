package com.aboluo.amall.manage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
public class AmallManageWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(AmallManageWebApplication.class, args);
    }

}
