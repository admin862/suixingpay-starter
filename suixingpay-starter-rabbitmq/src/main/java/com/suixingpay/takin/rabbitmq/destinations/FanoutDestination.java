/**  
 * All rights Reserved, Designed By Suixingpay.
 * @author: qiujiayu[qiu_jy@suixingpay.com] 
 * @date: 2018年1月29日 下午11:01:09   
 * @Copyright ©2018 Suixingpay. All rights reserved. 
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.takin.rabbitmq.destinations;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;

import com.suixingpay.takin.rabbitmq.type.ExchangeType;

import lombok.Data;

/**
 * RabbitMQ Fanout 模式;<br>
 * 将消息复制到各个绑定到交换器上的队列中，不需要设置RoutingKey（设置RoutingKey也会被忽略）<br>
 * 业务场景例子：<br>
 * 用户注册交换器：user-register-exchange，绑定了发邮件:send-mail和增加积分:add-score队列，<br>
 * 当用户注册成功时，将用户信息发送到user-register-exchange，发邮件模块和积分系统分别消费send-mail和add-score中的消息<br>
 * 
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2018年1月29日 下午11:01:09
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2018年1月29日 下午11:01:09
 */
@Data
public class FanoutDestination implements IDestination {

    private static final String DEFAULT_ROUTING_KEY = "";

    protected final String exchangeName;

    protected final String queueName;

    protected FanoutExchange exchange;

    protected Queue queue;

    public FanoutDestination(String exchangeName) {
        this(exchangeName, "fanout." + exchangeName);
    }

    public FanoutDestination(String exchangeName, String queueName) {
        if (null == exchangeName || exchangeName.trim().length() == 0) {
            throw new IllegalArgumentException("请设置交换器名称");
        }
        if (null == queueName || queueName.trim().length() == 0) {
            throw new IllegalArgumentException("请设置队列名称");
        }
        this.exchangeName = exchangeName.trim();
        this.queueName = queueName.trim();
    }

    @Override
    public FanoutExchange exchange() {
        if (null == exchange) {
            exchange = new FanoutExchange(exchangeName);
        }
        return exchange;
    }

    @Override
    public Queue queue() {
        if (null == queue) {
            this.queue = new Queue(queueName);
        }
        return queue;
    }

    @Override
    public final Binding binding() {
        return BindingBuilder.bind(queue()).to(exchange());
    }

    @Override
    public final String getRoutingKey() {
        return DEFAULT_ROUTING_KEY;
    }

    @Override
    public final ExchangeType exchangeType() {
        return ExchangeType.FNAOUT;
    }
}
