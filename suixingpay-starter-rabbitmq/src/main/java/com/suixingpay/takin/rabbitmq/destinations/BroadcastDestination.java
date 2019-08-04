/**  
 * All rights Reserved, Designed By Suixingpay.
 * @author: qiujiayu[qiu_jy@suixingpay.com] 
 * @date: 2018年1月29日 下午11:40:01   
 * @Copyright ©2018 Suixingpay. All rights reserved. 
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.takin.rabbitmq.destinations;

import java.util.UUID;

import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;

/**
 * 使用RabbitMQ Fanout 模式进行发送广播消息;<br>
 * 将消息复制到各个绑定到交换器上的队列中，不需要设置RoutingKey（设置RoutingKey也会被忽略）<br>
 * 业务场景：比如配置中心配置发变更好，通知给各个应用
 * 
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2018年1月29日 下午11:40:01
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2018年1月29日 下午11:40:01
 */
public final class BroadcastDestination extends FanoutDestination {
    private boolean durable = true;
    private boolean autoDelete = true;
    private boolean exclusive = true;

    /**
     * 队列名称默认以："broadcast." + exchangeName 为前缀
     * 
     * @param exchangeName 交换器名称
     */
    public BroadcastDestination(String exchangeName) {
        this(exchangeName, "broadcast." + exchangeName);
    }

    /**
     * @param exchangeName
     * @param queueNamePrefix 队列名称前缀
     */
    public BroadcastDestination(String exchangeName, String queueNamePrefix) {
        super(exchangeName, (null == queueNamePrefix ? "" : queueNamePrefix) + "-"
                + UUID.randomUUID().toString().replaceAll("-", ""));
    }

    @Override
    public final FanoutExchange exchange() {
        if (null == exchange) {
            exchange = new FanoutExchange(exchangeName, durable, autoDelete);
        }
        return exchange;
    }

    @Override
    public final Queue queue() {
        if (null == queue) {
            this.queue = new Queue(queueName, durable, exclusive, autoDelete);
        }
        return queue;
    }

}
