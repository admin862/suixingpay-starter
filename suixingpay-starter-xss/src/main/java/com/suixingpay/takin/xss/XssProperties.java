/**  
 * All rights Reserved, Designed By Suixingpay.
 * @author: jiayu.qiu[qiu_jy@suixingpay.com] 
 * @date: 2017年7月19日 下午10:58:32   
 * @Copyright ©2017 Suixingpay. All rights reserved. 
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.takin.xss;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import lombok.Data;

/**
 * 跨站请求防范
 * 
 * @author: jiayu.qiu[qiu_jy@suixingpay.com]
 * @date: 2017年7月19日 下午10:58:32
 * @version: V1.0
 * @review: jiayu.qiu[qiu_jy@suixingpay.com]/2017年7月19日 下午10:58:32
 */
@Data
@Validated
@ConfigurationProperties(prefix = XssProperties.PREFIX)
public class XssProperties {
    public static final String PREFIX = "suixingpay.xss";
    public static final String DEFAULT_URL_PATTERN = "/*";
    private static final String DEFAULT_FILTER_NAME = "sxfXssFilter";

    private static final int DEFAULT_ORDER = -9999;

    /** 是否启用 **/
    private boolean enabled = true;

    private String filterName = DEFAULT_FILTER_NAME;

    /**
     * TraceFilter urlPatterns
     */
    private String[] urlPatterns = {};

    /**
     * Ant-style path patterns 的忽略路径
     */
    private String[] ignoreUrls = {};

    private int order = DEFAULT_ORDER;

}
