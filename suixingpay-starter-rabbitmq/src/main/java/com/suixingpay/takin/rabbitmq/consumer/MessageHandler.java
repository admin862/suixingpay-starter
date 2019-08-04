package com.suixingpay.takin.rabbitmq.consumer;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import org.springframework.amqp.core.MessageProperties;

/**
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2018年1月30日 下午10:43:55
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2018年1月30日 下午10:43:55
 */
public abstract class MessageHandler<T> {

    /**
     * 如果处理失败，请抛异常
     * 
     * @param messageId
     * @param message
     * @param messageProperties
     * @return
     */
    public abstract void onMessage(String messageId, T message, MessageProperties messageProperties) throws Exception;

    /**
     * @param t
     */
    public abstract void onError(Throwable t);

    public final Type getType() {
        Type superClass = this.getClass().getGenericSuperclass();
        if (superClass instanceof Class<?>) {
            throw new IllegalArgumentException("必须设置MessageHandler中的泛型类型");
        }
        Type type = ((ParameterizedType) superClass).getActualTypeArguments()[0];

        if (type.getTypeName().equals(Object.class.getName())) {
            throw new IllegalArgumentException("MessageHandler中的泛型类型不能是Object");
        }
        return type;
    }
}
