/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2017年8月15日 下午6:24:23
 * @Copyright ©2017 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.takin.cache.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * TODO
 *
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2017年8月15日 下午6:24:23
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2017年8月15日 下午6:24:23
 */
@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
        registry.addResourceHandler("autoload-cache-ui.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("manager-ui.html").addResourceLocations("classpath:/META-INF/resources/");

    }
}
