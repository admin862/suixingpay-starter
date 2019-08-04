/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: tangqihua[tang_qh@suixingpay.com]
 * @date: 2018年04月10日 15时52分
 * @Copyright ©2018 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.takin.rabbitmq.retry;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

/**
 * @author: tangqihua[tang_qh@suixingpay.com]
 * @date: 2018年04月10日 15时52分
 * @version: V1.0
 * @review: tangqihua[tang_qh@suixingpay.com]/2018年04月10日 15时52分
 */
@Getter
@Setter
@ConfigurationProperties(prefix = RetryCacheProperties.PREFIX)
public class RetryCacheProperties {

    public static final String PREFIX = "suixingpay.rabbitmq.retry";

    /**
     * 是否启用
     */
    private boolean enabled = true;

    /**
     * 排除的交换器名称
     */
    private List<String> excludeExchangeNames;

    /**
     * 本地缓存路径
     */
    private String localCachePath = "./rabbitmq-cache/";

    /**
     * MQ消息retry间隔时间单位：毫秒），默认值为1分钟
     */
    private int retryInterval = 1000 * 60;

    /**
     * MQ消息超时时间（单位：毫秒），默认值为1小时
     */
    private int overTime = 1000 * 60 * 60;

    /**
     * MQ消息本地化间隔（单位：毫秒）, 默认值为1000毫秒
     */
    private int persistentInterval = 1000;

    /**
     * 本地缓存警告容量（本地缓存消息数量达此值是开始告警），默认值为300
     */
    private int alarmCapacity = 300;

    /**
     * 本地缓存最大容量，如果超过此容易，将禁发送消息，默认值为1000
     */
    private int maxCapacity = 1000;
}
