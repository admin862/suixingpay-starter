package com.suixingpay.takin.cache.demo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * TODO
 * 
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2017年10月31日 下午1:34:18
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2017年10月31日 下午1:34:18
 */
@SpringBootApplication
@MapperScan("com.suixingpay.takin.cache.demo.mapper")
public class CacheDemoAppliection {

    public static void main(String[] args) {
         SpringApplication.run(CacheDemoAppliection.class, args);
    }

}
