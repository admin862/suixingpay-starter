/**  
 * All rights Reserved, Designed By Suixingpay.
 * @author: jiayu.qiu[qiu_jy@suixingpay.com] 
 * @date: 2017年7月19日 下午10:58:32   
 * @Copyright ©2017 Suixingpay. All rights reserved. 
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.takin.distributedlock;

/**
 * 分布锁
 * 
 * @author: jiayu.qiu[qiu_jy@suixingpay.com]
 * @date: 2017年7月19日 下午10:58:32
 * @version: V1.0
 * @review: jiayu.qiu[qiu_jy@suixingpay.com]/2017年7月19日 下午10:58:32
 */
public interface IDistributedLock {

    /**
     * 获取分布式锁
     * 
     * @param key 锁Key
     * @param leaseTime
     *            锁的租约时间（单位：秒）。当前线程在获取到锁以后，在租约时间到期以后，会自动释放锁。如果在租约时间到期之前，方法执行完毕了，也会释放锁。
     * @param tryCnt 尝试次数(默认值为：0)
     * @param interval 尝试获取锁的间隔时间，单位是毫秒（默认值为：10ms）
     * @return boolean
     */
    boolean tryLock(String key, int leaseTime, int tryCnt, int interval);

    /**
     * 释放锁
     * 
     * @param key 锁Key
     */
    void release(String key);

}
