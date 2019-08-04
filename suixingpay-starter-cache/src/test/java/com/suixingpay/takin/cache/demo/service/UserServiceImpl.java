package com.suixingpay.takin.cache.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jarvis.cache.annotation.Cache;
import com.jarvis.cache.annotation.CacheDeleteTransactional;
import com.suixingpay.takin.cache.demo.dao.UserDAO;
import com.suixingpay.takin.cache.demo.to.User;

/**
 * TODO
 * 
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2017年10月31日 下午1:35:17
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2017年10月31日 下午1:35:17
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDAO userDAO;

    @Override
    public User getUserById(Integer id) {
        // 测试Mybatis
        // System.out.println(clientUpgradeMapper.getById(83));
        System.out.println("---------------------------->>>>");
        return userDAO.getUserById(id);
    }

    @Override
    @Cache(expire = 60 * 20, autoload = true, key = "'user2'+#args[0]")
    public User getUserById2(Integer id) {
        System.out.println("get user by id2");
        User user = new User();
        user.setId(id);
        user.setName("name1");
        return user;
    }

    @Override
    public void updateUser(User user) {
        userDAO.updateUser(user);
    }

    @Override
    @CacheDeleteTransactional
    public void updateUser2(User user) {
        userDAO.updateUser(user);
    }

}
