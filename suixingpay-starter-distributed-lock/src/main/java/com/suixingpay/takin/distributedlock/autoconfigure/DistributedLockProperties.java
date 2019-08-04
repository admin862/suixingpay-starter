/**  
 * All rights Reserved, Designed By Suixingpay.
 * @author: jiayu.qiu[qiu_jy@suixingpay.com] 
 * @date: 2017年7月19日 下午10:58:32   
 * @Copyright ©2017 Suixingpay. All rights reserved. 
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.takin.distributedlock.autoconfigure;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.env.Environment;

/**
 * 分布式锁的参数配置
 * 
 * @author: jiayu.qiu[qiu_jy@suixingpay.com]
 * @date: 2017年7月19日 下午10:58:32
 * @version: V1.0
 * @review: jiayu.qiu[qiu_jy@suixingpay.com]/2017年7月19日 下午10:58:32
 */
@ConfigurationProperties(prefix = "suixingpay.distributedlock")
public class DistributedLockProperties {

    /**
     * 命名空间
     */
    private String namespace;

    @Autowired
    private Environment env;

    private boolean namespaceEnable = false;

    private String zkServers;
    /**
     * 设置AOP中的执行顺序。 分布式锁的开启，应该尽量在前面，所以此值要尽量小。如果一个方法中有多个AOP拦截时，需要注意调整此值。
     */
    private Integer aopOrder = 0;

    @PostConstruct
    public void init() {
        if (namespaceEnable && null != env) {
            if (null == namespace || namespace.trim().length() == 0) {
                String applicationName = env.getProperty("spring.application.name");
                if (null != applicationName && applicationName.trim().length() > 0) {
                    namespace = applicationName;
                }
            }
        }
    }

    public boolean isNamespaceEnable() {
        return namespaceEnable;
    }

    public void setNamespaceEnable(boolean namespaceEnable) {
        this.namespaceEnable = namespaceEnable;
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public String getZkServers() {
        return zkServers;
    }

    public void setZkServers(String zkServers) {
        this.zkServers = zkServers;
    }

    public Integer getAopOrder() {
        return aopOrder;
    }

    public void setAopOrder(Integer aopOrder) {
        this.aopOrder = aopOrder;
    }

}
