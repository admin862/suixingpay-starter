/**  
 * All rights Reserved, Designed By Suixingpay.
 * @author: qiujiayu[qiu_jy@suixingpay.com] 
 * @date: 2018年2月1日 上午9:46:18   
 * @Copyright ©2018 Suixingpay. All rights reserved. 
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.takin.data.enums;

/**
 * 枚举与数字映射
 * 
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2018年2月1日 上午9:46:18
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2018年2月1日 上午9:46:18
 */
public interface BaseEnum {

    /**
     * @return
     */
    int getCode();

    /**
     * 显示名称
     * 
     * @return
     */
    String getDisplayName();
}
