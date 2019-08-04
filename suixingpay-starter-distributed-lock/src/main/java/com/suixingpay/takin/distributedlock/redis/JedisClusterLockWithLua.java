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

import redis.clients.jedis.JedisCluster;

/**
 * 基于JedisCluster + Lua 实现分布式锁
 * 
 * @author: jiayu.qiu[qiu_jy@suixingpay.com]
 * @date: 2017年7月19日 下午10:58:32
 * @version: V1.0
 * @review: jiayu.qiu[qiu_jy@suixingpay.com]/2017年7月19日 下午10:58:32
 */
public class JedisClusterLockWithLua extends AbstractRedisLockWithLua {

    private JedisCluster jedisCluster;

    public JedisClusterLockWithLua(JedisCluster jedisCluster) {
        this.jedisCluster = jedisCluster;
    }

    @Override
    protected Long eval(byte[] lockScript, String key, List<byte[]> args) throws UnsupportedEncodingException {
        List<byte[]> keys = new ArrayList<byte[]>();
        keys.add(key.getBytes(CHAR_SET));
        return (Long) jedisCluster.eval(lockScript, keys, args);
    }

    @Override
    protected void del(String key) {
        jedisCluster.del(key);
    }

}
