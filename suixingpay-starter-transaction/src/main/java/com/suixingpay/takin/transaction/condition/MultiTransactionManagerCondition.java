/**  
 * All rights Reserved, Designed By Suixingpay.
 * @author: qiujiayu[qiu_jy@suixingpay.com] 
 * @date: 2018年2月6日 下午1:25:41   
 * @Copyright ©2018 Suixingpay. All rights reserved. 
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.takin.transaction.condition;

/**
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2018年2月6日 下午1:25:41
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2018年2月6日 下午1:25:41
 */
public class MultiTransactionManagerCondition extends AbstractTransactionManagerCondition {

    @Override
    protected boolean matches(boolean finded) {
        return finded;
    }

}
