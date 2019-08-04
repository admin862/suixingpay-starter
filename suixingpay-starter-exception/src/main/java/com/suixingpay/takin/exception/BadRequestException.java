/**  
 * All rights Reserved, Designed By Suixingpay.
 * @author: qiujiayu[qiu_jy@suixingpay.com] 
 * @date: 2018年1月17日 上午10:29:58   
 * @Copyright ©2018 Suixingpay. All rights reserved. 
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.takin.exception;

import com.suixingpay.takin.exception.type.ExceptionCode;

/**
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2018年1月17日 上午10:29:58
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2018年1月17日 上午10:29:58
 */
public class BadRequestException extends BaseException {

    private static final long serialVersionUID = 3110418009731774131L;

    public BadRequestException() {
        super(ExceptionCode.BAD_REQUEST, ExceptionCode.BAD_REQUEST.defaultMessage());
    }
    
    public BadRequestException(String message) {
        super(ExceptionCode.BAD_REQUEST, message);
    }

    public BadRequestException(Throwable throwable) {
        super(ExceptionCode.BAD_REQUEST, throwable);
    }

}
