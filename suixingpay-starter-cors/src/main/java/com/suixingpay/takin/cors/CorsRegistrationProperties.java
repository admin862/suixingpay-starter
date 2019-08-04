/**  
 * All rights Reserved, Designed By Suixingpay.
 * @author: jiayu.qiu[qiu_jy@suixingpay.com] 
 * @date: 2017年7月19日 下午10:58:32   
 * @Copyright ©2017 Suixingpay. All rights reserved. 
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.takin.cors;

import java.util.LinkedHashMap;
import java.util.Map;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import lombok.Data;

/**
 * 跨域信息
 * 
 * @author: jiayu.qiu[qiu_jy@suixingpay.com]
 * @date: 2017年7月19日 下午10:58:32
 * @version: V1.0
 * @review: jiayu.qiu[qiu_jy@suixingpay.com]/2017年7月19日 下午10:58:32
 */
@Data
@Validated
@ConfigurationProperties(prefix = CorsRegistrationProperties.PREFIX)
public class CorsRegistrationProperties {
    public static final String PREFIX = "suixingpay.cors";

    /** 是否启用 **/
    private boolean enabled = true;

    /**
     * 描述 : 扫描地址(注意：是Ant风格的路径)
     */
    @NotBlank
    private String mapping = "/**";

    /**
     * 描述 : 允许cookie
     */
    private Boolean allowCredentials = true;

    /**
     * 描述 : 允许的域
     */
    private String allowedOrigins = "*";

    /**
     * 描述 : 允许的方法
     */
    private String allowedMethods = "GET,POST,DELETE,PUT,PATCH";

    /**
     * 描述 : 允许的头信息
     */
    private String allowedHeaders = "*";

    private Map<String, CorsRegistrationInfo> groups = new LinkedHashMap<>();

    @Data
    public static class CorsRegistrationInfo {
        /**
         * 描述 : 扫描地址
         */
        @NotBlank
        private String mapping;

        /**
         * 描述 : 允许cookie
         */
        private Boolean allowCredentials;

        /**
         * 描述 : 允许的域
         */
        private String allowedOrigins;

        /**
         * 描述 : 允许的方法
         */
        private String allowedMethods;

        /**
         * 描述 : 允许的头信息
         */
        private String allowedHeaders;
    }
}
