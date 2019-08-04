/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: jiayu.qiu[qiu_jy@suixingpay.com]
 * @date: 2017年7月19日 下午10:58:32
 * @Copyright ©2017 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.takin.manager.autoconfigure;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.suixingpay.takin.manager.HTTPBasicAuthorizeAttribute;
import com.suixingpay.takin.manager.ManagerFilterUrlPatternsRegistrationBean;
import com.suixingpay.takin.manager.ManagerProperties;
import com.suixingpay.takin.manager.ManagerUiController;
import com.suixingpay.takin.manager.ManagerUrlRegistrationBean;


/**
 * Manager 自动配置
 *
 * @author: jiayu.qiu[qiu_jy@suixingpay.com]
 * @date: 2017年7月19日 下午10:58:32
 * @version: V1.0
 * @review: jiayu.qiu[qiu_jy@suixingpay.com]/2017年7月19日 下午10:58:32
 */
@Configuration
@ConditionalOnWebApplication
@EnableConfigurationProperties({ManagerProperties.class})
@ConditionalOnProperty(value = ManagerProperties.PREFIX + ".enabled", matchIfMissing = true)
public class ManagerAutoConfiguration {

    @Autowired
    private ManagerProperties managerProperties;

    @Bean
    @ConditionalOnWebApplication
    public FilterRegistrationBean filterRegistrationBean(ObjectProvider<ManagerFilterUrlPatternsRegistrationBean[]> urlRegistrationBeans) {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        HTTPBasicAuthorizeAttribute httpBasicFilter = new HTTPBasicAuthorizeAttribute(managerProperties);
        registrationBean.setFilter(httpBasicFilter);
        List<String> urlPatterns = new ArrayList<String>();

        ManagerFilterUrlPatternsRegistrationBean[] registrationBeans = urlRegistrationBeans.getIfAvailable();
        if(null != registrationBeans) {
            for (ManagerFilterUrlPatternsRegistrationBean urlRegistrationBean : registrationBeans) {
                urlPatterns.addAll(urlRegistrationBean.getUrlPatterns());
            }
        }

        List<String> patterns = managerProperties.getUrlPatterns();
        if (null != patterns && patterns.size() > 0){
            urlPatterns.addAll(patterns);
        }
        urlPatterns.add("/manager-ui.html");
        urlPatterns.add("/manager-ui/*");
        registrationBean.setUrlPatterns(urlPatterns);
        return registrationBean;
    }

    @Bean
    @ConditionalOnMissingBean(ManagerUiController.class)
    public ManagerUiController managerUiController(ObjectProvider<ManagerUrlRegistrationBean[]> urlRegistrationBeans){
        return new ManagerUiController(urlRegistrationBeans, managerProperties);
    }
}
