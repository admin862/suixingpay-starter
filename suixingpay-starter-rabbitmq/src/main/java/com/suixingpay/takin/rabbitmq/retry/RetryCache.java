/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: tangqihua[tang_qh@suixingpay.com]
 * @date: 2018年04月08日 10时23分
 * @Copyright ©2018 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.takin.rabbitmq.retry;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.amqp.core.Message;

import lombok.Getter;

/**
 * @author: tangqihua[tang_qh@suixingpay.com]
 * @date: 2018年04月08日 10时23分
 * @version: V1.0
 * @review: tangqihua[tang_qh@suixingpay.com]/2018年04月08日 10时23分
 */
@Getter
public class RetryCache {

    private final Map<String, MessageCache> messageLocalCache;
    private final RetryCacheProperties retryCacheProperties;

    public RetryCache(RetryCacheProperties retryCacheProperties) {
        this.retryCacheProperties = retryCacheProperties;
        this.messageLocalCache = new ConcurrentHashMap<>(retryCacheProperties.getMaxCapacity());
    }

    public MessageCache add(String id, String exchangeName, String routingKey, Message message) {
        if (null == id || message == null) {
            return null;
        }
        boolean isExclude = isExcludeExchange(exchangeName);
        if (isExclude) {
            return null;
        }
        if (messageLocalCache.size() >= retryCacheProperties.getMaxCapacity()) {
            throw new RuntimeException(
                    "The count of unack message is over stock, " + retryCacheProperties.getMaxCapacity());
        }
        MessageCache messageCache = new MessageCache();
        messageCache.setId(id);
        messageCache.setExchangeName(exchangeName);
        messageCache.setRoutingKey(routingKey);
        messageCache.setMessage(message);
        messageCache.setFirstSendTime(System.currentTimeMillis());
        messageCache.setLastSendTime(System.currentTimeMillis());
        messageCache.setSended(false);
        messageLocalCache.put(id, messageCache);
        return messageCache;
    }

    public void del(String id) {
        if (null == id) {
            return;
        }
        messageLocalCache.remove(id);
    }

    public void del(MessageCache messageCache) {
        if (null == messageCache || null == messageCache.getId()) {
            return;
        }
        messageLocalCache.remove(messageCache.getId());
    }

    public void putAll(Map<String, MessageCache> m) {
        if (null == m || m.size() == 0) {
            return;
        }
        messageLocalCache.putAll(m);
    }

    /**
     * 根据exchange name判断是否需要排除
     *
     * @param exchangeName
     * @return
     */
    private boolean isExcludeExchange(String exchangeName) {
        if (null == retryCacheProperties.getExcludeExchangeNames()) {
            return false;
        }
        if (null == exchangeName || exchangeName.length() == 0) {
            return true;
        }
        for (String exchange : retryCacheProperties.getExcludeExchangeNames()) {
            if (exchangeName.equals(exchange)) {
                return true;
            }
        }
        return false;
    }

}
