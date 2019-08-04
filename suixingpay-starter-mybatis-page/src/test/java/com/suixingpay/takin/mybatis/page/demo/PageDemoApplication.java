package com.suixingpay.takin.mybatis.page.demo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.suixingpay.takin.mybatis.page.demo.mapper") // mybatis扫描
public class PageDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(PageDemoApplication.class, args);
    }
}
