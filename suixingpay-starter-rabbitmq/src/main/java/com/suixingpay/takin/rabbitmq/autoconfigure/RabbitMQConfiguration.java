/**  
 * All rights Reserved, Designed By Suixingpay.
 * @author: qiujiayu[qiu_jy@suixingpay.com] 
 * @date: 2018年3月6日 下午5:18:35   
 * @Copyright ©2018 Suixingpay. All rights reserved. 
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.takin.rabbitmq.autoconfigure;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.AbstractMessageListenerContainer;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import com.suixingpay.takin.rabbitmq.manager.RabbitmqConsumerManagerController;
import com.suixingpay.takin.rabbitmq.producer.MessageProducerCallback;
import com.suixingpay.takin.rabbitmq.producer.SimpleMessageProducer;

import lombok.extern.slf4j.Slf4j;

/**
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2018年3月6日 下午5:18:35
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2018年3月6日 下午5:18:35
 */
@Slf4j
@Configuration
@ConditionalOnBean(RabbitTemplate.class)
@AutoConfigureAfter(RabbitAutoConfiguration.class)
public class RabbitMQConfiguration {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private Environment env;

    private final MessageProducerCallback messageProducerCallback;

    public RabbitMQConfiguration(ObjectProvider<MessageProducerCallback> messageProducerCallbackHelperProvider) {
        if (null != messageProducerCallbackHelperProvider) {
            this.messageProducerCallback = messageProducerCallbackHelperProvider.getIfAvailable();
        } else {
            this.messageProducerCallback = null;
        }
    }

    @Bean
    public SimpleMessageProducer simpleMessageProducer() {
        SimpleMessageProducer producer = new SimpleMessageProducer(rabbitTemplate);
        producer.setCallback(messageProducerCallback);
        String applicationName = env.getProperty("spring.application.name");
        if (null != applicationName && applicationName.trim().length() > 0) {
            producer.setAppName(applicationName);
        }
        return producer;
    }

    @Bean
    @ConditionalOnWebApplication
    @ConditionalOnMissingBean(RabbitmqConsumerManagerController.class)
    public RabbitmqConsumerManagerController rabbitmqConsumerManagerController(
            ObjectProvider<AbstractMessageListenerContainer[]> listenerContainer) {
        log.debug("配置管理web [{}]", "RabbitmqConsumerManagerController");
        return new RabbitmqConsumerManagerController(listenerContainer);
    }
}
