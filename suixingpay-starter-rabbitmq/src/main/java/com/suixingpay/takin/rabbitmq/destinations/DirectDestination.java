/**  
 * All rights Reserved, Designed By Suixingpay.
 * @author: qiujiayu[qiu_jy@suixingpay.com] 
 * @date: 2018年1月29日 下午10:40:11   
 * @Copyright ©2018 Suixingpay. All rights reserved. 
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.takin.rabbitmq.destinations;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;

import com.suixingpay.takin.rabbitmq.type.ExchangeType;

import lombok.Data;

/**
 * RabbitMQ direct 模式;<br>
 * 消息直接发送到队列，不需要缓存 routingKey 就是队列名称
 * 
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2018年1月29日 下午10:40:11
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2018年1月29日 下午10:40:11
 */
@Data
public final class DirectDestination implements IDestination {

    private final String exchangeName;

    private final String queueName;

    private final String routingKey;

    private DirectExchange exchange;

    private Queue queue;

    public DirectDestination(String queueName) {
        this(DEFAULT_EXCHAGE_NAME, queueName);
    }

    public DirectDestination(String exchangeName, String queueName) {
        if (null == exchangeName) {
            throw new IllegalArgumentException("请设置交换器名称");
        }
        if (null == queueName || queueName.trim().length() == 0) {
            throw new IllegalArgumentException("请设置队列名称");
        }
        this.exchangeName = exchangeName.trim();
        this.queueName = queueName.trim();
        this.routingKey = this.queueName;
    }

    @Override
    public DirectExchange exchange() {
        if (null == exchange) {
            this.exchange = new DirectExchange(exchangeName);
        }
        return exchange;
    }

    @Override
    public Queue queue() {
        if (null == queue) {
            this.queue = new Queue(queueName.trim());
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
        return ExchangeType.DIRECT;
    }

}
