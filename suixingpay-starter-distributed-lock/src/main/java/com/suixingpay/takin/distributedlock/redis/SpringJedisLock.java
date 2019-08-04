/**  
 * All rights Reserved, Designed By Suixingpay.
 * @author: jiayu.qiu[qiu_jy@suixingpay.com] 
 * @date: 2017年7月19日 下午10:58:32   
 * @Copyright ©2017 Suixingpay. All rights reserved. 
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.takin.distributedlock.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisConnectionUtils;

import redis.clients.jedis.Jedis;

/**
 * 基于JedisConnectionFactory 实现分布式锁
 * 
 * @author: jiayu.qiu[qiu_jy@suixingpay.com]
 * @date: 2017年7月19日 下午10:58:32
 * @version: V1.0
 * @review: jiayu.qiu[qiu_jy@suixingpay.com]/2017年7月19日 下午10:58:32
 */
public class SpringJedisLock extends AbstractRedisLock {
    private static final Logger logger = LoggerFactory.getLogger(SpringJedisLock.class);

    private final JedisConnectionFactory redisConnectionFactory;

    public SpringJedisLock(JedisConnectionFactory redisConnectionFactory) {
        this.redisConnectionFactory = redisConnectionFactory;
    }

    @Override
    protected Long setnx(String key, String val) {
        if (null == redisConnectionFactory || null == key || key.length() == 0) {
            return -1L;
        }
        RedisConnection redisConnection = RedisConnectionUtils.getConnection(redisConnectionFactory);
        try {
            Jedis jedis = (Jedis) redisConnection.getNativeConnection();
            return jedis.setnx(key, val);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        } finally {
            RedisConnectionUtils.releaseConnection(redisConnection, redisConnectionFactory);
        }
        return 0L;
    }

    @Override
    protected void expire(String key, int expire) {
        if (null == redisConnectionFactory || null == key || key.length() == 0 || expire < 0) {
            return;
        }
        RedisConnection redisConnection = RedisConnectionUtils.getConnection(redisConnectionFactory);
        try {
            Jedis jedis = (Jedis) redisConnection.getNativeConnection();
            jedis.expire(key, expire);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        } finally {
            RedisConnectionUtils.releaseConnection(redisConnection, redisConnectionFactory);
        }

    }

    @Override
    protected String get(String key) {
        if (null == redisConnectionFactory || null == key || key.length() == 0) {
            return null;
        }
        RedisConnection redisConnection = RedisConnectionUtils.getConnection(redisConnectionFactory);
        try {
            Jedis jedis = (Jedis) redisConnection.getNativeConnection();
            return jedis.get(key);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        } finally {
            RedisConnectionUtils.releaseConnection(redisConnection, redisConnectionFactory);
        }
        return null;
    }

    @Override
    protected String getSet(String key, String newVal) {
        if (null == redisConnectionFactory || null == key || key.length() == 0) {
            return null;
        }
        RedisConnection redisConnection = RedisConnectionUtils.getConnection(redisConnectionFactory);
        try {
            Jedis jedis = (Jedis) redisConnection.getNativeConnection();
            return jedis.getSet(key, newVal);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        } finally {
            RedisConnectionUtils.releaseConnection(redisConnection, redisConnectionFactory);
        }
        return null;
    }

    @Override
    protected void del(String key) {
        if (null == redisConnectionFactory || null == key || key.length() == 0) {
            return;
        }
        RedisConnection redisConnection = RedisConnectionUtils.getConnection(redisConnectionFactory);
        try {
            Jedis jedis = (Jedis) redisConnection.getNativeConnection();
            jedis.del(key);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        } finally {
            RedisConnectionUtils.releaseConnection(redisConnection, redisConnectionFactory);
        }
    }

    public JedisConnectionFactory getRedisConnectionFactory() {
        return redisConnectionFactory;
    }

}
