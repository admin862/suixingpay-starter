/**  
 * All rights Reserved, Designed By Suixingpay.
 * @author: jiayu.qiu[qiu_jy@suixingpay.com] 
 * @date: 2017年7月19日 下午10:58:32   
 * @Copyright ©2017 Suixingpay. All rights reserved. 
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.takin.distributedlock.redis;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisConnectionUtils;

import redis.clients.jedis.Jedis;

/**
 * 基于JedisConnectionFactory + Lua 实现分布式锁
 * 
 * @author: jiayu.qiu[qiu_jy@suixingpay.com]
 * @date: 2017年7月19日 下午10:58:32
 * @version: V1.0
 * @review: jiayu.qiu[qiu_jy@suixingpay.com]/2017年7月19日 下午10:58:32
 */
public class SpringJedisLockWithLua extends AbstractRedisLockWithLua {

    private static final Logger logger = LoggerFactory.getLogger(SpringJedisLockWithLua.class);

    private final JedisConnectionFactory redisConnectionFactory;

    public SpringJedisLockWithLua(JedisConnectionFactory redisConnectionFactory) {
        this.redisConnectionFactory = redisConnectionFactory;
    }

    @Override
    protected Long eval(byte[] lockScript, String key, List<byte[]> args) throws UnsupportedEncodingException {
        if (null == redisConnectionFactory || null == key || key.length() == 0) {
            return -1L;
        }
        RedisConnection redisConnection = RedisConnectionUtils.getConnection(redisConnectionFactory);
        try {
            Jedis jedis = (Jedis) redisConnection.getNativeConnection();

            List<byte[]> keys = new ArrayList<byte[]>();
            keys.add(key.getBytes(CHAR_SET));
            return (Long) jedis.eval(lockScript, keys, args);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        } finally {
            RedisConnectionUtils.releaseConnection(redisConnection, redisConnectionFactory);
        }
        return 0L;
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

}
