/**  
 * All rights Reserved, Designed By Suixingpay.
 * @author: qiujiayu[qiu_jy@suixingpay.com] 
 * @date: 2017年8月22日 下午1:12:24   
 * @Copyright ©2017 Suixingpay. All rights reserved. 
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.takin.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.RedisClusterConnection;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnection;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;

import com.suixingpay.takin.redis.autoconfigure.RedisOperaterAutoConfigure;
import com.suixingpay.takin.redis.autoconfigure.RedisProperties;
import com.suixingpay.takin.redis.command.JedisBinaryCommand;
import com.suixingpay.takin.redis.command.JedisClusterBinaryCommand;
import com.suixingpay.takin.redis.command.RedisBinaryCommand;
import com.suixingpay.takin.serializer.ISerializer;

import redis.clients.jedis.JedisCluster;

/**
 * 工厂类
 * 
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2017年8月22日 下午1:12:24
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2017年8月22日 下午1:12:24
 */
public class RedisOperaterFactory {
    private static final Logger logger = LoggerFactory.getLogger(RedisOperaterAutoConfigure.class);

    /**
     * 创建IRedisOperater
     * 
     * @param namespace 命名空间，可以为空
     * @param redisISerializer 序列化工具
     * @param connectionFactory redis
     * @return
     */
    public static IRedisOperater create(RedisProperties config, ISerializer redisISerializer,
            RedisConnectionFactory connectionFactory) {
        if (null == connectionFactory) {
            return null;
        }
        if (!(connectionFactory instanceof JedisConnectionFactory)) {
            logger.debug("connectionFactory is not JedisConnectionFactory");
            return null;
        }

        RedisConnection redisConnection = null;
        try {
            redisConnection = connectionFactory.getConnection();
        } catch (Throwable e) {
            logger.error(e.getMessage(), e);
        }
        RedisBinaryCommand redisBinaryCommand = null;
        if (null != redisConnection) {
            if (redisConnection instanceof RedisClusterConnection) {
                RedisClusterConnection redisClusterConnection = (RedisClusterConnection) redisConnection;
                // 优先使用JedisCluster
                JedisCluster jedisCluster = null;
                jedisCluster = (JedisCluster) redisClusterConnection.getNativeConnection();
                if (null != jedisCluster) {
                    redisBinaryCommand = new JedisClusterBinaryCommand(jedisCluster);
                    logger.debug("IRedisOperater with JedisClusterBinaryCommand");
                }
            } else if (redisConnection instanceof JedisConnection) {
                redisBinaryCommand = new JedisBinaryCommand((JedisConnectionFactory) connectionFactory);
                logger.debug("IRedisOperater with RedisOperater");
            }
            return new RedisOperater(config, redisISerializer, redisBinaryCommand);
        }
        return null;
    }
}
