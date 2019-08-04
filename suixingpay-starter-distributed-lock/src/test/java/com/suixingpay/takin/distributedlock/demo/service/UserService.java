package com.suixingpay.takin.distributedlock.demo.service;

import com.suixingpay.takin.distributedlock.demo.to.User;

/**
 * 
 * TODO
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2017年10月31日 下午1:12:44
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2017年10月31日 下午1:12:44
 */
public interface UserService {

    /**
     * 
     * TODO
     * @param user
     */
    void updateUser(User user);

    /**
     * 
     * TODO
     * @param user
     * @return
     */
    boolean updateUserWithFallback(User user);

    /**
     * 
     * TODO
     * @param user
     */
    void updateUser1(User user);

}
