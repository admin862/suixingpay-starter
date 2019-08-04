/**  
 * All rights Reserved, Designed By Suixingpay.
 * @author: qiujiayu[qiu_jy@suixingpay.com] 
 * @date: 2017年12月27日 下午2:16:34   
 * @Copyright ©2017 Suixingpay. All rights reserved. 
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.takin.token.auth;

import javax.servlet.http.HttpServletRequest;

import com.suixingpay.takin.token.TokenInfo;
import com.suixingpay.takin.token.annotation.PreAuthorize;

/**
 * 权限检查器，判断是否有权限
 * 
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2017年12月27日 下午2:16:34
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2017年12月27日 下午2:16:34
 */
public interface AuthenticationChecker {

    boolean hasRole(PreAuthorize authorize, TokenInfo tokenInfo);

    boolean hasRole(HttpServletRequest request, TokenInfo tokenInfo);
}
