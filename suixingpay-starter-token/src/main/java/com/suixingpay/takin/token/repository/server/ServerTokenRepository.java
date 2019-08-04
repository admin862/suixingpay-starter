/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2017年9月12日 下午10:51:21
 * @Copyright ©2017 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.takin.token.repository.server;

import com.suixingpay.takin.token.TokenInfo;

/**
 * token信息服务端仓库
 *
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2017年9月12日 下午10:51:21
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2017年9月12日 下午10:51:21
 */
public interface ServerTokenRepository<T extends TokenInfo> {

    /**
     * 根据token获取登录信息
     *
     * @param token
     * @return
     */
    T get(String token);

    /**
     * 设置token
     *
     * @param token
     * @param value
     * @param timeout 单位秒
     */
    void set(String token, T value, int timeout);

    /**
     * delete
     *
     * @param token
     * @return
     */
    boolean del(String token);

    /**
     * @param token
     * @param timeout
     */
    void expire(String token, int timeout);
}
