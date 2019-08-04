package com.suixingpay.takin.rabbitmq.consumer;

import java.util.Date;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.util.ErrorHandler;

import com.rabbitmq.client.Channel;
import com.suixingpay.takin.rabbitmq.converter.JsonMessageConverter;
import com.suixingpay.takin.rabbitmq.destinations.IDestination;

import lombok.Getter;

/**
 * 消费者
 * 
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2018年1月30日 下午3:27:45
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2018年1月30日 下午3:27:45
 */
@Getter
public final class SimpleMessageConsumer<T> extends SimpleMessageListenerContainer {

    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleMessageConsumer.class);

    private final IDestination destination;

    /**
     * @param connectionFactory
     * @param destination
     * @param messageHandler
     */
    public SimpleMessageConsumer(ConnectionFactory connectionFactory, IDestination destination,
            MessageHandler<T> messageHandler) {
        if (null == destination) {
            throw new IllegalArgumentException("destination 参数不能为null");
        }
        if (null == messageHandler) {
            throw new IllegalArgumentException("messageHandler 参数不能为null");
        }
        this.destination = destination;
        this.setConnectionFactory(connectionFactory);

        MessageHandlerAdapter<T> handlerAdapter = new MessageHandlerAdapter<T>(this, messageHandler);
        this.setErrorHandler(handlerAdapter);
        this.setMessageListener(handlerAdapter);
        JsonMessageConverter converter = new JsonMessageConverter(messageHandler.getType());
        this.setMessageConverter(converter);
    }

    @PostConstruct
    private void init() {
        try {
            RabbitAdmin rabbitAdmin = (RabbitAdmin) this.getApplicationContext().getBean(AmqpAdmin.class);
            this.setRabbitAdmin(rabbitAdmin);
            if (!destination.isDefaultExchange()) {
                rabbitAdmin.declareExchange(destination.exchange());
            }
            rabbitAdmin.declareQueue(destination.queue());
            rabbitAdmin.declareBinding(destination.binding());
            this.setQueues(destination.queue());
        } catch (Throwable e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    private static class MessageHandlerAdapter<T> implements ChannelAwareMessageListener, ErrorHandler {

        private static final Logger LOGGER = LoggerFactory.getLogger(MessageHandlerAdapter.class);

        private final SimpleMessageConsumer<T> consumer;

        private final MessageHandler<T> handler;

        protected MessageHandlerAdapter(SimpleMessageConsumer<T> consumer, MessageHandler<T> handler) {
            this.consumer = consumer;
            this.handler = handler;
        }

        @Override
        public void onMessage(org.springframework.amqp.core.Message message, Channel channel) throws Exception {
            MessageProperties messageProperties = message.getMessageProperties();
            String messageId = null;
            String appId = null;
            Date timestamp = null;
            if (null != messageProperties) {
                messageId = messageProperties.getMessageId();
                appId = messageProperties.getAppId();
                timestamp = messageProperties.getTimestamp();
            }
            if (null != timestamp) {
                // 消息年龄
                long messageAge = System.currentTimeMillis() - timestamp.getTime();
                // 如果消息年龄过大
                if (messageAge > 120 * 1000) {
                    LOGGER.warn("queue:{}, exchange:{}, messageId:{}, from app:{}, message age:{}ms",
                            consumer.destination.queue().getName(), consumer.destination.exchange().getName(),
                            messageId, appId, messageAge);
                } else if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("queue:{}, exchange:{}, messageId:{}, from app:{}, message age:{}ms",
                            consumer.destination.queue().getName(), consumer.destination.exchange().getName(),
                            messageId, appId, messageAge);
                }
            }
            switch (consumer.getAcknowledgeMode()) {
                case MANUAL:
                    try {
                        @SuppressWarnings("unchecked")
                        T object = (T) consumer.getMessageConverter().fromMessage(message);
                        handler.onMessage(messageId, object, messageProperties);
                        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false); // 确认消息成功消费
                    } catch (Throwable e) {
                        channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);// ack返回false，并重新回到队列
                        LOGGER.error(messageId + ":" + e.getMessage(), e);
                        // handler.onError(e);
                    }
                    break;
                default:
                    try {
                        @SuppressWarnings("unchecked")
                        T object = (T) consumer.getMessageConverter().fromMessage(message);
                        handler.onMessage(messageId, object, messageProperties);
                    } catch (Throwable e) {
                        LOGGER.error(messageId + ":" + e.getMessage(), e);
                        // handler.onError(e);
                    }
                    break;
            }

        }

        @Override
        public void handleError(Throwable t) {
            LOGGER.error(t.getMessage(), t);
            handler.onError(t);
        }
    }
}
