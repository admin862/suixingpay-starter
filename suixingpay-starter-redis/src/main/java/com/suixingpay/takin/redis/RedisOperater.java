/**  
 * All rights Reserved, Designed By Suixingpay.
 * @author: qiujiayu[qiu_jy@suixingpay.com] 
 * @date: 2018年1月29日 上午9:43:20   
 * @Copyright ©2018 Suixingpay. All rights reserved. 
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.takin.redis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.data.redis.core.DefaultTypedTuple;
import org.springframework.data.redis.core.ZSetOperations.TypedTuple;

import com.suixingpay.takin.redis.autoconfigure.RedisProperties;
import com.suixingpay.takin.redis.command.RedisBinaryCommand;
import com.suixingpay.takin.serializer.ISerializer;

import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Tuple;

/**
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2018年1月29日 上午9:43:20
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2018年1月29日 上午9:43:20
 */
@Slf4j
public class RedisOperater implements IRedisOperater {

    protected final RedisProperties config;

    protected final ISerializer serializer;

    private final RedisBinaryCommand redisCommand;

    public RedisOperater(RedisProperties config, ISerializer serializer, RedisBinaryCommand redisCommand) {
        this.config = config;
        this.serializer = serializer;
        this.redisCommand = redisCommand;
    }

    @Override
    public Boolean exists(String key) {
        return this.doCommand(key, (k, jedis) -> {
            return jedis.exists(k);
        });
    }

    @Override
    public void set(String key, Object value) {
        String res = this.doCommand(key, (k, jedis) -> {
            return jedis.set(k, serializer.serialize(value));
        });
        if (!OK.equalsIgnoreCase(res)) {
            throw new RuntimeException(res);
        }
    }

    @Override
    public void setex(String key, Object value, int expire) {
        String res = this.doCommand(key, (k, jedis) -> {
            return jedis.setex(k, expire, serializer.serialize(value));
        });
        if (!OK.equalsIgnoreCase(res)) {
            throw new RuntimeException(res);
        }
    }

    @Override
    public boolean setnx(String key, Object value, int expire) {
        Long res = this.doCommand(key, (k, jedis) -> {
            return jedis.setnx(k, serializer.serialize(value));
        });

        boolean rv = res.intValue() == 1;
        if (rv && expire > 0) {
            res = this.doCommand(key, (k, jedis) -> {
                return jedis.expire(k, expire);
            });
            rv = res.intValue() == 1;
        }
        return rv;
    }

    @Override
    public Object get(String key) {
        return this.doCommand(key, (k, jedis) -> {
            return serializer.deserialize(jedis.get(k));
        });
    }

    @Override
    public Object getSet(String key, Object value) {
        return this.doCommand(key, (k, jedis) -> {
            return serializer.deserialize(jedis.getSet(k, serializer.serialize(value)));
        });
    }

    @Override
    public Long delete(String key) {
        return this.doCommand(key, (k, jedis) -> {
            return jedis.del(k);
        });
    }

    @Override
    public boolean expire(String key, int expire) {
        return this.doCommand(key, (k, jedis) -> {
            return jedis.expire(k, expire).intValue() == 1;
        });
    }

    @Override
    public long incr(String key) {
        return this.doCommand(key, (k, jedis) -> {
            return jedis.incr(k);
        });
    }

    @Override
    public long incrBy(String key, int increment) {
        return this.doCommand(key, (k, jedis) -> {
            return jedis.incrBy(k, increment);
        });
    }

    @Override
    public long decr(String key) {
        return this.doCommand(key, (k, jedis) -> {
            return jedis.decr(k);
        });
    }

    @Override
    public long decrBy(String key, int decrement) {
        return this.doCommand(key, (k, jedis) -> {
            return jedis.decrBy(k, decrement);
        });
    }

    @Override
    public Boolean hexists(String key, Object field) {
        return this.doCommand(key, (k, jedis) -> {
            return jedis.hexists(k, field(field));
        });
    }

    @Override
    public long hset(String key, Object field, Object value) {
        return this.doCommand(key, (k, jedis) -> {
            return jedis.hset(k, field(field), serializer.serialize(value));
        });
    }

    @Override
    public boolean hsetnx(String key, Object field, Object value) {
        return this.doCommand(key, (k, jedis) -> {
            return jedis.hsetnx(k, field(field), serializer.serialize(value)).intValue() == 1;
        });
    }

    @Override
    public void hmset(String key, Map<Object, Object> fieldValues) {
        Map<byte[], byte[]> hash = new HashMap<byte[], byte[]>(fieldValues.size());
        Iterator<Map.Entry<Object, Object>> it = fieldValues.entrySet().iterator();
        try {
            while (it.hasNext()) {
                Map.Entry<Object, Object> item = it.next();
                hash.put(field(item.getKey()), serializer.serialize(item.getValue()));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        String res = this.doCommand(key, (k, jedis) -> {
            return jedis.hmset(k, hash);
        });
        if (!OK.equalsIgnoreCase(res)) {
            throw new RuntimeException(res);
        }
    }

    @Override
    public Object hget(String key, Object field) {
        return this.doCommand(key, (k, jedis) -> {
            return serializer.deserialize(jedis.hget(k, field(field)));
        });
    }

    @Override
    public Map<String, Object> hgetAll(String key) {
        Map<byte[], byte[]> map = this.doCommand(key, (k, jedis) -> {
            return jedis.hgetAll(k);
        });
        Map<String, Object> res = null;
        if (null != map) {
            res = new HashMap<>(map.size());
            Iterator<Map.Entry<byte[], byte[]>> it = map.entrySet().iterator();
            Map.Entry<byte[], byte[]> item;
            try {
                while (it.hasNext()) {
                    item = it.next();
                    res.put(new String(item.getKey()), serializer.deserialize(item.getValue()));
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return res;
    }

    @Override
    public List<Object> hmget(String key, Object... field) {
        if (null == field || field.length == 0) {
            throw new IllegalArgumentException("缺field参数");
        }
        byte[][] fields = new byte[field.length][];
        for (int i = 0; i < field.length; i++) {
            fields[i] = field(field[i]);
        }
        List<byte[]> list = this.doCommand(key, (k, jedis) -> {
            return jedis.hmget(k, fields);
        });
        List<Object> res = null;
        if (null != list) {
            res = new ArrayList<>(list.size());
            try {
                for (byte[] data : list) {
                    res.add(serializer.deserialize(data));
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return res;
    }

    @Override
    public long hdel(String key, Object... field) {
        if (null == field || field.length == 0) {
            throw new IllegalArgumentException("缺field参数");
        }
        byte[][] fields = new byte[field.length][];
        for (int i = 0; i < field.length; i++) {
            fields[i] = field(field[i]);
        }
        return this.doCommand(key, (k, jedis) -> {
            return jedis.hdel(k, fields);
        });
    }

    @Override
    public long hincrBy(String key, Object field, int increment) {
        return this.doCommand(key, (k, jedis) -> {
            return jedis.hincrBy(k, field(field), increment);
        });
    }

    @Override
    public long zdd(String key, double score, Object member) {
        return this.doCommand(key, (k, jedis) -> {
            return jedis.zadd(k, score, serializer.serialize(member));
        });
    }

    @Override
    public long zcount(String key, double min, double max) {
        return this.doCommand(key, (k, jedis) -> {
            return jedis.zcount(k, min, max);
        });
    }

    @Override
    public Set<Object> zrange(String key, long start, long end) {
        Set<byte[]> set = this.doCommand(key, (k, jedis) -> {
            return jedis.zrange(k, start, end);
        });
        return convertToLinkedHashSet(set);
    }

    @Override
    public Set<TypedTuple<Object>> zrangeWithScores(String key, long start, long end) {
        Set<Tuple> set = this.doCommand(key, (k, jedis) -> {
            return jedis.zrangeWithScores(k, start, end);
        });
        Set<TypedTuple<Object>> res = null;
        if (null != set) {
            res = new LinkedHashSet<>(set.size());
            TypedTuple<Object> tmp;
            for (Tuple s : set) {
                try {
                    tmp = new DefaultTypedTuple<Object>(serializer.deserialize(s.getBinaryElement()), s.getScore());
                    res.add(tmp);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return res;
    }

    @Override
    public Set<Object> zrangeByScore(String key, double start, double stop) {
        Set<byte[]> set = this.doCommand(key, (k, jedis) -> {
            return jedis.zrangeByScore(k, start, stop);
        });
        return convertToLinkedHashSet(set);
    }

    @Override
    public Set<TypedTuple<Object>> zrangeByScoreWithScores(String key, double start, double stop) {
        Set<Tuple> set = this.doCommand(key, (k, jedis) -> {
            return jedis.zrangeByScoreWithScores(k, start, stop);
        });
        Set<TypedTuple<Object>> res = null;
        if (null != set) {
            res = new LinkedHashSet<>(set.size());
            TypedTuple<Object> tmp;
            for (Tuple s : set) {
                try {
                    tmp = new DefaultTypedTuple<Object>(serializer.deserialize(s.getBinaryElement()), s.getScore());
                    res.add(tmp);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return res;
    }

    @Override
    public long zrem(String key, Object... member) {
        if (null == member || member.length == 0) {
            throw new IllegalArgumentException("缺member参数");
        }
        byte[][] members = new byte[member.length][];
        try {
            for (int i = 0; i < member.length; i++) {
                members[i] = serializer.serialize(member[i]);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return this.doCommand(key, (k, jedis) -> {
            return jedis.zrem(k, members);
        });
    }

    @Override
    public Object eval(byte[] script, String key, Object... arg) {
        final List<byte[]> args;
        if (null != arg && arg.length > 0) {
            args = new ArrayList<>(arg.length);
            try {
                for (int i = 0; i < arg.length; i++) {
                    args.add(String.valueOf(arg[i]).getBytes(KEY_CHAR_SET));
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else {
            args = null;
        }
        return this.doCommand(key, (k, jedis) -> {
            List<byte[]> keys = new ArrayList<>(1);
            keys.add(k);
            return jedis.eval(script, keys, args);
        });
    }

    @Override
    public Object evalSha(byte[] sha1, String key, Object... arg) {
        final List<byte[]> args;
        if (null != arg && arg.length > 0) {
            args = new ArrayList<>(arg.length);
            try {
                for (int i = 0; i < arg.length; i++) {
                    args.add(String.valueOf(arg[i]).getBytes(KEY_CHAR_SET));
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else {
            args = null;
        }
        return this.doCommand(key, (k, jedis) -> {
            List<byte[]> keys = new ArrayList<>(1);
            keys.add(k);
            return jedis.evalsha(sha1, keys, args);
        });
    }

    @Override
    public byte[] scriptLoad(byte[] script, String key) {
        return this.doCommand(key, (k, jedis) -> {
            return jedis.scriptLoad(script, k);
        });
    }

    @Override
    public long sadd(String key, Object... member) {
        if (null == member || member.length == 0) {
            throw new IllegalArgumentException("缺member参数");
        }
        byte[][] members = new byte[member.length][];
        try {
            for (int i = 0; i < member.length; i++) {
                members[i] = serializer.serialize(member[i]);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return this.doCommand(key, (k, jedis) -> {
            return jedis.sadd(k, members);
        });
    }

    @Override
    public long scard(String key) {
        return this.doCommand(key, (k, jedis) -> {
            return jedis.scard(k);
        });
    }

    @Override
    public boolean sisMember(String key, Object member) {
        return this.doCommand(key, (k, jedis) -> {
            return jedis.sismember(k, serializer.serialize(member));
        });
    }

    @Override
    public Set<Object> smembers(String key) {
        Set<byte[]> vals = this.doCommand(key, (k, jedis) -> {
            return jedis.smembers(k);
        });
        Set<Object> res = null;
        if (null != vals) {
            res = new HashSet<>(vals.size());
            for (byte[] val : vals) {
                try {
                    res.add(serializer.deserialize(val));
                } catch (Exception e) {
                    throw new RuntimeException(e.getMessage());
                }
            }
        }
        return res;
    }

    @Override
    public Object spop(String key) {
        byte[] val = this.doCommand(key, (k, jedis) -> {
            return jedis.spop(k);
        });
        try {
            return serializer.deserialize(val);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public List<Object> srandMember(String key, int count) {
        List<byte[]> vals = this.doCommand(key, (k, jedis) -> {
            return jedis.srandmember(k, count);
        });
        return convertToObjectList(vals);
    }

    @Override
    public Object srandMember(String key) {
        byte[] val = this.doCommand(key, (k, jedis) -> {
            return jedis.srandmember(k);
        });
        try {
            return serializer.deserialize(val);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public long srem(String key, Object... member) {
        if (null == member || member.length == 0) {
            throw new IllegalArgumentException("缺member参数");
        }
        byte[][] members = new byte[member.length][];
        try {
            for (int i = 0; i < member.length; i++) {
                members[i] = serializer.serialize(member[i]);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return this.doCommand(key, (k, jedis) -> {
            return jedis.srem(k, members);
        });
    }

    public List<Object> convertToObjectList(List<byte[]> vals) {
        List<Object> res = null;
        if (null != vals) {
            res = new ArrayList<>(vals.size());
            for (byte[] val : vals) {
                try {
                    res.add(serializer.deserialize(val));
                } catch (Exception e) {
                    throw new RuntimeException(e.getMessage());
                }
            }
        }
        return res;
    }

    public Set<Object> convertToLinkedHashSet(Set<byte[]> vals) {
        Set<Object> res = null;
        if (null != vals) {
            res = new LinkedHashSet<>(vals.size());
            for (byte[] val : vals) {
                try {
                    res.add(serializer.deserialize(val));
                } catch (Exception e) {
                    throw new RuntimeException(e.getMessage());
                }
            }
        }
        return res;
    }

    @Override
    public String getNamespace() {
        String namespace = "";
        if (config.isNamespaceEnable()) {
            namespace = config.getNamespace();
        }
        if (null != namespace && namespace.trim().length() > 0) {
            namespace = namespace + ".";
        } else {
            namespace = "";
        }
        return namespace;
    }

    private <T> T doCommand(String key, RedisCallback<T> callback) {
        long begin = System.currentTimeMillis();
        String fullKey = key(key);
        try {
            return callback.callback(fullKey.getBytes(KEY_CHAR_SET), redisCommand);
        } catch (Throwable ex) {
            throw new RuntimeException(ex);
        } finally {
            if (config.getSlowLogSlowerThan() > 0
                    && (System.currentTimeMillis() - begin) > config.getSlowLogSlowerThan()) {
                long useTime = System.currentTimeMillis() - begin;
                log.error("execute redis command for key '{}' use time {}ms", fullKey, useTime);
            }
        }
    }

    @FunctionalInterface
    static interface RedisCallback<T> {
        /**
         * 处理回调
         * 
         * @param key
         * @param redisCommand
         * @return
         * @throws Exception
         */
        T callback(byte[] key, RedisBinaryCommand redisCommand) throws Exception;
    }

}
