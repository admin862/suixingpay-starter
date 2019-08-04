/**  
 * All rights Reserved, Designed By Suixingpay.
 * @author: jiayu.qiu[qiu_jy@suixingpay.com] 
 * @date: 2017年7月19日 下午10:58:32   
 * @Copyright ©2017 Suixingpay. All rights reserved. 
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.takin.cache.redis;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisConnectionUtils;

import com.jarvis.cache.ICacheManager;
import com.jarvis.cache.exception.CacheCenterConnectionException;
import com.jarvis.cache.serializer.ISerializer;
import com.jarvis.cache.serializer.StringSerializer;
import com.jarvis.cache.to.CacheKeyTO;
import com.jarvis.cache.to.CacheWrapper;

import redis.clients.jedis.Client;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;

/**
 * 基于 JedisConnectionFactory 实现Redis缓存操作
 * 
 * @author: jiayu.qiu[qiu_jy@suixingpay.com]
 * @date: 2017年7月19日 下午10:58:32
 * @version: V1.0
 * @review: jiayu.qiu[qiu_jy@suixingpay.com]/2017年7月19日 下午10:58:32
 */
public class SpringJedisCacheManager implements ICacheManager {

    private static final Logger logger = LoggerFactory.getLogger(SpringJedisCacheManager.class);

    private static final StringSerializer KEY_SERIALIZER = new StringSerializer();

    private final ISerializer<Object> serializer;

    private JedisConnectionFactory redisConnectionFactory;

    /**
     * Hash的缓存时长：等于0时永久缓存；大于0时，主要是为了防止一些已经不用的缓存占用内存;hashExpire小于0时，则使用@Cache中设置的expire值（默认值为-1）。
     */
    private int hashExpire = -1;

    /**
     * 是否通过脚本来设置 Hash的缓存时长
     */
    private boolean hashExpireByScript = false;

    public SpringJedisCacheManager(ISerializer<Object> serializer) {
        this.serializer = serializer;
    }

    public JedisConnectionFactory getRedisConnectionFactory() {
        return redisConnectionFactory;
    }

    public void setRedisConnectionFactory(JedisConnectionFactory redisConnectionFactory) {
        this.redisConnectionFactory = redisConnectionFactory;
    }

    @Override
    public void setCache(final CacheKeyTO cacheKeyTO, final CacheWrapper<Object> result, final Method method,
            final Object args[]) throws CacheCenterConnectionException {
        if (null == redisConnectionFactory || null == cacheKeyTO) {
            return;
        }
        String cacheKey = cacheKeyTO.getCacheKey();
        if (null == cacheKey || cacheKey.length() == 0) {
            return;
        }
        RedisConnection redisConnection = RedisConnectionUtils.getConnection(redisConnectionFactory);
        try {
            Jedis jedis = (Jedis) redisConnection.getNativeConnection();
            int expire = result.getExpire();
            String hfield = cacheKeyTO.getHfield();
            if (null == hfield || hfield.length() == 0) {
                if (expire == 0) {
                    jedis.set(KEY_SERIALIZER.serialize(cacheKey), serializer.serialize(result));
                } else if (expire > 0) {
                    jedis.setex(KEY_SERIALIZER.serialize(cacheKey), expire, serializer.serialize(result));
                }
            } else {
                hashSet(jedis, cacheKey, hfield, result);
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        } finally {
            RedisConnectionUtils.releaseConnection(redisConnection, redisConnectionFactory);
        }
    }

    private static byte[] hashSetScript;

    static {
        try {
            String tmpScript = "redis.call('HSET', KEYS[1], KEYS[2], ARGV[1]);\nredis.call('EXPIRE', KEYS[1], tonumber(ARGV[2]));";
            hashSetScript = tmpScript.getBytes("UTF-8");
        } catch (UnsupportedEncodingException ex) {
            logger.error(ex.getMessage(), ex);
        }
    }

    private static final Map<String, byte[]> HASH_SET_SCRIPT_SHA = new ConcurrentHashMap<>(16);

    private void hashSet(Jedis jedis, String cacheKey, String hfield, CacheWrapper<Object> result) throws Exception {
        byte[] key = KEY_SERIALIZER.serialize(cacheKey);
        byte[] field = KEY_SERIALIZER.serialize(hfield);
        byte[] val = serializer.serialize(result);
        int hExpire;
        if (hashExpire < 0) {
            hExpire = result.getExpire();
        } else {
            hExpire = hashExpire;
        }
        if (hExpire == 0) {
            jedis.hset(key, field, val);
        } else if (hExpire > 0) {
            if (hashExpireByScript) {
                String redis = jedis.getClient().getHost() + ":" + jedis.getClient().getPort() + ":"
                        + jedis.getClient().getDB();
                byte[] sha = HASH_SET_SCRIPT_SHA.get(redis);
                if (null == sha) {
                    sha = jedis.scriptLoad(hashSetScript);
                    HASH_SET_SCRIPT_SHA.put(redis, sha);
                }
                List<byte[]> keys = new ArrayList<byte[]>();
                keys.add(key);
                keys.add(field);

                List<byte[]> args = new ArrayList<byte[]>();
                args.add(val);
                args.add(KEY_SERIALIZER.serialize(String.valueOf(hExpire)));
                try {
                    jedis.evalsha(sha, keys, args);
                } catch (Exception ex) {
                    logger.error(ex.getMessage(), ex);
                    try {
                        sha = jedis.scriptLoad(hashSetScript);
                        HASH_SET_SCRIPT_SHA.put(redis, sha);
                        jedis.evalsha(sha, keys, args);
                    } catch (Exception ex1) {
                        logger.error(ex1.getMessage(), ex1);
                    }
                }
            } else {
                Pipeline p = jedis.pipelined();
                p.hset(key, field, val);
                p.expire(key, hExpire);
                p.sync();
            }
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public CacheWrapper<Object> get(final CacheKeyTO cacheKeyTO, final Method method, final Object args[])
            throws CacheCenterConnectionException {
        if (null == redisConnectionFactory || null == cacheKeyTO) {
            return null;
        }
        String cacheKey = cacheKeyTO.getCacheKey();
        if (null == cacheKey || cacheKey.length() == 0) {
            return null;
        }
        CacheWrapper<Object> res = null;
        RedisConnection redisConnection = RedisConnectionUtils.getConnection(redisConnectionFactory);
        try {
            Jedis jedis = (Jedis) redisConnection.getNativeConnection();
            byte bytes[] = null;
            String hfield = cacheKeyTO.getHfield();
            if (null == hfield || hfield.length() == 0) {
                bytes = jedis.get(KEY_SERIALIZER.serialize(cacheKey));
            } else {
                bytes = jedis.hget(KEY_SERIALIZER.serialize(cacheKey), KEY_SERIALIZER.serialize(hfield));
            }
            Type returnType = method.getGenericReturnType();
            res = (CacheWrapper<Object>) serializer.deserialize(bytes, returnType);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        } finally {
            RedisConnectionUtils.releaseConnection(redisConnection, redisConnectionFactory);
        }
        return res;
    }

    /**
     * 根据缓存Key删除缓存
     * 
     * @param cacheKeyTO 缓存Key
     */
    @Override
    public void delete(CacheKeyTO cacheKeyTO) throws CacheCenterConnectionException {
        if (null == redisConnectionFactory || null == cacheKeyTO) {
            return;
        }
        String cacheKey = cacheKeyTO.getCacheKey();
        if (null == cacheKey || cacheKey.length() == 0) {
            return;
        }
        logger.debug("delete cache:" + cacheKey);
        RedisConnection redisConnection = RedisConnectionUtils.getConnection(redisConnectionFactory);
        try {
            Jedis jedis = (Jedis) redisConnection.getNativeConnection();
            if ("*".equals(cacheKey)) {
                jedis.flushDB();
            } else if (cacheKey.indexOf("*") != -1) {
                // 如果传进来的值中 带有 * 或 ? 号，则会使用批量删除（遍历所有Redis服务器）,性能非常差，不建议使用这种方法。
                // 建议使用 hash表方缓存需要批量删除的数据。
                batchDel(jedis, cacheKey);
            } else {
                String hfield = cacheKeyTO.getHfield();
                if (null == hfield || hfield.length() == 0) {
                    jedis.del(KEY_SERIALIZER.serialize(cacheKey));
                } else {
                    jedis.hdel(KEY_SERIALIZER.serialize(cacheKey), KEY_SERIALIZER.serialize(hfield));
                }
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        } finally {
            RedisConnectionUtils.releaseConnection(redisConnection, redisConnectionFactory);
        }
    }

    private static byte[] delScript;

    static {
        StringBuilder tmp = new StringBuilder();
        tmp.append("local keys = redis.call('keys', KEYS[1]);\n");
        tmp.append("if(not keys or #keys == 0) then \n return nil; \n end \n");
        tmp.append("redis.call('del', unpack(keys)); \n return keys;");
        try {
            delScript = tmp.toString().getBytes("UTF-8");
        } catch (UnsupportedEncodingException ex) {
            logger.error(ex.getMessage(), ex);
        }
    }

    private static final Map<String, byte[]> DEL_SCRIPT_SHA = new ConcurrentHashMap<>(16);

    private void batchDel(Jedis jedis, String cacheKey) throws Exception {
        Client client = jedis.getClient();
        String redis = client.getHost() + ":" + client.getPort() + ":" + client.getDB();
        byte[] sha = DEL_SCRIPT_SHA.get(redis);
        byte[] key = KEY_SERIALIZER.serialize(cacheKey);
        if (null == sha) {
            sha = jedis.scriptLoad(delScript);
            DEL_SCRIPT_SHA.put(redis, sha);
        }
        try {
            @SuppressWarnings("unchecked")
            List<String> keys = (List<String>) jedis.evalsha(sha, 1, key);
            if (null != keys && keys.size() > 0) {
                /*
                 * for(String tmpKey: keys) {
                 * autoLoadHandler.resetAutoLoadLastLoadTime(tmpKey); }
                 */
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            try {
                sha = jedis.scriptLoad(delScript);
                DEL_SCRIPT_SHA.put(redis, sha);
                @SuppressWarnings("unchecked")
                List<String> keys = (List<String>) jedis.evalsha(sha, 1, key);
                if (null != keys && keys.size() > 0) {
                    /*
                     * for(String tmpKey: keys) {
                     * autoLoadHandler.resetAutoLoadLastLoadTime(tmpKey); }
                     */
                }
            } catch (Exception ex1) {
                logger.error(ex1.getMessage(), ex1);
            }
        }
    }

    public int getHashExpire() {
        return hashExpire;
    }

    public void setHashExpire(int hashExpire) {
        if (hashExpire < 0) {
            return;
        }
        this.hashExpire = hashExpire;
    }

    public boolean isHashExpireByScript() {
        return hashExpireByScript;
    }

    public void setHashExpireByScript(boolean hashExpireByScript) {
        this.hashExpireByScript = hashExpireByScript;
    }

}
