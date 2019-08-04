/**  
 * All rights Reserved, Designed By Suixingpay.
 * @author: matieli[ma_tl@suixingpay.com] 
 * @date: 2017年5月31日 下午8:36:10   
 * @Copyright ©2017 Suixingpay. All rights reserved. 
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.takin.exception;

import com.suixingpay.takin.exception.type.ExceptionCode;

/**
 * 404 业务异常
 * 
 * @author: matieli[ma_tl@suixingpay.com]
 * @date: 2017年5月31日 下午8:36:10
 * @version: V1.0
 * @review: matieli[ma_tl@suixingpay.com]/2017年5月31日 下午8:36:10
 */
public class NotFoundException extends BaseException {

    private static final long serialVersionUID = 6586938551931874299L;

    public NotFoundException() {
        super(ExceptionCode.NOT_FOUND, ExceptionCode.NOT_FOUND.defaultMessage());
    }
    public NotFoundException(String message) {
        super(ExceptionCode.NOT_FOUND, message);
    }

    public NotFoundException(Throwable throwable) {
        super(ExceptionCode.NOT_FOUND, throwable);
    }
}
