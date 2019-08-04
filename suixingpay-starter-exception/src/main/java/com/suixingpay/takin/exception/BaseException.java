/**  
 * All rights Reserved, Designed By Suixingpay.
 * @author: matieli[ma_tl@suixingpay.com] 
 * @date: 2017年5月24日 下午1:18:52   
 * @Copyright ©2017 Suixingpay. All rights reserved. 
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.takin.exception;

/**
 * 异常基础类
 * 
 * @author: matieli[ma_tl@suixingpay.com]
 * @date: 2017年5月24日 下午1:18:52
 * @version: V1.0
 * @review: matieli[ma_tl@suixingpay.com]/2017年5月24日 下午1:18:52
 */
public abstract class BaseException extends RuntimeException {
    private static final long serialVersionUID = 6626868446456726497L;

    private final IExceptionCode code;

    private final String message;

    public BaseException(IExceptionCode code, String message) {
        this.code = code;
        if ((null == message || message.length() == 0) && null != code) {
            message = code.defaultMessage();
        }
        this.message = message;
    }

    public BaseException(IExceptionCode code, Throwable throwable) {
        super(throwable);
        this.code = code;
        if (null == throwable && null != code) {
            message = code.defaultMessage();
        } else {
            this.message = throwable.getMessage();
        }
    }

    public IExceptionCode getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }

}
