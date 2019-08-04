/**  
 * All rights Reserved, Designed By Suixingpay.
 * @author: qiujiayu[qiu_jy@suixingpay.com] 
 * @date: 2018年1月31日 上午11:26:19   
 * @Copyright ©2018 Suixingpay. All rights reserved. 
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.takin.validation.autoconfigure;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * 验证框架自动配置<br/>
 * 实现 ValidationMessages 文件支持直接使用中文<br/>
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2018年1月31日 上午11:26:19
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2018年1月31日 上午11:26:19
 */
@Configuration
public class ValidatorAutoConfiguration extends WebMvcConfigurerAdapter {

    @Bean
    public MessageSource xsfValidationMessageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:ValidationMessages");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }

    @Bean
    public LocalValidatorFactoryBean sxfValidator() {
        LocalValidatorFactoryBean factoryBean = new LocalValidatorFactoryBean();
        factoryBean.setValidationMessageSource(xsfValidationMessageSource());
        return factoryBean;
    }

    @Override
    public org.springframework.validation.Validator getValidator() {
        return sxfValidator();
    }
}
