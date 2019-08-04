/**  
 * All rights Reserved, Designed By Suixingpay.
 * @author: jiayu.qiu[qiu_jy@suixingpay.com] 
 * @date: 2017年7月19日 下午10:58:32   
 * @Copyright ©2017 Suixingpay. All rights reserved. 
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.takin.distributedlock.redis;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.suixingpay.takin.distributedlock.IDistributedLock;
import com.suixingpay.takin.distributedlock.pojo.RedisLockInfo;

/**
 * 基于Redis实现分布式锁
 * 
 * @author: jiayu.qiu[qiu_jy@suixingpay.com]
 * @date: 2017年7月19日 下午10:58:32
 * @version: V1.0
 * @review: jiayu.qiu[qiu_jy@suixingpay.com]/2017年7月19日 下午10:58:32
 */
public abstract class AbstractRedisLock implements IDistributedLock {

    private static final Logger logger = LoggerFactory.getLogger(AbstractRedisLock.class);

    private static final ThreadLocal<Map<String, RedisLockInfo>> LOCK_START_TIME = new ThreadLocal<Map<String, RedisLockInfo>>();

    /**
     * 
     * TODO
     * @param key
     * @param val
     * @return
     */
    protected abstract Long setnx(String key, String val);

    /**
     * 
     * TODO
     * @param key
     * @param expire
     */
    protected abstract void expire(String key, int expire);

    /**
     * 
     * TODO
     * @param key
     * @return
     */
    protected abstract String get(String key);

    /**
     * 
     * TODO
     * @param key
     * @param newVal
     * @return
     */
    protected abstract String getSet(String key, String newVal);

    /**
     * 
     * TODO
     * @return
     */
    protected long serverTimeMillis() {
        return System.currentTimeMillis();
    }

    /**
     * 
     * TODO
     * @param value
     * @return
     */
    protected boolean isTimeExpired(String value) {
        return serverTimeMillis() > Long.parseLong(value);
    }

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
        long lockExpireTime = serverTimeMillis() + (lockExpire * 1000) + 1;// 锁超时时间
        String lockExpireTimeStr = String.valueOf(lockExpireTime);
        if (setnx(key, lockExpireTimeStr).intValue() == 1) {// 获取到锁
            try {
                expire(key, lockExpire);
            } catch (Throwable e) {
                logger.error(e.getMessage(), e);
            }
            return true;
        }
        String oldValue = get(key);
        // lock is expired
        if (oldValue != null && isTimeExpired(oldValue)) {
            // getset is atomic
            String oldValue2 = getSet(key, lockExpireTimeStr);
            // 但是走到这里时每个线程拿到的oldValue肯定不可能一样(因为getset是原子性的)
            // 假如拿到的oldValue依然是expired的，那么就说明拿到锁了
            if (oldValue2 != null && isTimeExpired(oldValue2)) {
                return true;
            }
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
