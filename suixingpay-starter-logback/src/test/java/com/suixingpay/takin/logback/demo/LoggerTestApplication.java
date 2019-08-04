/**  
 * All rights Reserved, Designed By Suixingpay.
 * @author: qiujiayu[qiu_jy@suixingpay.com] 
 * @date: 2017年9月18日 下午12:04:21   
 * @Copyright ©2017 Suixingpay. All rights reserved. 
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.takin.logback.demo;

import java.util.Arrays;
import java.util.Optional;

import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.core.util.StatusPrinter;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;

/**
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2017年9月18日 下午12:04:21
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2017年9月18日 下午12:04:21
 */
@Slf4j
@SpringBootApplication
@RestController
public class LoggerTestApplication {
    public static void main(String[] args) throws Exception {
        SpringApplication app = new SpringApplication(LoggerTestApplication.class);
        ApplicationContext context = app.run(args);
        // print internal state
        LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
        StatusPrinter.print(lc);
        Environment env = context.getBean(Environment.class);
        MDC.put("traceId", "" + System.currentTimeMillis());
        log.info("application started!");
        String[] profiles = env.getActiveProfiles();
        System.out.println("active profiles--->" + Arrays.toString(profiles));
        log.info("active profiles--->" + Arrays.toString(profiles));
        for (int i = 0; i < 100; i++) {
            log.info("test {}", i);
            Thread.sleep(1);
        }
        log.info("13613175443");
    }
    
    @GetMapping
    public Optional<String> test() {
        log.info("test");
        log.error("test");
        return Optional.of("test");
    }
}
