/**  
 * All rights Reserved, Designed By Suixingpay.
 * @author: qiujiayu[qiu_jy@suixingpay.com] 
 * @date: 2018年1月17日 上午10:32:12   
 * @Copyright ©2018 Suixingpay. All rights reserved. 
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.takin.exception;

import com.suixingpay.takin.exception.type.ExceptionCode;

/**
 * 
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2018年1月17日 上午10:32:12
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2018年1月17日 上午10:32:12
 */
public class UnauthorizedException extends BaseException {

    private static final long serialVersionUID = -8095359574406530829L;

    public UnauthorizedException() {
        super(ExceptionCode.UNAUTHORIZED, ExceptionCode.UNAUTHORIZED.defaultMessage());
    }
    
    public UnauthorizedException(String message) {
        super(ExceptionCode.UNAUTHORIZED, message);
    }

    public UnauthorizedException(Throwable throwable) {
        super(ExceptionCode.UNAUTHORIZED, throwable);
    }
}
