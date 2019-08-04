/**  
 * All rights Reserved, Designed By Suixingpay.
 * @author: qiujiayu[qiu_jy@suixingpay.com] 
 * @date: 2017年9月13日 上午9:25:16   
 * @Copyright ©2017 Suixingpay. All rights reserved. 
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.takin.token.repository.client;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 通过http 参数传递token
 * 
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2017年9月13日 上午9:25:16
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2017年9月13日 上午9:25:16
 */
public class ParameterRepository implements TokenClientRepository {

    @Override
    public String get(HttpServletRequest request, String tokenName) {
        return request.getParameter(tokenName);
    }

    @Override
    public void set(HttpServletResponse response, String tokenName, String value) {
    }

    @Override
    public void del(HttpServletRequest request, HttpServletResponse response, String tokenName) {
    }

}
