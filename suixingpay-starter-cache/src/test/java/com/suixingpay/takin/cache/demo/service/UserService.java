package com.suixingpay.takin.cache.demo.service;

import com.suixingpay.takin.cache.demo.to.User;

/**
 * TODO
 * 
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2017年10月31日 下午1:35:09
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2017年10月31日 下午1:35:09
 */
public interface UserService {

    /**
     * TODO
     * 
     * @param id
     * @return
     */
    User getUserById(Integer id);

    /**
     * TODO
     * 
     * @param id
     * @return
     */
    User getUserById2(Integer id);

    /**
     * TODO
     * 
     * @param user
     */
    void updateUser(User user);

    /**
     * TODO
     * 
     * @param user
     */
    void updateUser2(User user);
}
