package com.suixingpay.takin.redis.command;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisConnectionUtils;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Tuple;

public class JedisBinaryCommand implements RedisBinaryCommand {

    private final JedisConnectionFactory redisConnectionFactory;

    public JedisBinaryCommand(JedisConnectionFactory redisConnectionFactory) {
        this.redisConnectionFactory = redisConnectionFactory;
    }

    private RedisConnection getRedisConnection() {
        return RedisConnectionUtils.getConnection(redisConnectionFactory);
    }

    private void releaseConnection(RedisConnection redisConnection) {
        RedisConnectionUtils.releaseConnection(redisConnection, redisConnectionFactory);
    }

    @Override
    public Boolean exists(byte[] key) {
        RedisConnection redisConnection = null;
        try {
            redisConnection = getRedisConnection();
            return ((Jedis) redisConnection.getNativeConnection()).exists(key);
        } finally {
            releaseConnection(redisConnection);
        }
    }

    @Override
    public String set(byte[] key, byte[] value) {
        RedisConnection redisConnection = null;
        try {
            redisConnection = getRedisConnection();
            return ((Jedis) redisConnection.getNativeConnection()).set(key, value);
        } finally {
            releaseConnection(redisConnection);
        }
    }

    @Override
    public String setex(byte[] key, int expire, byte[] value) {
        RedisConnection redisConnection = null;
        try {
            redisConnection = getRedisConnection();
            return ((Jedis) redisConnection.getNativeConnection()).setex(key, expire, value);
        } finally {
            releaseConnection(redisConnection);
        }
    }

    @Override
    public Long setnx(byte[] key, byte[] value) {
        RedisConnection redisConnection = null;
        try {
            redisConnection = getRedisConnection();
            return ((Jedis) redisConnection.getNativeConnection()).setnx(key, value);
        } finally {
            releaseConnection(redisConnection);
        }
    }

    @Override
    public byte[] get(byte[] key) {
        RedisConnection redisConnection = null;
        try {
            redisConnection = getRedisConnection();
            return ((Jedis) redisConnection.getNativeConnection()).get(key);
        } finally {
            releaseConnection(redisConnection);
        }
    }

    @Override
    public byte[] getSet(byte[] key, byte[] value) {
        RedisConnection redisConnection = null;
        try {
            redisConnection = getRedisConnection();
            return ((Jedis) redisConnection.getNativeConnection()).getSet(key, value);
        } finally {
            releaseConnection(redisConnection);
        }
    }

    @Override
    public Long del(byte[] key) {
        RedisConnection redisConnection = null;
        try {
            redisConnection = getRedisConnection();
            return ((Jedis) redisConnection.getNativeConnection()).del(key);
        } finally {
            releaseConnection(redisConnection);
        }
    }

    @Override
    public Long expire(byte[] key, int expire) {
        RedisConnection redisConnection = null;
        try {
            redisConnection = getRedisConnection();
            return ((Jedis) redisConnection.getNativeConnection()).expire(key, expire);
        } finally {
            releaseConnection(redisConnection);
        }
    }

    @Override
    public Long incr(byte[] key) {
        RedisConnection redisConnection = null;
        try {
            redisConnection = getRedisConnection();
            return ((Jedis) redisConnection.getNativeConnection()).incr(key);
        } finally {
            releaseConnection(redisConnection);
        }
    }

    @Override
    public Long incrBy(byte[] key, int increment) {
        RedisConnection redisConnection = null;
        try {
            redisConnection = getRedisConnection();
            return ((Jedis) redisConnection.getNativeConnection()).incrBy(key, increment);
        } finally {
            releaseConnection(redisConnection);
        }
    }

    @Override
    public Long decr(byte[] key) {
        RedisConnection redisConnection = null;
        try {
            redisConnection = getRedisConnection();
            return ((Jedis) redisConnection.getNativeConnection()).decr(key);
        } finally {
            releaseConnection(redisConnection);
        }
    }

    @Override
    public Long decrBy(byte[] key, int decrement) {
        RedisConnection redisConnection = null;
        try {
            redisConnection = getRedisConnection();
            return ((Jedis) redisConnection.getNativeConnection()).decrBy(key, decrement);
        } finally {
            releaseConnection(redisConnection);
        }
    }

    @Override
    public Boolean hexists(byte[] key, byte[] field) {
        RedisConnection redisConnection = null;
        try {
            redisConnection = getRedisConnection();
            return ((Jedis) redisConnection.getNativeConnection()).hexists(key, field);
        } finally {
            releaseConnection(redisConnection);
        }
    }

    @Override
    public Long hset(byte[] key, byte[] field, byte[] value) {
        RedisConnection redisConnection = null;
        try {
            redisConnection = getRedisConnection();
            return ((Jedis) redisConnection.getNativeConnection()).hset(key, field, value);
        } finally {
            releaseConnection(redisConnection);
        }
    }

    @Override
    public Long hsetnx(byte[] key, byte[] field, byte[] value) {
        RedisConnection redisConnection = null;
        try {
            redisConnection = getRedisConnection();
            return ((Jedis) redisConnection.getNativeConnection()).hsetnx(key, field, value);
        } finally {
            releaseConnection(redisConnection);
        }
    }

    @Override
    public String hmset(byte[] key, Map<byte[], byte[]> fieldValues) {
        RedisConnection redisConnection = null;
        try {
            redisConnection = getRedisConnection();
            return ((Jedis) redisConnection.getNativeConnection()).hmset(key, fieldValues);
        } finally {
            releaseConnection(redisConnection);
        }
    }

    @Override
    public byte[] hget(byte[] key, byte[] field) {
        RedisConnection redisConnection = null;
        try {
            redisConnection = getRedisConnection();
            return ((Jedis) redisConnection.getNativeConnection()).hget(key, field);
        } finally {
            releaseConnection(redisConnection);
        }
    }

    @Override
    public Map<byte[], byte[]> hgetAll(byte[] key) {
        RedisConnection redisConnection = null;
        try {
            redisConnection = getRedisConnection();
            return ((Jedis) redisConnection.getNativeConnection()).hgetAll(key);
        } finally {
            releaseConnection(redisConnection);
        }
    }

    @Override
    public List<byte[]> hmget(byte[] key, byte[]... field) {
        RedisConnection redisConnection = null;
        try {
            redisConnection = getRedisConnection();
            return ((Jedis) redisConnection.getNativeConnection()).hmget(key, field);
        } finally {
            releaseConnection(redisConnection);
        }
    }

    @Override
    public Long hdel(byte[] key, byte[]... field) {
        RedisConnection redisConnection = null;
        try {
            redisConnection = getRedisConnection();
            return ((Jedis) redisConnection.getNativeConnection()).hdel(key, field);
        } finally {
            releaseConnection(redisConnection);
        }
    }

    @Override
    public Long hincrBy(byte[] key, byte[] field, int increment) {
        RedisConnection redisConnection = null;
        try {
            redisConnection = getRedisConnection();
            return ((Jedis) redisConnection.getNativeConnection()).hincrBy(key, field, increment);
        } finally {
            releaseConnection(redisConnection);
        }
    }

    @Override
    public Long zadd(byte[] key, double score, byte[] member) {
        RedisConnection redisConnection = null;
        try {
            redisConnection = getRedisConnection();
            return ((Jedis) redisConnection.getNativeConnection()).zadd(key, score, member);
        } finally {
            releaseConnection(redisConnection);
        }
    }

    @Override
    public Long zcount(byte[] key, double min, double max) {
        RedisConnection redisConnection = null;
        try {
            redisConnection = getRedisConnection();
            return ((Jedis) redisConnection.getNativeConnection()).zcount(key, min, max);
        } finally {
            releaseConnection(redisConnection);
        }
    }
    
    @Override
    public Set<byte[]> zrange(byte[] key, final long start, final long end) {
        RedisConnection redisConnection = null;
        try {
            redisConnection = getRedisConnection();
            return ((Jedis) redisConnection.getNativeConnection()).zrange(key, start, end);
        } finally {
            releaseConnection(redisConnection);
        }
    }
    
    @Override
    public Set<Tuple> zrangeWithScores(byte[] key, long start, long end) {
        RedisConnection redisConnection = null;
        try {
            redisConnection = getRedisConnection();
            return ((Jedis) redisConnection.getNativeConnection()).zrangeWithScores(key, start, end);
        } finally {
            releaseConnection(redisConnection);
        }
    }

    @Override
    public Set<byte[]> zrangeByScore(byte[] key, double start, double end) {
        RedisConnection redisConnection = null;
        try {
            redisConnection = getRedisConnection();
            return ((Jedis) redisConnection.getNativeConnection()).zrangeByScore(key, start, end);
        } finally {
            releaseConnection(redisConnection);
        }
    }

    @Override
    public Set<Tuple> zrangeByScoreWithScores(byte[] key, double start, double stop) {
        RedisConnection redisConnection = null;
        try {
            redisConnection = getRedisConnection();
            return ((Jedis) redisConnection.getNativeConnection()).zrangeByScoreWithScores(key, start, stop);
        } finally {
            releaseConnection(redisConnection);
        }
    }

    @Override
    public Long zrem(byte[] key, byte[]... member) {
        RedisConnection redisConnection = null;
        try {
            redisConnection = getRedisConnection();
            return ((Jedis) redisConnection.getNativeConnection()).zrem(key, member);
        } finally {
            releaseConnection(redisConnection);
        }
    }

    @Override
    public Object eval(byte[] script, List<byte[]> keys, List<byte[]> args) {
        RedisConnection redisConnection = null;
        try {
            redisConnection = getRedisConnection();
            return ((Jedis) redisConnection.getNativeConnection()).eval(script, keys, args);
        } finally {
            releaseConnection(redisConnection);
        }
    }

    @Override
    public Object evalsha(byte[] sha1, List<byte[]> keys, List<byte[]> args) {
        RedisConnection redisConnection = null;
        try {
            redisConnection = getRedisConnection();
            return ((Jedis) redisConnection.getNativeConnection()).evalsha(sha1, keys, args);
        } finally {
            releaseConnection(redisConnection);
        }
    }

    @Override
    public byte[] scriptLoad(byte[] script, byte[] key) {
        RedisConnection redisConnection = null;
        try {
            redisConnection = getRedisConnection();
            return ((Jedis) redisConnection.getNativeConnection()).scriptLoad(script);
        } finally {
            releaseConnection(redisConnection);
        }
    }

    @Override
    public Long sadd(byte[] key, byte[]... member) {
        RedisConnection redisConnection = null;
        try {
            redisConnection = getRedisConnection();
            return ((Jedis) redisConnection.getNativeConnection()).sadd(key, member);
        } finally {
            releaseConnection(redisConnection);
        }
    }

    @Override
    public Long scard(byte[] key) {
        RedisConnection redisConnection = null;
        try {
            redisConnection = getRedisConnection();
            return ((Jedis) redisConnection.getNativeConnection()).scard(key);
        } finally {
            releaseConnection(redisConnection);
        }
    }

    @Override
    public boolean sismember(byte[] key, byte[] member) {
        RedisConnection redisConnection = null;
        try {
            redisConnection = getRedisConnection();
            return ((Jedis) redisConnection.getNativeConnection()).sismember(key, member);
        } finally {
            releaseConnection(redisConnection);
        }
    }

    @Override
    public Set<byte[]> smembers(byte[] key) {
        RedisConnection redisConnection = null;
        try {
            redisConnection = getRedisConnection();
            return ((Jedis) redisConnection.getNativeConnection()).smembers(key);
        } finally {
            releaseConnection(redisConnection);
        }
    }

    @Override
    public byte[] spop(byte[] key) {
        RedisConnection redisConnection = null;
        try {
            redisConnection = getRedisConnection();
            return ((Jedis) redisConnection.getNativeConnection()).spop(key);
        } finally {
            releaseConnection(redisConnection);
        }
    }

    @Override
    public List<byte[]> srandmember(byte[] key, int count) {
        RedisConnection redisConnection = null;
        try {
            redisConnection = getRedisConnection();
            return ((Jedis) redisConnection.getNativeConnection()).srandmember(key, count);
        } finally {
            releaseConnection(redisConnection);
        }
    }

    @Override
    public byte[] srandmember(byte[] key) {
        RedisConnection redisConnection = null;
        try {
            redisConnection = getRedisConnection();
            return ((Jedis) redisConnection.getNativeConnection()).srandmember(key);
        } finally {
            releaseConnection(redisConnection);
        }
    }

    @Override
    public Long srem(byte[] key, byte[]... member) {
        RedisConnection redisConnection = null;
        try {
            redisConnection = getRedisConnection();
            return ((Jedis) redisConnection.getNativeConnection()).srem(key, member);
        } finally {
            releaseConnection(redisConnection);
        }
    }

}
