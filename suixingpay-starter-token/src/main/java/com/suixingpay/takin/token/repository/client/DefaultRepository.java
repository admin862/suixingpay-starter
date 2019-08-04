/**  
 * All rights Reserved, Designed By Suixingpay.
 * @author: qiujiayu[qiu_jy@suixingpay.com] 
 * @date: 2017年12月28日 上午1:56:10   
 * @Copyright ©2017 Suixingpay. All rights reserved. 
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.takin.token.repository.client;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.suixingpay.takin.token.TokenProperties;

/**
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2017年12月28日 上午1:56:10
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2017年12月28日 上午1:56:10
 */
public class DefaultRepository implements TokenClientRepository {

    private final TokenClientRepository[] clientRepositories;

    public DefaultRepository(TokenClientRepository[] clientRepositories) {
        this.clientRepositories = clientRepositories;
        if (null == clientRepositories || clientRepositories.length == 0) {
            throw new IllegalArgumentException("请设置'" + TokenProperties.PREFIX + ".clientRepositoryTypes'");
        }
    }

    @Override
    public String get(HttpServletRequest request, String tokenName) {
        String token = null;
        for (TokenClientRepository rep : clientRepositories) {
            token = rep.get(request, tokenName);
            if (null != token && token.trim().length() > 0) {
                return token;
            }
        }
        return token;
    }

    @Override
    public void set(HttpServletResponse response, String tokenName, String value) {
        for (TokenClientRepository rep : clientRepositories) {
            rep.set(response, tokenName, value);
        }
    }

    @Override
    public void del(HttpServletRequest request, HttpServletResponse response, String tokenName) {
        for (TokenClientRepository rep : clientRepositories) {
            rep.del(request, response, tokenName);
        }
    }

}
