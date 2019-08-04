package com.suixingpay.takin.redis.command;

import java.util.List;
import java.util.Map;
import java.util.Set;

import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.Tuple;

public class JedisClusterBinaryCommand implements RedisBinaryCommand {

    private final JedisCluster jedisCluster;

    public JedisClusterBinaryCommand(JedisCluster jedisCluster) {
        this.jedisCluster = jedisCluster;
    }

    @Override
    public Boolean exists(byte[] key) {
        return jedisCluster.exists(key);
    }

    @Override
    public String set(byte[] key, byte[] value) {
        return jedisCluster.set(key, value);
    }

    @Override
    public String setex(byte[] key, int expire, byte[] value) {
        return jedisCluster.setex(key, expire, value);
    }

    @Override
    public Long setnx(byte[] key, byte[] value) {
        return jedisCluster.setnx(key, value);
    }

    @Override
    public byte[] get(byte[] key) {
        return jedisCluster.get(key);
    }

    @Override
    public byte[] getSet(byte[] key, byte[] value) {
        return jedisCluster.getSet(key, value);
    }

    @Override
    public Long del(byte[] key) {
        return jedisCluster.del(key);
    }

    @Override
    public Long expire(byte[] key, int expire) {
        return jedisCluster.expire(key, expire);
    }

    @Override
    public Long incr(byte[] key) {
        return jedisCluster.incr(key);
    }

    @Override
    public Long incrBy(byte[] key, int increment) {
        return jedisCluster.incrBy(key, increment);
    }

    @Override
    public Long decr(byte[] key) {
        return jedisCluster.decr(key);
    }

    @Override
    public Long decrBy(byte[] key, int decrement) {
        return jedisCluster.decrBy(key, decrement);
    }

    @Override
    public Boolean hexists(byte[] key, byte[] field) {
        return jedisCluster.hexists(key, field);
    }

    @Override
    public Long hset(byte[] key, byte[] field, byte[] value) {
        return jedisCluster.hset(key, field, value);
    }

    @Override
    public Long hsetnx(byte[] key, byte[] field, byte[] value) {
        return jedisCluster.hsetnx(key, field, value);
    }

    @Override
    public String hmset(byte[] key, Map<byte[], byte[]> fieldValues) {
        return jedisCluster.hmset(key, fieldValues);
    }

    @Override
    public byte[] hget(byte[] key, byte[] field) {
        return jedisCluster.hget(key, field);
    }

    @Override
    public Map<byte[], byte[]> hgetAll(byte[] key) {
        return jedisCluster.hgetAll(key);
    }

    @Override
    public List<byte[]> hmget(byte[] key, byte[]... field) {
        return jedisCluster.hmget(key, field);
    }

    @Override
    public Long hdel(byte[] key, byte[]... field) {
        return jedisCluster.hdel(key, field);
    }

    @Override
    public Long hincrBy(byte[] key, byte[] field, int increment) {
        return jedisCluster.hincrBy(key, field, increment);
    }

    @Override
    public Long zadd(byte[] key, double score, byte[] member) {
        return jedisCluster.zadd(key, score, member);
    }

    @Override
    public Long zcount(byte[] key, double min, double max) {
        return jedisCluster.zcount(key, min, max);
    }

    @Override
    public Set<byte[]> zrange(byte[] key, final long start, final long end) {
        return jedisCluster.zrange(key, start, end);
    }

    @Override
    public Set<Tuple> zrangeWithScores(byte[] key, long start, long end) {
        return jedisCluster.zrangeWithScores(key, start, end);
    }

    @Override
    public Set<byte[]> zrangeByScore(byte[] key, double start, double end) {
        return jedisCluster.zrangeByScore(key, start, end);
    }

    @Override
    public Set<Tuple> zrangeByScoreWithScores(byte[] key, double start, double stop) {
        return jedisCluster.zrangeByScoreWithScores(key, start, stop);
    }

    @Override
    public Long zrem(byte[] key, byte[]... member) {
        return jedisCluster.zrem(key, member);
    }

    @Override
    public Object eval(byte[] script, List<byte[]> keys, List<byte[]> args) {
        return jedisCluster.eval(script, keys, args);
    }

    @Override
    public Object evalsha(byte[] sha1, List<byte[]> keys, List<byte[]> args) {
        return jedisCluster.evalsha(sha1, keys, args);
    }

    @Override
    public byte[] scriptLoad(byte[] script, byte[] key) {
        return jedisCluster.scriptLoad(script, key);
    }

    @Override
    public Long sadd(byte[] key, byte[]... member) {
        return jedisCluster.sadd(key, member);
    }

    @Override
    public Long scard(byte[] key) {
        return jedisCluster.scard(key);
    }

    @Override
    public boolean sismember(byte[] key, byte[] member) {
        return jedisCluster.sismember(key, member);
    }

    @Override
    public Set<byte[]> smembers(byte[] key) {
        return jedisCluster.smembers(key);
    }

    @Override
    public byte[] spop(byte[] key) {
        return jedisCluster.spop(key);
    }

    @Override
    public List<byte[]> srandmember(byte[] key, int count) {
        return jedisCluster.srandmember(key, count);
    }

    @Override
    public byte[] srandmember(byte[] key) {
        return jedisCluster.srandmember(key);
    }

    @Override
    public Long srem(byte[] key, byte[]... member) {
        return jedisCluster.srem(key, member);
    }

}
