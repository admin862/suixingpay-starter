/**  
 * All rights Reserved, Designed By Suixingpay.
 * @author: qiujiayu[qiu_jy@suixingpay.com] 
 * @date: 2018年3月7日 上午11:20:50   
 * @Copyright ©2018 Suixingpay. All rights reserved. 
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.takin.rabbitmq.demo.converter;

import com.suixingpay.takin.rabbitmq.consumer.MessageHandler;
import com.suixingpay.takin.rabbitmq.converter.JsonMessageConverter;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO
 * 
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2018年3月7日 上午11:20:50
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2018年3月7日 上午11:20:50
 */
public class JsonMessageConverterTest {

    /**
     * TODO
     * 
     * @param args
     */
    @SuppressWarnings("unchecked")
    public static void main(String[] args) {
        List<User> users = new ArrayList<>();
        users.add(new User(1L, "name1"));
        users.add(new User(2L, "name2"));
        users.add(new User(3L, "name3"));

        MessageHandler<List<User>> messageHandler = new MessageHandler<List<User>>() {

            @Override
            public void onMessage(String messageId, List<User> message, MessageProperties messageProperties)
                    throws Exception {
                // TODO Auto-generated method stub

            }

            @Override
            public void onError(Throwable t) {
                // TODO Auto-generated method stub

            }

        };

        System.out.println(messageHandler.getType());
        JsonMessageConverter converter = new JsonMessageConverter(messageHandler.getType());

        Message message = converter.toMessage(users, new MessageProperties());

        users = (List<User>) converter.fromMessage(message);

        for (User user : users) {
            System.out.println(user);
        }

        MessageHandler<Object> messageHandler1 = new MessageHandler<Object>() {

            @Override
            public void onMessage(String messageId, Object message, MessageProperties messageProperties)
                    throws Exception {
                // TODO Auto-generated method stub

            }

            @Override
            public void onError(Throwable t) {
                // TODO Auto-generated method stub

            }

        };

        System.out.println(messageHandler1.getType());
    }

    @Data
    @AllArgsConstructor
    static class User {
        private Long id;
        private String name;
    }

}
