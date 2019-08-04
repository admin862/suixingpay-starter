/**  
 * All rights Reserved, Designed By Suixingpay.
 * @author: jiayu.qiu[qiu_jy@suixingpay.com] 
 * @date: 2017年7月19日 下午10:58:32   
 * @Copyright ©2017 Suixingpay. All rights reserved. 
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.takin.logback.config;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.suixingpay.takin.logback.filter.TraceFilter;

/**
 * Logback Trace
 * 
 * @author: jiayu.qiu[qiu_jy@suixingpay.com]
 * @date: 2017年7月19日 下午10:58:32
 * @version: V1.0
 * @review: jiayu.qiu[qiu_jy@suixingpay.com]/2017年7月19日 下午10:58:32
 */
@Configuration
@ConditionalOnWebApplication
@ConditionalOnClass(Filter.class)
@EnableConfigurationProperties({ TraceProperties.class })
@ConditionalOnProperty(value = TraceProperties.PREFIX + ".enabled", matchIfMissing = true)
public class TraceAutoConfiguration {
    
    
    @Autowired
    private TraceProperties properties;

    @Bean
    public TraceFilter sxfTraceFilter() {
        return new TraceFilter(properties);
    }

    @Bean
    public FilterRegistrationBean sxfTraceFilterRegistrationBean(TraceFilter sxfTraceFilter) {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(sxfTraceFilter);
        registration.setName(properties.getFilterName());
        String[] urlPatterns = properties.getUrlPatterns();
        if (null == urlPatterns || urlPatterns.length == 0) {
            urlPatterns = new String[] { TraceProperties.DEFAULT_URL_PATTERN };
        }
        registration.addUrlPatterns(urlPatterns);
        registration.setOrder(properties.getOrder());
        // registration.setDispatcherTypes(EnumSet.allOf(DispatcherType.class));
        registration.setDispatcherTypes(DispatcherType.REQUEST);
        return registration;
    }
}
