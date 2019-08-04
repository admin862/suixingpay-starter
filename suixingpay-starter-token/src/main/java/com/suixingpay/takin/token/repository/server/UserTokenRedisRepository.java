/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: tangqihua[tang_qh@suixingpay.com]
 * @date: 2018年03月09日 15时22分
 * @Copyright ©2018 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.takin.token.repository.server;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;

import com.suixingpay.takin.redis.IRedisOperater;

/**
 * 使用Redis存储用户登录token
 * 
 * @author: tangqihua[tang_qh@suixingpay.com]
 * @date: 2018年03月09日 15时22分
 * @version: V1.0
 * @review: tangqihua[tang_qh@suixingpay.com]/2018年03月09日 15时22分
 */
public class UserTokenRedisRepository implements UserTokenRepository {

    private static final String KEY_PREFIX = ":user-token:";

    private String toKey(String userId) {
        return KEY_PREFIX + userId;
    }

    @Autowired
    private final IRedisOperater redisOperater;

    public UserTokenRedisRepository(IRedisOperater redisOperater) {
        this.redisOperater = redisOperater;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Set<String> getAll(String userId) {
        String key = toKey(userId);
        @SuppressWarnings("rawtypes")
        Set tokens = redisOperater.zrange(key, 0, -1);
        return (Set<String>) tokens;
    }

    @Override
    public void addToken(String userId, String token, int timeout) {
        if (null == userId || null == token) {
            return;
        }
        String key = toKey(userId);
        redisOperater.zdd(key, System.currentTimeMillis() / 1000, token);
        redisOperater.expire(key, timeout);
    }

    @Override
    public boolean removeToken(String userId, String token) {
        if (null == userId || null == token) {
            return false;
        }
        String key = toKey(userId);
        long srem = redisOperater.zrem(key, token);
        return 1 == srem;
    }

    @Override
    public String getSet(String userId, String token, int timeout) {
        if (null == userId || null == token) {
            return null;
        }
        String key = toKey(userId);
        String old = (String) redisOperater.getSet(key, token);
        redisOperater.expire(key, timeout);
        return old;
    }

    @Override
    public boolean del(String userId) {
        if (null == userId) {
            return false;
        }
        String key = toKey(userId);
        Long delete = redisOperater.delete(key);
        return null != delete && delete.intValue() > 0;
    }

    @Override
    public long removeTokens(String userId, Set<String> tokens) {
        if (null == userId || null == tokens || tokens.size() == 0) {
            return -1L;
        }
        Object[] arr = new String[tokens.size()];
        arr = tokens.toArray(arr);
        String key = toKey(userId);
        return redisOperater.zrem(key, arr);
    }

    @Override
    public String getSingleModeToken(String userId) {
        if (null == userId) {
            return null;
        }
        String key = toKey(userId);
        return (String) redisOperater.get(key);
    }
}
