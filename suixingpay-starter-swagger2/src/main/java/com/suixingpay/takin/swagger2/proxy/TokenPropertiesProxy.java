/**  
 * All rights Reserved, Designed By Suixingpay.
 * @author: qiujiayu[qiu_jy@suixingpay.com] 
 * @date: 2018年1月11日 上午9:31:16   
 * @Copyright ©2018 Suixingpay. All rights reserved. 
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.takin.swagger2.proxy;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;

import com.suixingpay.takin.token.TokenProperties;
import com.suixingpay.takin.token.repository.client.ClientRepositoryType;

/**
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2018年1月11日 上午9:31:16
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2018年1月11日 上午9:31:16
 */
public class TokenPropertiesProxy {

    @Autowired
    private TokenProperties tokenProperties;

    public boolean isEnabled() {
        return tokenProperties.isEnabled();
    }

    public String getTokenName() {
        return tokenProperties.getTokenName();
    }

    public Set<String> getParameterTypes() {
        ClientRepositoryType[] clientRepositoryTypes = tokenProperties.getClientRepositoryTypes();
        int size = 0;
        if (null != clientRepositoryTypes && clientRepositoryTypes.length > 0) {
            size = clientRepositoryTypes.length;
        }
        Set<String> set = new HashSet<>(size);
        for (int i = 0; i < size; i++) {
            ClientRepositoryType type = clientRepositoryTypes[i];
            switch (type) {
                case PARAM:
                    set.add("query");
                    break;
                case HEADER:
                    set.add("header");
                    break;
                case COOKIE:
                    set.add("cookie");
                    break;
                default:
                    break;
            }
        }
        return set;
    }
}
