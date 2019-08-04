/**  
 * All rights Reserved, Designed By Suixingpay.
 * @author: qiujiayu[qiu_jy@suixingpay.com] 
 * @date: 2018年3月6日 下午8:40:47   
 * @Copyright ©2018 Suixingpay. All rights reserved. 
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.takin.rabbitmq.demo.config;

import com.suixingpay.takin.rabbitmq.producer.MessageProducerCallback;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * TODO
 * 
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2018年3月6日 下午8:40:47
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2018年3月6日 下午8:40:47
 */
@Configuration
public class ProducerConfiguration {

    @Bean
    public MessageProducerCallback messageProducerCallback() {
        return new MessageProducerCallback() {

            /**
             * 消息发送到RabbitMQ 交换器中情况，注意，此时还没有发送到消息者中 <br/>
             * 当 spring.rabbitmq.publisher-confirms设置为true时生效 <br/>
             */
            @Override
            public void onConfirm(String id, boolean ack, String cause) {
                if (ack) {
                    System.out.println("消息 " + id + " 已经成功发送到交换器中");
                } else {
                    System.err.println("消息 " + id + " 因 " + cause + " 未能发送到交换器中");
                }
            }

            @Override
            public void onReturned(String messageId, Object message, int replyCode, String replyText, String exchange,String routingKey) {
                System.err.println("消息:" + messageId + " 发送到交换器:" + exchange + " 后，无法根据自身的类型和路由键:" + routingKey + " 找到一个符合条件的队列");
            }
        };
    }
}
