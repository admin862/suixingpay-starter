/**  
 * All rights Reserved, Designed By Suixingpay.
 * @author: jiayu.qiu[qiu_jy@suixingpay.com] 
 * @date: 2017年7月19日 下午10:58:32   
 * @Copyright ©2017 Suixingpay. All rights reserved. 
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.takin.token;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.env.Environment;

import com.suixingpay.takin.token.repository.client.ClientRepositoryType;

import lombok.Data;
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
@ConfigurationProperties(prefix = TokenProperties.PREFIX)
public class TokenProperties {
    public static final String PREFIX = "suixingpay.token";
    @Autowired
    private Environment env;

    /** 是否启用 **/
    private boolean enabled = true;

    /**
     * 是否启用同一时刻单一回话模式
     */
    private boolean singleMode = false;

    /**
     * 命名空间
     */
    private String namespace;

    /**
     * 匹配路径
     */
    private String[] pathPatterns = new String[] { "/**" };

    /**
     * 排除路径
     */
    private String[] excludePathPatterns = new String[] {};

    /**
     * token会话过期时间:最后一次访问，超过timeout，则说明会话过期，单位秒
     */
    private int timeout = 30 * 60;

    /**
     * token过期时间：token生成超过maxTimeout，强制token失效，单位秒
     */
    private int maxTimeout = 12 * 60 * 60;

    /**
     * 客户端传token服务端使用的名字
     */
    private String tokenName = "SXF-TOKEN";

    /**
     * 一个用户允许同时在线的token数量(必须大于1)
     */
    private int maxTokensForOneUser = 5;

    /**
     * 客户端存储token的类型 注意： 如果默认数组长度大于1时，不能设置默认值，可能会出现重复值问题
     */
    private ClientRepositoryType[] clientRepositoryTypes;

    private Cookie cookie = new Cookie();

    private int order = Integer.MAX_VALUE;

    @PostConstruct
    public void init() {
        if (null != env && (null == namespace || namespace.trim().length() == 0)) {
            String applicationName = env.getProperty("spring.application.name");
            if (null != applicationName && applicationName.trim().length() > 0) {
                namespace = applicationName;
                return;
            }
        }
        namespace = "";
    }

    public void setMaxTokensForOneUser(int maxTokensForOneUser) {
        if (maxTokensForOneUser > 1) {
            this.maxTokensForOneUser = maxTokensForOneUser;
        }
    }

    @Data
    public static class Cookie {
        private String domain = null;
        private boolean httpOnly = false;
        private int maxAge = 0;
        private String path = null;
        private boolean secure = false;
    }

}
