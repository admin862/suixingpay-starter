/**  
 * All rights Reserved, Designed By Suixingpay.
 * @author: qiujiayu[qiu_jy@suixingpay.com] 
 * @date: 2018年3月6日 下午9:28:03   
 * @Copyright ©2018 Suixingpay. All rights reserved. 
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.takin.rabbitmq.demo.config;

import org.springframework.amqp.core.MessageProperties;

import com.suixingpay.takin.rabbitmq.consumer.MessageHandler;

/**
 * TODO
 * 
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2018年3月6日 下午9:28:03
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2018年3月6日 下午9:28:03
 */
public class TestMessageHandler extends MessageHandler<String> {

    private final String consumerName;

    private final boolean testAck;

    public TestMessageHandler(String consumerName, boolean testAck) {
        this.consumerName = consumerName;
        this.testAck = testAck;
    }

    public TestMessageHandler(String consumerName) {
        this.consumerName = consumerName;
        testAck = false;
    }

    @Override
    public void onMessage(String messageId, String message, MessageProperties messageProperties) throws Exception {
        System.out.println(consumerName + "--->" + message + ";messageId:" + messageId);
        if (testAck) {
            throw new Exception("ack test");
        }
    }

    @Override
    public void onError(Throwable t) {
        System.err.println(consumerName + "--->" + t.getMessage());
    }
}
