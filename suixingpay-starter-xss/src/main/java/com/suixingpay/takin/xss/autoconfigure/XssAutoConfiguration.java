/**  
 * All rights Reserved, Designed By Suixingpay.
 * @author: jiayu.qiu[qiu_jy@suixingpay.com] 
 * @date: 2017年7月19日 下午10:58:32   
 * @Copyright ©2017 Suixingpay. All rights reserved. 
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.takin.xss.autoconfigure;

import java.util.HashSet;
import java.util.Set;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.suixingpay.takin.xss.XssFilter;
import com.suixingpay.takin.xss.XssProperties;

/**
 * 跨站请求防范
 * 
 * @author: jiayu.qiu[qiu_jy@suixingpay.com]
 * @date: 2017年7月19日 下午10:58:32
 * @version: V1.0
 * @review: jiayu.qiu[qiu_jy@suixingpay.com]/2017年7月19日 下午10:58:32
 */
@Configuration
@ConditionalOnWebApplication
@ConditionalOnClass({ Filter.class, WebMvcConfigurer.class })
@ConditionalOnBean(Jackson2ObjectMapperBuilder.class)
@EnableConfigurationProperties({ XssProperties.class })
@ConditionalOnProperty(value = XssProperties.PREFIX + ".enabled", matchIfMissing = true)
public class XssAutoConfiguration extends WebMvcConfigurerAdapter {
    @Autowired
    private XssProperties properties;

    private final ObjectMapper mapper;

    public XssAutoConfiguration(ObjectProvider<ObjectMapper> objectMapperProvider) {
        if (null != objectMapperProvider && null != objectMapperProvider.getIfAvailable()) {
            mapper = objectMapperProvider.getIfAvailable();
        } else {
            mapper = new ObjectMapper();
        }
    }

    @Bean
    public XssFilter sxfXssFilter() {
        return new XssFilter(properties, mapper);
    }

    @Bean
    public FilterRegistrationBean sxfXssFilterRegistrationBean(XssFilter sxfXssFilter) {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(sxfXssFilter);
        registration.setName(properties.getFilterName());
        String[] urlPatterns = properties.getUrlPatterns();
        if (null == urlPatterns || urlPatterns.length == 0) {
            urlPatterns = new String[] { XssProperties.DEFAULT_URL_PATTERN };
        }
        registration.addUrlPatterns(urlPatterns);
        registration.setOrder(properties.getOrder());
        // registration.setDispatcherTypes(EnumSet.allOf(DispatcherType.class));
        registration.setDispatcherTypes(DispatcherType.REQUEST);

        Set<String> set = new HashSet<>();
        set.add("/js/**");
        set.add("/css/**");
        set.add("/images/**");
        set.add("/static/**");
        set.add("/favicon.ico");
        set.add("/**/favicon.ico");
        set.add("/webjars/**");
        set.add("/swagger-ui.html");
        set.add("/v2/api-docs");
        set.add("/swagger-resources/**");
        for (String s : properties.getIgnoreUrls()) {
            set.add(s);
        }
        String[] ignoreUrls = new String[set.size()];
        ignoreUrls = set.toArray(ignoreUrls);
        properties.setIgnoreUrls(ignoreUrls);
        return registration;
    }
}
