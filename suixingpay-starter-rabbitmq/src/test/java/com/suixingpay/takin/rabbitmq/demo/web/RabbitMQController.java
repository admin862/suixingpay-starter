/**  
 * All rights Reserved, Designed By Suixingpay.
 * @author: qiujiayu[qiu_jy@suixingpay.com] 
 * @date: 2018年3月6日 下午2:09:12   
 * @Copyright ©2018 Suixingpay. All rights reserved. 
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.takin.rabbitmq.demo.web;

import com.suixingpay.takin.rabbitmq.destinations.BroadcastDestination;
import com.suixingpay.takin.rabbitmq.destinations.DirectDestination;
import com.suixingpay.takin.rabbitmq.destinations.FanoutDestination;
import com.suixingpay.takin.rabbitmq.destinations.TopicDestination;
import com.suixingpay.takin.rabbitmq.producer.SimpleMessageProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * TODO
 * 
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2018年3月6日 下午2:09:12
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2018年3月6日 下午2:09:12
 */
@RestController

public class RabbitMQController {

    @Autowired
    private SimpleMessageProducer simpleMessageProducer;

    @GetMapping("direct")
    public String directSender() {
        String exchangeName = "direct.qiujy-test";
        String queueName = "direct.qiujy-test";
        DirectDestination destination = new DirectDestination(exchangeName, queueName);
        try {
            simpleMessageProducer.sendMessage(destination, "direct-test:" + System.currentTimeMillis());
            return "OK";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "error";
    }

    @GetMapping("fanout")
    public String sender() {
        // 生产者只需要设置交换器名字
        String exchangeName = "fanout.qiujy-test";
        FanoutDestination destination = new FanoutDestination(exchangeName);
        try {
            simpleMessageProducer.sendMessage(destination, "fanout-test:" + System.currentTimeMillis());
            return "OK";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "error";
    }

    @GetMapping("broadcast")
    public String broadcast() {
        // 生产者只需要设置交换器名字
        String exchangeName = "broadcast.qiujy-test";
        BroadcastDestination destination = new BroadcastDestination(exchangeName);
        try {
            simpleMessageProducer.sendMessage(destination, "broadcast-test:" + System.currentTimeMillis());
            return "OK";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "error";
    }

    @GetMapping("topic")
    public String topic() {
        // 日志收集
        String exchangeName = "topic.qiujy-logs-exchange";
        try {
            // 系统A的error日志
            sendTopic(exchangeName, "systemA.error.log", "systema-error-message-1");
            // 系统B的error日志
            sendTopic(exchangeName, "systemB.error.log", "systemb-error-message-2");
            // 系统A的info日志
            sendTopic(exchangeName, "systemA.info.log", "systema-info-message-3");
            // 系统B的info日志
            sendTopic(exchangeName, "systemB.info.log", "systemb-info-message-4");
            return "OK";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "error";
    }

    private void sendTopic(String exchangeName, String routingKey, String message) throws Exception {
        TopicDestination destination = new TopicDestination(exchangeName, routingKey);
        simpleMessageProducer.sendMessage(destination, message);
    }

}
