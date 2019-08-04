/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2017年9月12日 下午11:42:04
 * @Copyright ©2017 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.takin.token;

import java.io.Serializable;

import com.suixingpay.takin.token.auth.AuthenticationChecker;

import lombok.Getter;
import lombok.Setter;

/**
 * 用户登录信息
 *
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2017年9月12日 下午11:42:04
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2017年9月12日 下午11:42:04
 */
@Getter
@Setter
public abstract class TokenInfo implements Serializable {

    private static final long serialVersionUID = -3962706622653140628L;

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 创建时间:单位毫秒
     */
    private long createTime;

    /**
     * 如果权限比较少的情况下，把权限信息保存在登录信息中， 如果权限比较多，请实现{@link AuthenticationChecker}进行验证
     *
     * @return
     */
    public abstract String[] getRoles();
}
