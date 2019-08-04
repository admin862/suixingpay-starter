/**  
 * All rights Reserved, Designed By Suixingpay.
 * @author: qiujiayu[qiu_jy@suixingpay.com] 
 * @date: 2018年2月1日 下午4:16:55   
 * @Copyright ©2018 Suixingpay. All rights reserved. 
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.takin.exception;

import com.suixingpay.takin.exception.type.ExceptionCode;

/**
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2018年2月1日 下午4:16:55
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2018年2月1日 下午4:16:55
 */
public class ServiceUnavailableException extends BaseException {

    private static final long serialVersionUID = 1L;

    public ServiceUnavailableException() {
        super(ExceptionCode.SERVICE_UNAVAILABLE, ExceptionCode.SERVICE_UNAVAILABLE.defaultMessage());
    }

    public ServiceUnavailableException(String message) {
        super(ExceptionCode.SERVICE_UNAVAILABLE, message);
    }

    public ServiceUnavailableException(Throwable throwable) {
        super(ExceptionCode.SERVICE_UNAVAILABLE, throwable);
    }
}
