package com.suixingpay.takin.cache.demo.dao;

import org.springframework.stereotype.Component;

import com.jarvis.cache.annotation.Cache;
import com.jarvis.cache.annotation.CacheDelete;
import com.jarvis.cache.annotation.CacheDeleteKey;
import com.suixingpay.takin.cache.demo.to.User;

/**
 * TODO
 * 
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2017年10月31日 下午1:34:41
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2017年10月31日 下午1:34:41
 */
@Component
public class UserDAO {

    @Cache(expire = 60 * 20, autoload = true, key = "'user:'+#args[0]")
    public User getUserById(Integer id) {
        User user = new User();
        user.setId(id);
        user.setAge(id + 10);
        user.setName("name" + id);
        System.out.println("load data form db");
        return user;
    }

    @CacheDelete({ @CacheDeleteKey(value = "'user:'+#args[0].id") })
    public void updateUser(User user) {

    }
}
