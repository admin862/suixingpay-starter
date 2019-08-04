/**  
 * All rights Reserved, Designed By Suixingpay.
 * @author: jiayu.qiu[qiu_jy@suixingpay.com] 
 * @date: 2017年7月19日 下午10:58:32   
 * @Copyright ©2017 Suixingpay. All rights reserved. 
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.takin.cors.autoconfigure;

import java.util.Map;

import javax.servlet.Filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.suixingpay.takin.cors.CorsRegistrationProperties;
import com.suixingpay.takin.cors.CorsRegistrationProperties.CorsRegistrationInfo;

/**
 * 跨域自动配置
 * 
 * @author: jiayu.qiu[qiu_jy@suixingpay.com]
 * @date: 2017年7月19日 下午10:58:32
 * @version: V1.0
 * @review: jiayu.qiu[qiu_jy@suixingpay.com]/2017年7月19日 下午10:58:32
 */
@Configuration
@ConditionalOnWebApplication
@ConditionalOnClass({ Filter.class, WebMvcConfigurer.class })
@EnableConfigurationProperties({ CorsRegistrationProperties.class })
@ConditionalOnProperty(value = CorsRegistrationProperties.PREFIX + ".enabled", matchIfMissing = true)
public class CorsAutoConfiguration extends WebMvcConfigurerAdapter{
    @Autowired
    private CorsRegistrationProperties corsProperties;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        Map<String, CorsRegistrationInfo> groups = corsProperties.getGroups();
        if (null != groups && groups.size() > 0) {
            for (CorsRegistrationInfo item : groups.values()) {
                addMappings(registry, item);
            }
        } else {
            addMappings(registry);
        }
    }

    private void addMappings(CorsRegistry registry) {
        CorsRegistration cr = registry.addMapping(corsProperties.getMapping());
        if (corsProperties.getAllowCredentials() != null) {
            cr.allowCredentials(corsProperties.getAllowCredentials());
        }
        if (isNotBlank(corsProperties.getAllowedOrigins())) {
            String[] allowedOriginArray = corsProperties.getAllowedOrigins().split(",");
            cr.allowedOrigins(allowedOriginArray);
        }
        if (isNotBlank(corsProperties.getAllowedMethods())) {
            String[] allowedMethodArray = corsProperties.getAllowedMethods().split(",");
            cr.allowedMethods(allowedMethodArray);
        }
        if (isNotBlank(corsProperties.getAllowedHeaders())) {
            String[] allowedHeaderArray = corsProperties.getAllowedHeaders().split(",");
            cr.allowedHeaders(allowedHeaderArray);
        }
    }

    private void addMappings(CorsRegistry registry, CorsRegistrationInfo item) {
        CorsRegistration cr = registry.addMapping(item.getMapping());
        if (item.getAllowCredentials() != null) {
            cr.allowCredentials(item.getAllowCredentials());
        } else if (corsProperties.getAllowCredentials() != null) {
            cr.allowCredentials(corsProperties.getAllowCredentials());
        }
        if (isNotBlank(item.getAllowedOrigins())) {
            String[] allowedOriginArray = item.getAllowedOrigins().split(",");
            cr.allowedOrigins(allowedOriginArray);
        } else if (isNotBlank(corsProperties.getAllowedOrigins())) {
            String[] allowedOriginArray = corsProperties.getAllowedOrigins().split(",");
            cr.allowedOrigins(allowedOriginArray);
        }
        if (isNotBlank(item.getAllowedMethods())) {
            String[] allowedMethodArray = item.getAllowedMethods().split(",");
            cr.allowedMethods(allowedMethodArray);
        } else if (isNotBlank(corsProperties.getAllowedMethods())) {
            String[] allowedMethodArray = corsProperties.getAllowedMethods().split(",");
            cr.allowedMethods(allowedMethodArray);
        }
        if (isNotBlank(item.getAllowedHeaders())) {
            String[] allowedHeaderArray = item.getAllowedHeaders().split(",");
            cr.allowedHeaders(allowedHeaderArray);
        } else if (isNotBlank(corsProperties.getAllowedHeaders())) {
            String[] allowedHeaderArray = corsProperties.getAllowedHeaders().split(",");
            cr.allowedHeaders(allowedHeaderArray);
        }
    }

    private boolean isNotBlank(String str) {
        return null != str && str.length() > 0;
    }
}
