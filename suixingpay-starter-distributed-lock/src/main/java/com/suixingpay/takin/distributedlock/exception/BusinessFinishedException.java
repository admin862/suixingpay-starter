/**  
 * All rights Reserved, Designed By Suixingpay.
 * @author: jiayu.qiu[qiu_jy@suixingpay.com] 
 * @date: 2017年7月19日 下午10:58:32   
 * @Copyright ©2017 Suixingpay. All rights reserved. 
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.takin.distributedlock.exception;

/**
 * 业务已处理异常（用于避免业务重复处得）
 * 
 * @author: jiayu.qiu[qiu_jy@suixingpay.com]
 * @date: 2017年7月19日 下午10:58:32
 * @version: V1.0
 * @review: jiayu.qiu[qiu_jy@suixingpay.com]/2017年7月19日 下午10:58:32
 */
public final class BusinessFinishedException extends Exception {

    private static final long serialVersionUID = 1917782235298269710L;
    private final String businessStatusKey;

    public BusinessFinishedException(String businessStatusKey, String message) {
        super(message);
        this.businessStatusKey = businessStatusKey;
    }

    public String getBusinessStatusKey() {
        return businessStatusKey;
    }

    @Override
    public String toString() {
        return "BusinessFinishedException [businessStatusKey=" + businessStatusKey + "]";
    }

}
