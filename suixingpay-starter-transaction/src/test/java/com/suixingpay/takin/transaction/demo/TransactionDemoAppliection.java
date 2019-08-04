package com.suixingpay.takin.transaction.demo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.suixingpay.takin.mybatis.dynamicdatasource.autoconfigure.DynamicDataSourceInterceptorAutoConfiguration;

/**
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2017年10月31日 下午1:34:18
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2017年10月31日 下午1:34:18
 */
@SpringBootApplication(exclude=DynamicDataSourceInterceptorAutoConfiguration.class)
// @EnableTransactionManagement
@MapperScan("com.suixingpay.takin.transaction.demo.mapper")
public class TransactionDemoAppliection {

    public static void main(String[] args) {
        SpringApplication.run(TransactionDemoAppliection.class, args);
    }

}
