/**  
 * All rights Reserved, Designed By Suixingpay.
 * @author: jiayu.qiu[qiu_jy@suixingpay.com] 
 * @date: 2017年7月19日 下午10:58:32   
 * @Copyright ©2017 Suixingpay. All rights reserved. 
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.takin.distributedlock.redis;

import redis.clients.jedis.JedisCluster;

/**
 * 基于JedisCluster 实现分布式锁
 * 
 * @author: jiayu.qiu[qiu_jy@suixingpay.com]
 * @date: 2017年7月19日 下午10:58:32
 * @version: V1.0
 * @review: jiayu.qiu[qiu_jy@suixingpay.com]/2017年7月19日 下午10:58:32
 */
public class JedisClusterLock extends AbstractRedisLock {

    private JedisCluster jedisCluster;

    public JedisClusterLock(JedisCluster jedisCluster) {
        this.jedisCluster = jedisCluster;
    }

    @Override
    protected Long setnx(String key, String val) {
        return this.jedisCluster.setnx(key, val);
    }

    @Override
    protected void expire(String key, int expire) {
        this.jedisCluster.expire(key, expire);
    }

    @Override
    protected String get(String key) {
        return this.jedisCluster.get(key);
    }

    @Override
    protected String getSet(String key, String newVal) {
        return this.jedisCluster.getSet(key, newVal);
    }

    @Override
    protected void del(String key) {
        this.jedisCluster.del(key);
    }

}
