/**  
 * All rights Reserved, Designed By Suixingpay.
 * @author: qiujiayu[qiu_jy@suixingpay.com] 
 * @date: 2018年3月6日 下午4:02:11   
 * @Copyright ©2018 Suixingpay. All rights reserved. 
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.takin.rabbitmq.demo.config;

import com.suixingpay.takin.rabbitmq.consumer.SimpleMessageConsumer;
import com.suixingpay.takin.rabbitmq.destinations.DirectDestination;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * TODO
 * 
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2018年3月6日 下午4:02:11
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2018年3月6日 下午4:02:11
 */
@Configuration
public class ConsumerConfiguration {
    @Autowired
    private ConnectionFactory connectionFactory;

    @Bean
    public SimpleMessageConsumer<String> directA() {
        String exchangeName = "direct.qiujy-test";
        String queueName = "direct.qiujy-test";
        DirectDestination destination = new DirectDestination(exchangeName, queueName);
        String consumerName = "directA";
        boolean testAck = false;
        SimpleMessageConsumer<String> consumer = new SimpleMessageConsumer<String>(connectionFactory, destination, new TestMessageHandler(consumerName, testAck));
         consumer.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        return consumer;
    }

//    @Bean
//    public SimpleMessageConsumer<String> directB() {
//        String exchangeName = "direct.qiujy-test";
//        String queueName = "direct.qiujy-test";
//        DirectDestination destination = new DirectDestination(exchangeName, queueName);
//        String consumerName = "directB";
//        boolean testAck = false;
//        SimpleMessageConsumer<String> consumer = new SimpleMessageConsumer<String>(connectionFactory, destination, new TestMessageHandler(consumerName, testAck));
//        // consumer.setAcknowledgeMode(AcknowledgeMode.MANUAL);
//        return consumer;
//    }

//    @Bean
//    public SimpleMessageConsumer<String> fanoutA() {
//        String exchangeName = "fanout.qiujy-test";
//        String queueName = "fanout.qiujy-test-queue-a";
//        FanoutDestination destination = new FanoutDestination(exchangeName, queueName);
//        String consumerName = "fanoutA";
//        boolean testAck = false;
//        return new SimpleMessageConsumer<String>(connectionFactory, destination, new TestMessageHandler(consumerName, testAck));
//    }
//
//    @Bean
//    public SimpleMessageConsumer<String> fanoutB() {
//        String exchangeName = "fanout.qiujy-test";
//        String queueName = "fanout.qiujy-test-queue-b";
//        FanoutDestination destination = new FanoutDestination(exchangeName, queueName);
//        String consumerName = "fanoutB";
//        boolean testAck = false;
//        return new SimpleMessageConsumer<String>(connectionFactory, destination, new TestMessageHandler(consumerName, testAck));
//    }
//
//    @Bean
//    public SimpleMessageConsumer<String> fanoutB1() {
//        String exchangeName = "fanout.qiujy-test";
//        String queueName = "fanout.qiujy-test-queue-b";
//        FanoutDestination destination = new FanoutDestination(exchangeName, queueName);
//        String consumerName = "fanoutB1";
//        boolean testAck = false;
//        return new SimpleMessageConsumer<String>(connectionFactory, destination, new TestMessageHandler(consumerName, testAck));
//    }
//
//    @Bean
//    public SimpleMessageConsumer<String> broadcastA() {
//        String exchangeName = "broadcast.qiujy-test";
//        String queueNamePrefix = "broadcast.a.";
//        BroadcastDestination destination = new BroadcastDestination(exchangeName, queueNamePrefix);
//        String consumerName = "broadcastA";
//        boolean testAck = false;
//        return new SimpleMessageConsumer<String>(connectionFactory, destination, new TestMessageHandler(consumerName, testAck));
//    }
//
//    @Bean
//    public SimpleMessageConsumer<String> broadcastB() {
//        String exchangeName = "broadcast.qiujy-test";
//        String queueNamePrefix = "broadcast.b.";
//        BroadcastDestination destination = new BroadcastDestination(exchangeName, queueNamePrefix);
//        String consumerName = "broadcastB";
//        boolean testAck = false;
//        return new SimpleMessageConsumer<String>(connectionFactory, destination, new TestMessageHandler(consumerName, testAck));
//    }
//
//
//    @Bean
//    public SimpleMessageConsumer<String> systemAErrorLog() {
//        String exchangeName = "topic.qiujy-logs-exchange";
//        String queueName = "topic.qiujy-test-system-a-error-log";
//        String bindingKey = "systemA.error.log";
//        TopicDestination destination = new TopicDestination(exchangeName, queueName, bindingKey);
//        String consumerName = "systemAErrorLog";
//        boolean testAck = false;
//        return new SimpleMessageConsumer<String>(connectionFactory, destination, new TestMessageHandler(consumerName, testAck));
//    }
//
//    @Bean
//    public SimpleMessageConsumer<String> systemAAllLog() {
//        String exchangeName = "topic.qiujy-logs-exchange";
//        String queueName = "topic.qiujy-test-system-a-all-log";
//        String bindingKey = "systemA.*.log";
//        TopicDestination destination = new TopicDestination(exchangeName, queueName, bindingKey);
//        String consumerName = "systemAAllLog";
//        boolean testAck = false;
//        return new SimpleMessageConsumer<String>(connectionFactory, destination, new TestMessageHandler(consumerName, testAck));
//    }
//
//    @Bean
//    public SimpleMessageConsumer<String> systemBAllLog() {
//        String exchangeName = "topic.qiujy-logs-exchange";
//        String queueName = "topic.qiujy-test-system-b-all-log";
//        String bindingKey = "systemB.*.log";
//        TopicDestination destination = new TopicDestination(exchangeName, queueName, bindingKey);
//        String consumerName = "systemBAllLog";
//        boolean testAck = false;
//        return new SimpleMessageConsumer<String>(connectionFactory, destination, new TestMessageHandler(consumerName, testAck));
//    }
//
//    @Bean
//    public SimpleMessageConsumer<String> allSystemAllLog() {
//        String exchangeName = "topic.qiujy-logs-exchange";
//        String queueName = "topic.qiujy-test-all-system-all-log";
//        String bindingKey = "#";
//        TopicDestination destination = new TopicDestination(exchangeName, queueName, bindingKey);
//        String consumerName = "allSystemAllLog";
//        boolean testAck = false;
//        return new SimpleMessageConsumer<String>(connectionFactory, destination, new TestMessageHandler(consumerName, testAck));
//    }

}
