/**  
 * All rights Reserved, Designed By Suixingpay.
 * @author: jiayu.qiu[qiu_jy@suixingpay.com] 
 * @date: 2017年7月19日 下午10:58:32   
 * @Copyright ©2017 Suixingpay. All rights reserved. 
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.takin.logback.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

/**
 * @author: jiayu.qiu[qiu_jy@suixingpay.com]
 * @date: 2017年7月19日 下午10:58:32
 * @version: V1.0
 * @review: jiayu.qiu[qiu_jy@suixingpay.com]/2017年7月19日 下午10:58:32
 */
@Data
@ConfigurationProperties(prefix = TraceProperties.PREFIX)
public class TraceProperties {
    public static final String PREFIX = "suixingpay.logback.trace";
    public static final String DEFAULT_INVALID_CHARS = "[^A-Za-z0-9_-]";
    public static final String DEFAULT_URL_PATTERN = "/*";
    private static final String DEFAULT_FILTER_NAME= "sxfTraceFilter";
    private static final int DEFAULT_ORDER = -99999;

    /** 是否启用TraceFilter **/
    private boolean enabled = true;

    private String filterName = DEFAULT_FILTER_NAME;
    /**
     * TraceFilter urlPatterns
     */
    private String[] urlPatterns;
    /**
     * Ant-style path patterns 的忽略路径
     */
    private String[] ignoreUrls;

    /**
     * 客户端通过参数或Header传过来的追踪ID的参数名称
     */
    private String traceId = "SXF-TRACE-ID";

    /**
     * traceId 保留的最大长度，如果超过这个长度，则会截取前面的traceIdMaxLength 长度字符，如果小于等于0，则不限制
     */
    private int traceIdMaxLength = 16;

    /**
     * 避免客户端传入的traceId中的特殊字符打乱日志格式，需要把一些非法字符过滤掉
     */
    private String invalidChars = DEFAULT_INVALID_CHARS;
    
    private int order = DEFAULT_ORDER;

}
