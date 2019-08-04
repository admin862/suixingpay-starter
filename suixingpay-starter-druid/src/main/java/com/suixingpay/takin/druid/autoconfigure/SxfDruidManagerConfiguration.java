/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: tangqihua[tang_qh@suixingpay.com]
 * @date: 2018年04月17日 10时09分
 * @Copyright ©2018 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.takin.druid.autoconfigure;

import com.alibaba.druid.spring.boot.autoconfigure.properties.DruidStatProperties;
import com.suixingpay.takin.manager.ManagerFilterUrlPatternsRegistrationBean;
import com.suixingpay.takin.manager.ManagerUrlRegistrationBean;
import com.suixingpay.takin.manager.autoconfigure.ManagerAutoConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.Collection;

/**
 * @author: tangqihua[tang_qh@suixingpay.com]
 * @date: 2018年04月17日 10时09分
 * @version: V1.0
 * @review: tangqihua[tang_qh@suixingpay.com]/2018年04月17日 10时09分
 */
@Slf4j
@Configuration
@ConditionalOnWebApplication
@AutoConfigureBefore(ManagerAutoConfiguration.class)
@ConditionalOnClass(ManagerUrlRegistrationBean.class)
@ConditionalOnProperty(value = { SxfDruidManagerConfiguration.PREFIX + ".enabled",
        "spring.datasource.druid.stat-view-servlet.enabled" }, matchIfMissing = true)
public class SxfDruidManagerConfiguration {

    public static final String PREFIX = "suixingpay.manager.druid";

    @Autowired
    private DruidStatProperties properties;

    @Bean
    public ManagerUrlRegistrationBean DruidManagerUrlRegistrationBean() {

        ManagerUrlRegistrationBean registrationBean = new ManagerUrlRegistrationBean();
        registrationBean.setTitle("Druid监控");
        registrationBean.setUrl(getUrlPrefix() + "index.html");
        log.debug("注册管理web[{}]", registrationBean.getTitle());
        return registrationBean;
    }

    @Bean
    public ManagerFilterUrlPatternsRegistrationBean DruidManagerFilterUrlPatternsRegistrationBean() {
        Collection<String> urlPatterns = Arrays.asList("/" + getUrlPrefix() + "*");
        log.debug("注册应用url[{}]到权限过滤器", StringUtils.collectionToCommaDelimitedString(urlPatterns));
        return new ManagerFilterUrlPatternsRegistrationBean(urlPatterns);
    }

    private String getUrlPrefix() {
        DruidStatProperties.StatViewServlet config = properties.getStatViewServlet();
        String urlPattern;
        if (config.getUrlPattern() != null) {
            urlPattern = config.getUrlPattern();
            // 去除头"/"与尾的"*"
            urlPattern = urlPattern.substring(1, urlPattern.length() - 1);
        } else {
            urlPattern = "druid/";
        }
        return urlPattern;
    }
}
