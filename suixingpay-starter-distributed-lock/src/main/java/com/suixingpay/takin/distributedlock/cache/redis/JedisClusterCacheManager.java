/**  
 * All rights Reserved, Designed By Suixingpay.
 * @author: jiayu.qiu[qiu_jy@suixingpay.com] 
 * @date: 2017年7月19日 下午10:58:32   
 * @Copyright ©2017 Suixingpay. All rights reserved. 
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.takin.distributedlock.cache.redis;

import com.suixingpay.takin.distributedlock.cache.ICacheManager;
import com.suixingpay.takin.serializer.ISerializer;

import redis.clients.jedis.JedisCluster;

/**
 * 基于JedisCluster 实现Redis缓存操作
 * 
 * @author: jiayu.qiu[qiu_jy@suixingpay.com]
 * @date: 2017年7月19日 下午10:58:32
 * @version: V1.0
 * @review: jiayu.qiu[qiu_jy@suixingpay.com]/2017年7月19日 下午10:58:32
 */
public class JedisClusterCacheManager implements ICacheManager {

    private final JedisCluster jedisCluster;

    private final ISerializer serializer;

    public JedisClusterCacheManager(JedisCluster jedisCluster, ISerializer serializer) {
        this.jedisCluster = jedisCluster;
        this.serializer = serializer;
    }

    @Override
    public void setCache(String cacheKey, Object result, int expire) throws Exception {
        if (null == jedisCluster || null == cacheKey || cacheKey.length() == 0 || expire < 0) {
            return;
        }
        if (expire == 0) {
            jedisCluster.set(cacheKey.getBytes(KEY_CHAR_SET), serializer.serialize(result));
        } else if (expire > 0) {
            jedisCluster.setex(cacheKey.getBytes(KEY_CHAR_SET), expire, serializer.serialize(result));
        }
    }

    @Override
    public Object get(String cacheKey) throws Exception {
        if (null == jedisCluster || null == cacheKey || cacheKey.length() == 0) {
            return null;
        }
        return serializer.deserialize(jedisCluster.get(cacheKey.getBytes(KEY_CHAR_SET)));
    }

    @Override
    public void delete(String key) throws Exception {
        jedisCluster.del(key.getBytes(KEY_CHAR_SET));
    }

    public JedisCluster getJedisCluster() {
        return jedisCluster;
    }

}
