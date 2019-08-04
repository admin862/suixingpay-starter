/**  
 * All rights Reserved, Designed By Suixingpay.
 * @author: qiujiayu[qiu_jy@suixingpay.com] 
 * @date: 2018年2月1日 上午10:44:01   
 * @Copyright ©2018 Suixingpay. All rights reserved. 
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.takin.web.autoconfigure;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.ConverterRegistry;

import com.suixingpay.takin.web.converter.StringToBaseEnumConverterFactory;
import com.suixingpay.takin.web.converter.StringToDateConverter;

/**
 * 类型转换器自动配置
 * 
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2018年2月1日 上午10:44:01
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2018年2月1日 上午10:44:01
 */
@Configuration
@ConditionalOnWebApplication
@EnableConfigurationProperties(WebProperties.class)
public class ConversionServiceAutoConfiguration {
    @Autowired
    private ConverterRegistry converterRegistry;

    @Autowired
    private WebProperties properties;

    @PostConstruct
    public void init() {
        if (properties.getConverter().getStringToDate().isEnabled()) {
            converterRegistry.addConverter(new StringToDateConverter());
        }
        if (properties.getConverter().getStringToBaseEnum().isEnabled()) {
            converterRegistry.addConverterFactory(new StringToBaseEnumConverterFactory());
        }
    }

}
