/**  
 * All rights Reserved, Designed By Suixingpay.
 * @author: guohongjian[guo_hj@suixingpay.com] 
 * @date: 2017年8月15日 上午10:44:59   
 * @Copyright ©2017 Suixingpay. All rights reserved. 
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.takin.cache.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.DelegatingWebMvcConfiguration;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

/**
 * 注入自定义的CustomRequestMappingHandlerMapping
 * 
 * @author: guohongjian[guo_hj@suixingpay.com]
 * @date: 2017年8月15日 上午10:44:59
 * @version: V1.0
 * @review: guohongjian[guo_hj@suixingpay.com]/2017年8月15日 上午10:44:59
 */
@Configuration
public class CustomWebMvcConfig extends DelegatingWebMvcConfiguration {//

    /**
     * TODO
     * 
     * @return
     * @see org.springframework.boot.autoconfigure.web.WebMvcRegistrations#getRequestMappingHandlerMapping()
     */
    @Override
    protected RequestMappingHandlerMapping createRequestMappingHandlerMapping() {
        return new CustomRequestMappingHandlerMapping();
    }

}
