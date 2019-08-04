package com.suixingpay.common.amqp.rabbitmq.dto;

/**
 * 为了兼容老框架而保留此类，不建议使用
 * 
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2018年4月25日 下午3:59:20
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2018年4月25日 下午3:59:20
 */
@Deprecated
public class AmqpMessage<T> implements Message {

    private static final long serialVersionUID = 1L;

    private final String id;

    private final T message;

    @Override
    public String getId() {
        return this.id;
    }

    public Object getMessage() {
        return this.message;
    }

    public AmqpMessage(T message, String id) {
        this.message = message;
        this.id = id;
    }

    @Override
    public String toString() {
        return "AmqpMessage [id=" + id + ", message=" + message + "]";
    }
}
