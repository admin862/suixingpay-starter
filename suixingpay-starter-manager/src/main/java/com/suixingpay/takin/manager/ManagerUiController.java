/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: tangqihua[tang_qh@suixingpay.com]
 * @date: 2018年03月20日 15时45分
 * @Copyright ©2018 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.takin.manager;

import java.util.LinkedHashSet;
import java.util.Set;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 管理器列表
 * 
 * @author: tangqihua[tang_qh@suixingpay.com]
 * @date: 2018年03月20日 15时45分
 * @version: V1.0
 * @review: tangqihua[tang_qh@suixingpay.com]/2018年03月20日 15时45分
 */
@RestController
@RequestMapping("/manager-ui/")
public class ManagerUiController {

    private final Set<ManagerUrlRegistrationBean> urls;

    public ManagerUiController(ObjectProvider<ManagerUrlRegistrationBean[]> urlRegistrationBeans, ManagerProperties managerProperties) {
        this.urls = new LinkedHashSet<ManagerUrlRegistrationBean>();
        if(null != managerProperties.getUrls()) {
            this.urls.addAll(managerProperties.getUrls());
        }
        ManagerUrlRegistrationBean[] registrationBeans = urlRegistrationBeans.getIfAvailable();
        if(null != registrationBeans && registrationBeans.length>0) {
            for(ManagerUrlRegistrationBean item: registrationBeans) {
                this.urls.add(item);
            }
        }
    }

    @GetMapping
    public Set<ManagerUrlRegistrationBean> list() {
        return urls;
    }
}
