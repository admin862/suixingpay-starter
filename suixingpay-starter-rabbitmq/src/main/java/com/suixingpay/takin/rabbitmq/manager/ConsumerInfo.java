/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: tangqihua[tang_qh@suixingpay.com]
 * @date: 2018年03月19日 17时48分
 * @Copyright ©2018 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.takin.rabbitmq.manager;

import lombok.Data;

/**
 * @author: tangqihua[tang_qh@suixingpay.com]
 * @date: 2018年03月19日 17时48分
 * @version: V1.0
 * @review: tangqihua[tang_qh@suixingpay.com]/2018年03月19日 17时48分
 */
@Data
public class ConsumerInfo {
    private String name;

    private String queueNames;

    private boolean running;
}
