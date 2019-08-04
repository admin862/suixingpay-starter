/**  
 * All rights Reserved, Designed By Suixingpay.
 * @author: qiujiayu[qiu_jy@suixingpay.com] 
 * @date: 2018年3月26日 上午11:24:47   
 * @Copyright ©2018 Suixingpay. All rights reserved. 
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.takin.manager;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2018年3月26日 上午11:24:47
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2018年3月26日 上午11:24:47
 */
public class ManagerFilterUrlPatternsRegistrationBean {
    
    private final Set<String> urlPatterns;

    public ManagerFilterUrlPatternsRegistrationBean(Collection<String> urlPatterns) {
        this.urlPatterns = new LinkedHashSet<String>(urlPatterns);
    }

    public Set<String> getUrlPatterns() {
        return urlPatterns;
    }

}
