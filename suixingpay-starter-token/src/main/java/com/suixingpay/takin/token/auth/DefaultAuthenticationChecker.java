/**  
 * All rights Reserved, Designed By Suixingpay.
 * @author: qiujiayu[qiu_jy@suixingpay.com] 
 * @date: 2017年12月27日 下午2:23:08   
 * @Copyright ©2017 Suixingpay. All rights reserved. 
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.takin.token.auth;

import javax.servlet.http.HttpServletRequest;

import com.suixingpay.takin.token.TokenInfo;
import com.suixingpay.takin.token.annotation.PreAuthorize;

/**
 * 权限检查的默认实现
 * 
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2017年12月27日 下午2:23:08
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2017年12月27日 下午2:23:08
 */
public class DefaultAuthenticationChecker implements AuthenticationChecker {

    @Override
    public boolean hasRole(PreAuthorize authorize, TokenInfo tokenInfo) {
        if (null == authorize) {
            return true;
        }
        if (null == tokenInfo) {
            return false;
        }
        String[] auths = authorize.value();
        if (null != auths && auths.length > 0) {
            String[] roles = tokenInfo.getRoles();
            if (null == roles || roles.length == 0) {
                return false;
            }
            for (String auth : auths) {
                if (contains(roles, auth)) {
                    return true;
                }
            }
            return false;
        }
        return true;
    }

    private boolean contains(String[] arr, String item) {
        if (null != item && item.length() > 0 && null != arr && arr.length > 0) {
            for (String tmp : arr) {
                if (item.equals(tmp)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean hasRole(HttpServletRequest request, TokenInfo tokenInfo) {
        return true;
    }

}
