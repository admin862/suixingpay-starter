/**  
 * All rights Reserved, Designed By Suixingpay.
 * @author: qiujiayu[qiu_jy@suixingpay.com] 
 * @date: 2017年9月12日 下午10:27:47   
 * @Copyright ©2017 Suixingpay. All rights reserved. 
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.takin.token.repository.client;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 从客户端获取token的方法,可以是从header或cookie等方式传递
 * 
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2017年9月12日 下午10:27:47
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2017年9月12日 下午10:27:47
 */
public interface TokenClientRepository {
    /**
     * 获取Token
     * 
     * @param request
     * @param tokenName
     * @return
     */
    String get(HttpServletRequest request, String tokenName);

    /**
     * @param response
     * @param tokenName
     * @param token
     */
    void set(HttpServletResponse response, String tokenName, String token);

    /**
     * @param request
     * @param response
     * @param tokenName
     */
    void del(HttpServletRequest request, HttpServletResponse response, String tokenName);
}
