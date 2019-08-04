/**  
 * All rights Reserved, Designed By Suixingpay.
 * @author: jiayu.qiu[qiu_jy@suixingpay.com] 
 * @date: 2017年7月19日 下午10:58:32   
 * @Copyright ©2017 Suixingpay. All rights reserved. 
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.takin.manager;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

/**
 * @author: jiayu.qiu[qiu_jy@suixingpay.com]
 * @date: 2017年7月19日 下午10:58:32
 * @version: V1.0
 * @review: jiayu.qiu[qiu_jy@suixingpay.com]/2017年7月19日 下午10:58:32
 */
@Getter
@Setter
@ConfigurationProperties(prefix = ManagerProperties.PREFIX)
public class ManagerProperties {
    public static final String PREFIX = "suixingpay.manager";
    
    private boolean enabled = true;

    private String userName = "admin";

    private String password = "sxfAdmin";
    
    private List<String> urlPatterns;

    private String sessionName = "sxf_manager_auth";
    
    private List<ManagerUrlRegistrationBean> urls;
}
