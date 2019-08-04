/**  
 * All rights Reserved, Designed By Suixingpay.
 * @author: qiujiayu[qiu_jy@suixingpay.com] 
 * @date: 2018年1月3日 下午1:33:36   
 * @Copyright ©2018 Suixingpay. All rights reserved. 
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.takin.kaptcha.repository;

import com.suixingpay.takin.redis.IRedisOperater;

/**
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2018年1月3日 下午1:33:36
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2018年1月3日 下午1:33:36
 */
public class RedisKaptchaRepository implements KaptchaRepository {

    private final IRedisOperater redisOperater;

    public RedisKaptchaRepository(IRedisOperater redisOperater) {
        this.redisOperater = redisOperater;
    }

    @Override
    public void set(String key, String value, int expire) {
        redisOperater.setex(key, value, expire);
    }

    @Override
    public String get(String key) {
        return (String) redisOperater.get(key);
    }

}
