/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: tangqihua[tang_qh@suixingpay.com]
 * @date: 2018年04月16日 09时55分
 * @Copyright ©2018 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.takin.rabbitmq.manager;

import com.suixingpay.takin.rabbitmq.converter.JsonMessageConverter;
import com.suixingpay.takin.rabbitmq.retry.MessageCache;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author: tangqihua[tang_qh@suixingpay.com]
 * @date: 2018年04月16日 09时55分
 * @version: V1.0
 * @review: tangqihua[tang_qh@suixingpay.com]/2018年04月16日 09时55分
 */
@Setter
@Getter
public class MessageContent implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    private String exchangeName;

    private String routingKey;

    private String message;

    private String firstSendTime;

    private String lastSendTime;

    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");

    public MessageContent(MessageCache messageCache) {
        if (null == messageCache) {
            throw new RuntimeException("messageCache is null");
        }
        this.id = messageCache.getId();
        this.exchangeName = messageCache.getExchangeName();
        this.routingKey = messageCache.getRoutingKey();
        this.message = JsonMessageConverter.getMessageContent(messageCache.getMessage());
        this.firstSendTime = DATE_FORMAT.format(new Date(messageCache.getFirstSendTime()));
        this.lastSendTime = DATE_FORMAT.format(new Date(messageCache.getLastSendTime()));
    }
}