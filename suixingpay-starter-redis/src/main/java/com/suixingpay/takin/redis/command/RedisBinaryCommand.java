package com.suixingpay.takin.redis.command;

import java.util.List;
import java.util.Map;
import java.util.Set;

import redis.clients.jedis.Tuple;

/**
 * 
 * Reids 二进制操作 <br/>
 * 为了兼容集群方式，只实现单个key的操作
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2018年3月9日 上午11:39:33
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2018年3月9日 上午11:39:33
 */
public interface RedisBinaryCommand {

    Boolean exists(byte[] key);

    String set(byte[] key, byte[] value);

    String setex(byte[] key, int expire, byte[] value);

    Long setnx(byte[] key, byte[] value);

    byte[] get(byte[] key);

    byte[] getSet(byte[] key, byte[] value);

    Long del(byte[] key);

    Long expire(byte[] key, int expire);

    Long incr(byte[] key);

    Long incrBy(byte[] key, int increment);

    Long decr(byte[] key);

    Long decrBy(byte[] key, int decrement);

    Boolean hexists(byte[] key, byte[] field);

    Long hset(byte[] key, byte[] field, byte[] value);

    Long hsetnx(byte[] key, byte[] field, byte[] value);

    String hmset(byte[] key, Map<byte[], byte[]> fieldValues);

    byte[] hget(byte[] key, byte[] field);

    Map<byte[], byte[]> hgetAll(byte[] key);

    List<byte[]> hmget(byte[] key, byte[]... field);

    Long hdel(byte[] key, byte[]... field);

    Long hincrBy(byte[] key, byte[] field, int increment);

    Long zadd(byte[] key, double score, byte[] member);

    Long zcount(byte[] key, double min, double max);
    
    Set<byte[]> zrange(byte[] key, final long start, final long end);
    
    Set<Tuple> zrangeWithScores(byte[] key, long start, long end);

    Set<byte[]> zrangeByScore(byte[] key, double start, double stop);

    Set<Tuple> zrangeByScoreWithScores(byte[] key, double start, double stop);

    Long zrem(byte[] key, byte[]... member);

    Object eval(byte[] script, List<byte[]> keys, List<byte[]> args);

    Object evalsha(byte[] sha1, List<byte[]> keys, List<byte[]> args);

    byte[] scriptLoad(byte[] script, byte[] key);

    Long sadd(byte[] key, byte[]... member);

    Long scard(byte[] key);

    boolean sismember(byte[] key, byte[] member);

    Set<byte[]> smembers(byte[] key);

    byte[] spop(byte[] key);

    List<byte[]> srandmember(byte[] key, int count);

    byte[] srandmember(byte[] key);

    Long srem(byte[] key, byte[]... member);

}
