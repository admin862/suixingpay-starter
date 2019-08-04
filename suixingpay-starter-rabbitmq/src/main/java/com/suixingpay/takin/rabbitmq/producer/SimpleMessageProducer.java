package com.suixingpay.takin.rabbitmq.producer;

import java.util.Date;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpIllegalStateException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate.ReturnCallback;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.amqp.support.converter.MessageConverter;

import com.suixingpay.takin.rabbitmq.converter.JsonMessageConverter;
import com.suixingpay.takin.rabbitmq.destinations.IDestination;
import com.suixingpay.takin.rabbitmq.retry.MessageCache;
import com.suixingpay.takin.rabbitmq.retry.RetryCache;

import lombok.Getter;
import lombok.Setter;

/**
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2018年1月30日 下午2:41:48
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2018年1月30日 下午2:41:48
 */
@Setter
@Getter
public final class SimpleMessageProducer implements RabbitTemplate.ConfirmCallback, ReturnCallback {
    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleMessageProducer.class);
    private static final JsonMessageConverter messageConverter = new JsonMessageConverter(null);
    /**
     * 用于发送消息
     */
    private final RabbitTemplate rabbitTemplate;

    /**
     * 消息回调
     */
    private MessageProducerCallback callback;

    /**
     * 消息重试缓存
     */
    private RetryCache retryCache;

    /**
     * 生产者名称，用于告诉消费者消息是由哪个应用产生的， 默认是spring.application.name
     */
    private String appName;

    /**
     * @param rabbitTemplate
     */
    public SimpleMessageProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
        this.rabbitTemplate.setMessageConverter(messageConverter);
        this.rabbitTemplate.setConfirmCallback(this);
        this.rabbitTemplate.setReturnCallback(this);
    }

    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        // 成功发送到RabbitMQ 交换器中，注意，此时还没有发送到消息者中
        if (ack) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("消息 {} 已经成功发送到交换器中", correlationData.getId());
            }
            if (null != retryCache) {
                retryCache.del(correlationData.getId());
            }
        } else {
            if (LOGGER.isWarnEnabled()) {
                LOGGER.warn("消息 {} 因{} 未能发送到交换器中", correlationData.getId(), cause);
            }
        }
        if (null != callback) {
            callback.onConfirm(correlationData.getId(), ack, cause);
        }
    }

    @Override
    public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
        Object messageData = this.rabbitTemplate.getMessageConverter().fromMessage(message);
        MessageProperties messageProperties = message.getMessageProperties();
        if (LOGGER.isWarnEnabled()) {
            LOGGER.warn("消息:{} 发送到交换器:{}后，无法根据自身的类型和路由键:{}找到一个符合条件的队列，replyCode：{}, replyText {}.，详细信息：{}", messageData,
                    exchange, routingKey, replyCode, replyText, messageProperties);
        }
        String messageId = null;
        if (null != messageProperties) {
            messageId = messageProperties.getMessageId();
        }
        if (null != callback) {
            callback.onReturned(messageId, messageData, replyCode, replyText, exchange, routingKey);
        }
    }

    private Message convertMessageIfNecessary(final Object object, MessageProperties messageProperties) {
        if (object instanceof Message) {
            return (Message) object;
        }
        if (null == messageProperties) {
            messageProperties = new MessageProperties();
        }
        return getRequiredMessageConverter().toMessage(object, messageProperties);
    }

    private MessageConverter getRequiredMessageConverter() throws IllegalStateException {
        MessageConverter converter = this.rabbitTemplate.getMessageConverter();
        if (converter == null) {
            throw new AmqpIllegalStateException(
                    "No 'messageConverter' specified. Check configuration of RabbitTemplate.");
        }
        return converter;
    }

    /**
     * @param destination IDestination实例
     * @param message 消息内容
     * @param messageProperties MessageProperties，用于设置消息属性
     * @throws Exception
     */
    public void sendMessage(IDestination destination, Object message, MessageProperties messageProperties)
            throws Exception {
        String messageId = null;
        if (null != messageProperties) {
            messageId = messageProperties.getMessageId();
        }
        if (null == messageId || messageId.isEmpty()) {
            messageId = UUID.randomUUID().toString().replaceAll("-", "");
        }
        String routingKey = destination.getRoutingKey();
        if (null != routingKey && routingKey.indexOf("*") != -1 && routingKey.indexOf("#") != -1) {
            throw new Exception("路由键\"" + routingKey + "\"中包含有*或#字符!");
        }

        try {
            Message sendMessage = convertMessageIfNecessary(message, messageProperties);
            messageProperties = sendMessage.getMessageProperties();
            if (null != messageProperties) {
                if (null == messageProperties.getMessageId()) {
                    messageProperties.setMessageId(messageId);
                }
                if (null == messageProperties.getCorrelationIdString()) {
                    messageProperties.setCorrelationIdString(messageId);
                }
                messageProperties.setAppId(appName);
                messageProperties.setTimestamp(new Date());
            }
            String exchangeName = destination.exchange().getName();
            // 添加到缓存
            MessageCache messageCache = null;
            if (null != retryCache) {
                messageCache = retryCache.add(messageId, exchangeName, destination.getRoutingKey(), sendMessage);
            }
            doSendMessage(exchangeName, destination.getRoutingKey(), sendMessage, messageId);
            if (null != messageCache) {
                messageCache.setSended(true);
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            if (null != retryCache) {
                retryCache.del(messageId);
            }
            throw e;
        }
    }

    /**
     * @param exchangeName
     * @param routingKey
     * @param message
     * @param messageId
     */
    public void doSendMessage(String exchangeName, String routingKey, Message message, String messageId) {
        rabbitTemplate.send(exchangeName, routingKey, message, new CorrelationData(messageId));
    }

    /**
     * @param destination
     * @param message
     * @throws Exception
     */
    public void sendMessage(IDestination destination, Object message) throws Exception {
        sendMessage(destination, message, null);
    }

}
