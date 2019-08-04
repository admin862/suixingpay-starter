/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: tangqihua[tang_qh@suixingpay.com]
 * @date: 2018年03月27日 17时16分
 * @Copyright ©2018 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.takin.rabbitmq.autoconfigure;

import java.util.Arrays;
import java.util.Collection;

import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import com.suixingpay.takin.manager.ManagerFilterUrlPatternsRegistrationBean;
import com.suixingpay.takin.manager.ManagerUrlRegistrationBean;
import com.suixingpay.takin.manager.autoconfigure.ManagerAutoConfiguration;

import lombok.extern.slf4j.Slf4j;

/**
 * @author: tangqihua[tang_qh@suixingpay.com]
 * @date: 2018年03月27日 17时16分
 * @version: V1.0
 * @review: tangqihua[tang_qh@suixingpay.com]/2018年03月27日 17时16分
 */
@Slf4j
@Configuration
@ConditionalOnWebApplication
@AutoConfigureBefore(ManagerAutoConfiguration.class)
@ConditionalOnClass(ManagerUrlRegistrationBean.class)
@ConditionalOnProperty(value = SxfRabbitMQManagerConfiguration.PREFIX + ".enabled", matchIfMissing = true)
public class SxfRabbitMQManagerConfiguration {
    public static final String PREFIX = "suixingpay.manager.rabbitmq";

    @Bean
    public ManagerUrlRegistrationBean rabbitMQManagerUrlRegistrationBean() {
        ManagerUrlRegistrationBean registrationBean = new ManagerUrlRegistrationBean();
        registrationBean.setTitle("RabbitMQ消费者状态操作");
        registrationBean.setUrl("rabbitmq-ui.html");
        log.debug("注册管理web[{}]", registrationBean.getTitle());
        return registrationBean;
    }

    @Bean
    public ManagerFilterUrlPatternsRegistrationBean rabbitMQManagerFilterUrlPatternsRegistrationBean() {
        Collection<String> urlPatterns = Arrays.asList("/rabbitmq-ui.html", "/rabbitmq/consumer/*");
        log.debug("注册应用url[{}]到权限过滤器", StringUtils.collectionToCommaDelimitedString(urlPatterns));
        return new ManagerFilterUrlPatternsRegistrationBean(urlPatterns);
    }
}