/**  
 * All rights Reserved, Designed By Suixingpay.
 * @author: qiujiayu[qiu_jy@suixingpay.com] 
 * @date: 2018年1月30日 上午12:00:46   
 * @Copyright ©2018 Suixingpay. All rights reserved. 
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.takin.rabbitmq.destinations;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;

import com.suixingpay.takin.rabbitmq.type.ExchangeType;

import lombok.Data;

/**
 * RabbitMQ topic模式;<br>
 * topic 模式允许通*、#通配符进行模式匹配，性能会比较差；<br>
 * 业务场景：日志处理，将日志根据不同级别进行处理<br>
 * 交换器：log-exchange<br>
 * 错误日志队列：error-log-queue通过"error.*" routingKey 绑定到log-exchange上<br>
 * 调试日志队列：debug-log-queue通过"debug.*" routingKey 绑定到log-exchange上<br>
 * INFO日志队列：info-log-queue通过"info.*" routingKey 绑定到log-exchange上<br>
 * 所有日志队列：all-log-queue通过"#" routingKey 绑定到log-exchange上<br>
 * 
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2018年1月30日 上午12:00:46
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2018年1月30日 上午12:00:46
 */
@Data
public final class TopicDestination implements IDestination {

    private final String exchangeName;

    private final String queueName;

    private final String routingKey;

    private TopicExchange exchange;

    private Queue queue;

    public TopicDestination(String exchangeName, String routingKey) {
        this(exchangeName, "topic." + exchangeName, routingKey);
    }

    public TopicDestination(String exchangeName, String queueName, String routingKey) {
        if (null == exchangeName || exchangeName.trim().length() == 0) {
            throw new IllegalArgumentException("请设置交换器名称");
        }
        if (null == queueName || queueName.trim().length() == 0) {
            throw new IllegalArgumentException("请设置队列名称");
        }
        if (null == routingKey || routingKey.trim().length() == 0) {
            throw new IllegalArgumentException("请设置路由Key");
        }
        this.exchangeName = exchangeName.trim();
        this.queueName = queueName.trim();
        this.routingKey = routingKey.trim();
    }

    @Override
    public TopicExchange exchange() {
        if (null == exchange) {
            exchange = new TopicExchange(exchangeName);
        }
        return exchange;
    }

    @Override
    public Queue queue() {
        if (null == queue) {
            queue = new Queue(queueName);
        }
        return queue;
    }

    @Override
    public Binding binding() {
        return BindingBuilder.bind(queue()).to(exchange()).with(getRoutingKey());
    }

    @Override
    public String getRoutingKey() {
        return routingKey;
    }

    @Override
    public ExchangeType exchangeType() {
        return ExchangeType.TOPIC;
    }
}
