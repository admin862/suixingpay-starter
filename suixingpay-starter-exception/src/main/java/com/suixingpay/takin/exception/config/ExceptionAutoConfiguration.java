/**  
 * All rights Reserved, Designed By Suixingpay.
 * @author: jiayu.qiu[qiu_jy@suixingpay.com] 
 * @date: 2017年7月19日 下午10:58:32   
 * @Copyright ©2017 Suixingpay. All rights reserved. 
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.takin.exception.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.suixingpay.takin.exception.handler.GlobalExceptionHandler;
import com.suixingpay.takin.exception.handler.SuixingPayExceptionHandler;

/**
 * token 自动配置
 * 
 * @author: jiayu.qiu[qiu_jy@suixingpay.com]
 * @date: 2017年7月19日 下午10:58:32
 * @version: V1.0
 * @review: jiayu.qiu[qiu_jy@suixingpay.com]/2017年7月19日 下午10:58:32
 */
@Configuration
@ConditionalOnWebApplication
@EnableConfigurationProperties({ ExceptionProperties.class })
public class ExceptionAutoConfiguration {
    @Autowired
    private ExceptionProperties properties;

    @Bean
    public SuixingPayExceptionHandler sxfExceptionHandler() {
        return new SuixingPayExceptionHandler(properties);
    }

    @Bean
    @ConditionalOnProperty(value = ExceptionProperties.PREFIX + ".enabled", matchIfMissing = true)
    public GlobalExceptionHandler sxfGlobalExceptionHandler(SuixingPayExceptionHandler sxfExceptionHandler) {
        return new GlobalExceptionHandler(sxfExceptionHandler);
    }
}
