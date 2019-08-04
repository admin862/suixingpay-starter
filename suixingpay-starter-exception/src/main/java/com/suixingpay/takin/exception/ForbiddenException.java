/**  
 * All rights Reserved, Designed By Suixingpay.
 * @author: qiujiayu[qiu_jy@suixingpay.com] 
 * @date: 2018年1月17日 上午10:33:57   
 * @Copyright ©2018 Suixingpay. All rights reserved. 
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.takin.exception;

import com.suixingpay.takin.exception.type.ExceptionCode;

/**
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2018年1月17日 上午10:33:57
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2018年1月17日 上午10:33:57
 */
public class ForbiddenException extends BaseException {

    private static final long serialVersionUID = 5857465752216212001L;

    public ForbiddenException() {
        super(ExceptionCode.FORBIDDEN, ExceptionCode.FORBIDDEN.defaultMessage());
    }
    
    public ForbiddenException(String message) {
        super(ExceptionCode.FORBIDDEN, message);
    }

    public ForbiddenException(Throwable throwable) {
        super(ExceptionCode.FORBIDDEN, throwable);
    }
}
