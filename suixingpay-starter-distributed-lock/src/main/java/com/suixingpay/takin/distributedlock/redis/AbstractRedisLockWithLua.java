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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.suixingpay.takin.distributedlock.IDistributedLock;
import com.suixingpay.takin.distributedlock.pojo.RedisLockInfo;

/**
 * 基于Redis+Lua实现分布式锁; 实现方法更容易理解，但性能相对会差些
 * 
 * @author: jiayu.qiu[qiu_jy@suixingpay.com]
 * @date: 2017年7月19日 下午10:58:32
 * @version: V1.0
 * @review: jiayu.qiu[qiu_jy@suixingpay.com]/2017年7月19日 下午10:58:32
 */
public abstract class AbstractRedisLockWithLua implements IDistributedLock {

    private static final Logger logger = LoggerFactory.getLogger(AbstractRedisLockWithLua.class);

    private static final ThreadLocal<Map<String, RedisLockInfo>> LOCK_START_TIME = new ThreadLocal<Map<String, RedisLockInfo>>();

    protected static final String CHAR_SET = "UTF-8";
    /**
     * 分布式锁 KEY[1] lock key <br>
     * ARGV[1] 过期时间 ARGV[2] 缓存时长 返回值: 如果执行成功, 则返回1; 否则返回0
     */
    private static final String LOCK_SCRIPT_STR = "local lockKey= KEYS[1]\n"//
            + "local lock = redis.call('SETNX', lockKey, '1')\n" // 持锁
            + "if lock == 0 then\n" //
            + "  return 0\n" //
            + "end\n" //
            + "redis.call('EXPIRE', lockKey, tonumber(ARGV[1]))\n" // 持锁n秒
            + "return 1\n";

    private static byte[] lockScript;

    static {
        try {
            lockScript = LOCK_SCRIPT_STR.getBytes(CHAR_SET);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    /**
     * 
     * TODO
     * @param lockScript
     * @param key
     * @param args
     * @return
     * @throws UnsupportedEncodingException
     */
    protected abstract Long eval(byte[] lockScript, String key, List<byte[]> args) throws UnsupportedEncodingException;

    /**
     * 
     * TODO
     * @param key
     */
    protected abstract void del(String key);

    @Override
    public boolean tryLock(String key, int lockExpire, int tryCnt, int interval) {
        if (interval <= 0) {
            interval = 10;
        }
        boolean locked = false;
        for (int i = 0; !(locked = getLock(key, lockExpire)) && i < tryCnt; i++) {
            try {
                Thread.sleep(interval);
            } catch (InterruptedException e) {
                logger.error(e.getMessage(), e);
            }
        }
        if (locked) {
            Map<String, RedisLockInfo> startTimeMap = LOCK_START_TIME.get();
            if (null == startTimeMap) {
                startTimeMap = new HashMap<String, RedisLockInfo>(8);
                LOCK_START_TIME.set(startTimeMap);
            }
            RedisLockInfo info = new RedisLockInfo();
            info.setLeaseTime(lockExpire * 1000);
            info.setStartTime(System.currentTimeMillis());
            startTimeMap.put(key, info);
        }
        return locked;
    }

    private boolean getLock(String key, int lockExpire) {
        try {
            List<byte[]> args = new ArrayList<byte[]>();
            args.add(String.valueOf(lockExpire).getBytes(CHAR_SET));

            Long rv = eval(lockScript, key, args);
            return null != rv && rv.intValue() == 1;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return false;
    }

    @Override
    public void release(String key) {
        Map<String, RedisLockInfo> startTimeMap = LOCK_START_TIME.get();
        RedisLockInfo info = null;
        if (null != startTimeMap) {
            info = startTimeMap.remove(key);
        }
        if (null != info && (System.currentTimeMillis() - info.getStartTime()) >= info.getLeaseTime()) {
            return;
        }
        try {
            del(key);
        } catch (Throwable e) {
            logger.error(e.getMessage(), e);
        }
    }
}
