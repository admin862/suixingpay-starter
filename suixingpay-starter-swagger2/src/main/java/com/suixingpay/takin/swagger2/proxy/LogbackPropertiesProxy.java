/**  
 * All rights Reserved, Designed By Suixingpay.
 * @author: qiujiayu[qiu_jy@suixingpay.com] 
 * @date: 2018年1月11日 上午9:31:16   
 * @Copyright ©2018 Suixingpay. All rights reserved. 
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.takin.swagger2.proxy;

import org.springframework.beans.factory.annotation.Autowired;

import com.suixingpay.takin.logback.config.TraceProperties;

/**
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2018年1月11日 上午9:31:16
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2018年1月11日 上午9:31:16
 */
public class LogbackPropertiesProxy {

    @Autowired
    private TraceProperties logbackProperties;

    public boolean isEnabled() {
        return logbackProperties.isEnabled();
    }

    public String getTraceId() {
        return logbackProperties.getTraceId();
    }

    public String getInvalidChars() {
        return logbackProperties.getInvalidChars();
    }
    
    public int getTraceIdMaxLength() {
        return logbackProperties.getTraceIdMaxLength();
    }
}
