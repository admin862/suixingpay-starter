/**  
 * All rights Reserved, Designed By Suixingpay.
 * @author: qiujiayu[qiu_jy@suixingpay.com] 
 * @date: 2018年1月19日 下午1:14:58   
 * @Copyright ©2018 Suixingpay. All rights reserved. 
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.takin.exception;

import lombok.Data;

/**
 * 异常信息
 * 
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2018年1月19日 下午1:14:58
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2018年1月19日 下午1:14:58
 */
@Data
public class ExceptionInfo {
    /**
     * 错误码
     */
    private final int code;
    /**
     * 错误信息
     */
    private final String message;

    public ExceptionInfo(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
