/**  
 * All rights Reserved, Designed By Suixingpay.
 * @author: qiujiayu[qiu_jy@suixingpay.com] 
 * @date: 2018年1月17日 上午10:27:13   
 * @Copyright ©2018 Suixingpay. All rights reserved. 
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.takin.exception;

import com.suixingpay.takin.exception.type.ExceptionCode;

/**
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2018年1月17日 上午10:27:13
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2018年1月17日 上午10:27:13
 */
public class NotModifiedException extends BaseException {

    private static final long serialVersionUID = 8484555345167284243L;

    public NotModifiedException() {
        super(ExceptionCode.NOT_MODIFIED, ExceptionCode.NOT_MODIFIED.defaultMessage());
    }
    
    public NotModifiedException(String message) {
        super(ExceptionCode.NOT_MODIFIED, message);
    }

    public NotModifiedException(Throwable throwable) {
        super(ExceptionCode.NOT_MODIFIED, throwable);
    }
}
