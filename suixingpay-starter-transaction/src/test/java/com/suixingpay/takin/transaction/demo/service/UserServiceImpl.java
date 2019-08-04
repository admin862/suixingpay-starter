package com.suixingpay.takin.transaction.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.suixingpay.takin.transaction.demo.mapper.UserMapper;
import com.suixingpay.takin.transaction.demo.to.User;

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
    private UserMapper userMapper;

    @Override
    public User getUserById(Long id) {
        return userMapper.getById(id);
    }

    @Override
    public void addUser(User user) {
        userMapper.addUser(user);
        throw new RuntimeException("事务测试");
    }

    @Override
    public void updateUser(User user) {
        userMapper.updateUser(user);
    }

    @Override
    public void deleteUser(Long id) {
        userMapper.deleteUser(id);
    }

}
