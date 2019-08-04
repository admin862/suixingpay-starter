/**  
 * All rights Reserved, Designed By Suixingpay.
 * @author: qiujiayu[qiu_jy@suixingpay.com] 
 * @date: 2017年12月27日 上午11:07:05   
 * @Copyright ©2017 Suixingpay. All rights reserved. 
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.takin.token.repository.client;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.suixingpay.takin.token.TokenProperties;
import com.suixingpay.takin.token.TokenProperties.Cookie;
import com.suixingpay.takin.token.util.CookieUtil;

/**
 * 使用cookie保存token
 * 
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2017年12月27日 上午11:07:05
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2017年12月27日 上午11:07:05
 */
public class CookieRepository implements TokenClientRepository {

    private final TokenProperties tokenProperties;

    public CookieRepository(TokenProperties tokenProperties) {
        this.tokenProperties = tokenProperties;
    }

    @Override
    public String get(HttpServletRequest request, String tokenName) {
        return CookieUtil.getCookieValue(request, tokenName);
    }

    @Override
    public void set(HttpServletResponse response, String tokenName, String token) {
        String cookieName = tokenName;
        String cookieValue = token;
        Cookie cookie = tokenProperties.getCookie();
        CookieUtil.addCookie(response, cookieName, cookieValue, cookie.getDomain(), cookie.isHttpOnly(),
                cookie.getMaxAge(), cookie.getPath(), cookie.isSecure());
    }

    @Override
    public void del(HttpServletRequest request, HttpServletResponse response, String tokenName) {
        String cookieName = tokenName;
        CookieUtil.delCookie(request, response, cookieName);
    }

}
