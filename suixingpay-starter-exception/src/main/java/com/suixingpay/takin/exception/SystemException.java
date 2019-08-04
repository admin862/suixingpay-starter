/**  
 * All rights Reserved, Designed By Suixingpay.
 * @author: matieli[ma_tl@suixingpay.com] 
 * @date: 2017年5月24日 下午1:21:38   
 * @Copyright ©2017 Suixingpay. All rights reserved. 
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.takin.exception;

import com.suixingpay.takin.exception.type.ExceptionCode;

/**
 * 系统级异常
 * 
 * @author: matieli[ma_tl@suixingpay.com]
 * @date: 2017年5月24日 下午1:21:38
 * @version: V1.0
 * @review: matieli[ma_tl@suixingpay.com]/2017年5月24日 下午1:21:38
 */
public class SystemException extends BaseException {
    private static final long serialVersionUID = 8996110607302347653L;

    public SystemException() {
        super(ExceptionCode.INTERNAL_SERVER_ERROR, ExceptionCode.INTERNAL_SERVER_ERROR.defaultMessage());
    }
    
    public SystemException(String message) {
        super(ExceptionCode.INTERNAL_SERVER_ERROR, message);
    }

    public SystemException(Throwable throwable) {
        super(ExceptionCode.INTERNAL_SERVER_ERROR, throwable);
    }
}
