/**  
 * All rights Reserved, Designed By Suixingpay.
 * @author: qiujiayu[qiu_jy@suixingpay.com] 
 * @date: 2017年9月13日 上午9:14:09   
 * @Copyright ©2017 Suixingpay. All rights reserved. 
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.takin.token.repository.server;

import com.suixingpay.takin.redis.IRedisOperater;
import com.suixingpay.takin.token.TokenInfo;

/**
 * Redis Repository
 * 
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2017年9月13日 上午9:14:09
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2017年9月13日 上午9:14:09
 */
public class ServerTokenRedisRepository<T extends TokenInfo> implements ServerTokenRepository<T> {

    private final IRedisOperater redisOperater;

    public ServerTokenRedisRepository(IRedisOperater redisOperater) {
        this.redisOperater = redisOperater;
    }

    @Override
    @SuppressWarnings("unchecked")
    public T get(String token) {
        return (T) redisOperater.get(token);
    }

    @Override
    public void set(String token, T value, int timeout) {
        redisOperater.setex(token, value, timeout);
    }

    @Override
    public boolean del(String token) {
        Long res = redisOperater.delete(token);
        return null != res && res.intValue() > 0;
    }

    @Override
    public void expire(String token, int timeout) {
        redisOperater.expire(token, timeout);
    }
}
