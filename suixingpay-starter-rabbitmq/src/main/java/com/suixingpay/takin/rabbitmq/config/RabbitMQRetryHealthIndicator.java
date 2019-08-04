/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: tangqihua[tang_qh@suixingpay.com]
 * @date: 2018年04月09日 16时45分
 * @Copyright ©2018 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.takin.rabbitmq.config;

import org.springframework.boot.actuate.health.AbstractHealthIndicator;
import org.springframework.boot.actuate.health.Health.Builder;

import com.suixingpay.takin.rabbitmq.retry.RetryCache;
import com.suixingpay.takin.rabbitmq.retry.RetryCacheProperties;

/**
 * @author: tangqihua[tang_qh@suixingpay.com]
 * @date: 2018年04月09日 16时45分
 * @version: V1.0
 * @review: tangqihua[tang_qh@suixingpay.com]/2018年04月09日 16时45分
 */
public class RabbitMQRetryHealthIndicator extends AbstractHealthIndicator {

    private final RetryCache retryCache;

    private final RetryCacheProperties retryCacheProperties;

    public RabbitMQRetryHealthIndicator(RetryCache retryCache, RetryCacheProperties retryCacheProperties) {
        this.retryCache = retryCache;
        this.retryCacheProperties = retryCacheProperties;
    }

    @Override
    protected void doHealthCheck(Builder builder) {
        int messageSize = retryCache.getMessageLocalCache().size();
        if (messageSize >= retryCacheProperties.getMaxCapacity()) {
            builder.unknown().withDetail("MessageCache.error", "未确认消息已达" + retryCacheProperties.getMaxCapacity() + "条");
        } else if (messageSize >= retryCacheProperties.getAlarmCapacity()) {
            builder.unknown().withDetail("MessageCache.warn",
                    "未确认消息已达" + retryCacheProperties.getAlarmCapacity() + "条");
        } else {
            builder.up().withDetail("MessageCache.size", messageSize);
        }
    }
}
