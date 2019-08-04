/**  
 * All rights Reserved, Designed By Suixingpay.
 * @author: qiujiayu[qiu_jy@suixingpay.com] 
 * @date: 2018年1月3日 下午1:29:06   
 * @Copyright ©2018 Suixingpay. All rights reserved. 
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.takin.kaptcha.repository;

/**
 * 验证码仓储
 * 
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2018年1月3日 下午1:29:06
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2018年1月3日 下午1:29:06
 */
public interface KaptchaRepository {

    /**
     * 往仓储写数据(会覆写旧值)
     * 
     * @param key 验证码key
     * @param value 验证码
     * @param expire 缓存时长，单位秒
     */
    void set(final String key, final String value, final int expire);

    /**
     * 根据验证码key获得验证码
     * 
     * @param key 验证码key
     * @return 验证码
     */
    String get(final String key);
}
