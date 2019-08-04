package com.suixingpay.takin.distributedlock.demo.service;

import org.springframework.stereotype.Service;

import com.jarvis.cache.annotation.CacheDeleteTransactional;
import com.suixingpay.takin.distributedlock.annotation.DistributedLock;
import com.suixingpay.takin.distributedlock.demo.to.User;

/**
 * 
 * TODO
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2017年10月31日 下午1:12:55
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2017年10月31日 下午1:12:55
 */
@Service
public class UserServiceImpl implements UserService {

    /**
     * 测试分布式锁（最简单的使用）
     * 
     * @param user
     * @see com.suixingpay.takin.distributedlock.demo.service.UserService#updateUser(com.suixingpay.takin.distributedlock.demo.to.User)
     */
    @DistributedLock(key = "'update_user_lock_'+#args[0].id")
    @CacheDeleteTransactional
    // @CacheDelete({ @CacheDeleteKey({ "'user_'+#args[0].id" }) })
    @Override
    public void updateUser(User user) {
        try {
            System.out.println("updating user:" + user);
            Thread.sleep(200);
        } catch (Exception ex) {
        }
    }

    @DistributedLock(key = "'update_user_wfb_lock_'+#args[0].id", fallbackMethod = "fallback", checkBusinessFinished = true, returnLastResult = true)
    @Override
    public boolean updateUserWithFallback(User user) {
        try {
            System.out.println("updating user:" + user);
            Thread.sleep(20);
        } catch (Exception ex) {
        }
        return true;
    }

    public boolean fallback(User user) {
        return false;
    }

    @DistributedLock(key = "'update_user1_lock_'+#args[0].id", tryCnt = 10, checkBusinessFinished = true, businessFinishedMessage = "'had update user'+#args[0].id")
    @Override
    public void updateUser1(User user) {
        try {
            System.out.println("updating1 user:" + user);
            // Thread.sleep(200);
        } catch (Exception ex) {
        }
    }

}
