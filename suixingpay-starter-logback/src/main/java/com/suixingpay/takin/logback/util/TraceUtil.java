/**  
 * All rights Reserved, Designed By Suixingpay.
 * @author: qiujiayu[qiu_jy@suixingpay.com] 
 * @date: 2018年4月19日 下午2:57:27   
 * @Copyright ©2018 Suixingpay. All rights reserved. 
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.takin.logback.util;

import org.slf4j.MDC;

/**
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2018年4月19日 下午2:57:27
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2018年4月19日 下午2:57:27
 */
public class TraceUtil {

    private static final String DEFAULT_TRADE_NUM = "tradenum";

    /**
     * @param traceId
     */
    public static void beginTrace(String traceId) {
        MDC.put(DEFAULT_TRADE_NUM, traceId);
    }

    /**
     * 
     */
    public static void beginTrace() {
        String traceId = MiscUtil.genUniqueStr();
        beginTrace(traceId);
    }

    /**
     * 获取当前trace id
     * 
     * @return
     */
    public static String getTraceNum() {
        return MDC.get(DEFAULT_TRADE_NUM);
    }

    /**
     * 
     */
    public static void endTrace() {
        MDC.remove(DEFAULT_TRADE_NUM);
    }
}
