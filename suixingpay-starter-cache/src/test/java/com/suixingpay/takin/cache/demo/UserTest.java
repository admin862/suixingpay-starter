/**  
 * All rights Reserved, Designed By Suixingpay.
 * @author: qiujiayu[qiu_jy@suixingpay.com] 
 * @date: 2017年8月15日 上午11:52:22   
 * @Copyright ©2017 Suixingpay. All rights reserved. 
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.takin.cache.demo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.suixingpay.takin.cache.demo.mapper.UserMapper;
import com.suixingpay.takin.cache.demo.to.User;
import com.suixingpay.takin.cache.demo.type.Sex;
import com.suixingpay.takin.cache.demo.type.State;

/**
 * TODO
 * 
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2017年8月15日 上午11:52:22
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2017年8月15日 上午11:52:22
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("dev")
public class UserTest {
    @Autowired
    private UserMapper userMapper;

    @Test
    public void testAddUser() {
        User user = new User();
        user.setAge(100);
        user.setName("李四14");
        user.setSex(Sex.MALE);
        user.setState(State.NORMAL);
        userMapper.addUser(user);
    }

    @Test
    public void testGetUser() {
        User user = userMapper.getById(2);
        System.out.println(user);
    }
}
