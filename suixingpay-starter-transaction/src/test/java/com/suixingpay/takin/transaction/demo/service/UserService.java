package com.suixingpay.takin.transaction.demo.service;

import com.suixingpay.takin.transaction.demo.to.User;

/**
 * TODO
 * 
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2017年10月31日 下午1:35:09
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2017年10月31日 下午1:35:09
 */
public interface UserService {

    User getUserById(Long id);

    void addUser(User user);

    void updateUser(User user);

    void deleteUser(Long id);

}
