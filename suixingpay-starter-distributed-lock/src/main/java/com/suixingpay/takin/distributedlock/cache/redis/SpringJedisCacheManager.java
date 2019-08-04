/**  
 * All rights Reserved, Designed By Suixingpay.
 * @author: jiayu.qiu[qiu_jy@suixingpay.com] 
 * @date: 2017年7月19日 下午10:58:32   
 * @Copyright ©2017 Suixingpay. All rights reserved. 
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.takin.distributedlock.cache.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisConnectionUtils;

import com.suixingpay.takin.distributedlock.cache.ICacheManager;
import com.suixingpay.takin.serializer.ISerializer;

import redis.clients.jedis.Jedis;

/**
 * 基于JedisConnectionFactory 实现Redis缓存操作
 * 
 * @author: jiayu.qiu[qiu_jy@suixingpay.com]
 * @date: 2017年7月19日 下午10:58:32
 * @version: V1.0
 * @review: jiayu.qiu[qiu_jy@suixingpay.com]/2017年7月19日 下午10:58:32
 */
public class SpringJedisCacheManager implements ICacheManager {

    private static final Logger logger = LoggerFactory.getLogger(SpringJedisCacheManager.class);

    private final JedisConnectionFactory redisConnectionFactory;

    private final ISerializer serializer;

    public SpringJedisCacheManager(JedisConnectionFactory redisConnectionFactory, ISerializer serializer) {
        this.redisConnectionFactory = redisConnectionFactory;
        this.serializer = serializer;
    }

    @Override
    public void setCache(String cacheKey, Object result, int expire) throws Exception {
        if (null == redisConnectionFactory || null == cacheKey || cacheKey.length() == 0 || expire < 0) {
            return;
        }
        RedisConnection redisConnection = RedisConnectionUtils.getConnection(redisConnectionFactory);
        try {
            Jedis jedis = (Jedis) redisConnection.getNativeConnection();
            if (expire == 0) {
                jedis.set(cacheKey.getBytes(KEY_CHAR_SET), serializer.serialize(result));
            } else if (expire > 0) {
                jedis.setex(cacheKey.getBytes(KEY_CHAR_SET), expire, serializer.serialize(result));
            }
        } catch (Exception ex) {
            throw ex;
        } finally {
            RedisConnectionUtils.releaseConnection(redisConnection, redisConnectionFactory);
        }
    }

    @Override
    public Object get(String cacheKey) throws Exception {
        if (null == redisConnectionFactory || null == cacheKey || cacheKey.length() == 0) {
            return null;
        }
        RedisConnection redisConnection = RedisConnectionUtils.getConnection(redisConnectionFactory);
        try {
            Jedis jedis = (Jedis) redisConnection.getNativeConnection();
            return serializer.deserialize(jedis.get(cacheKey.getBytes(KEY_CHAR_SET)));
        } catch (Exception ex) {
            throw ex;
        } finally {
            RedisConnectionUtils.releaseConnection(redisConnection, redisConnectionFactory);
        }
    }

    @Override
    public void delete(String cacheKey) throws Exception {
        if (null == redisConnectionFactory || null == cacheKey || cacheKey.length() == 0) {
            return;
        }
        logger.debug("delete cache:" + cacheKey);
        RedisConnection redisConnection = RedisConnectionUtils.getConnection(redisConnectionFactory);
        try {
            Jedis jedis = (Jedis) redisConnection.getNativeConnection();
            jedis.del(cacheKey.getBytes(KEY_CHAR_SET));
        } catch (Exception ex) {
            throw ex;
        } finally {
            RedisConnectionUtils.releaseConnection(redisConnection, redisConnectionFactory);
        }
    }

    public JedisConnectionFactory getRedisConnectionFactory() {
        return redisConnectionFactory;
    }
}
