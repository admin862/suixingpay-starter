/**  
 * All rights Reserved, Designed By Suixingpay.
 * @author: qiujiayu[qiu_jy@suixingpay.com] 
 * @date: 2018年1月17日 下午6:10:41   
 * @Copyright ©2018 Suixingpay. All rights reserved. 
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.takin.swagger2;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.suixingpay.takin.swagger2.proxy.TokenPropertiesProxy;
import com.suixingpay.takin.token.TokenProperties;
import com.suixingpay.takin.token.autoconfigure.TokenAutoConfiguration;

/**
 * 避免强依赖做特殊处理
 * 
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2018年1月17日 下午6:10:41
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2018年1月17日 下午6:10:41
 */
@Configuration
@ConditionalOnClass(TokenAutoConfiguration.class)
@AutoConfigureAfter({ TokenAutoConfiguration.class })
public class TokenPropertiesProxyConfiguration {

    @Bean
    @ConditionalOnBean(TokenProperties.class)
    public TokenPropertiesProxy sxfSwagger2TokenPropertiesProxy() {
        return new TokenPropertiesProxy();
    }
}
