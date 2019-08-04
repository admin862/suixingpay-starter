package com.suixingpay.common.amqp.rabbitmq.dto;

import java.io.Serializable;

/**
 * 为了兼容老框架而保留此类，不建议使用
 * 
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2018年4月25日 下午3:59:20
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2018年4月25日 下午3:59:20
 */
@Deprecated
public interface Message extends Serializable {
    String getId();
}
