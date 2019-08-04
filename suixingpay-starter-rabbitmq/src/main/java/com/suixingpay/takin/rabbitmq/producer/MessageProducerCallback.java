package com.suixingpay.takin.rabbitmq.producer;

/**
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2018年1月30日 下午3:02:44
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2018年1月30日 下午3:02:44
 */
public interface MessageProducerCallback {
    /**
     * @param id
     * @param ack
     * @param cause
     */
    void onConfirm(String id, boolean ack, String cause);

    /**
     * Returned message callback.
     * 
     * @param messageId
     * @param message the returned message.
     * @param replyCode the reply code.
     * @param replyText the reply text.
     * @param exchange the exchange.
     * @param routingKey the routing key.
     */
    void onReturned(String messageId, Object message, int replyCode, String replyText, String exchange,
            String routingKey);
}
