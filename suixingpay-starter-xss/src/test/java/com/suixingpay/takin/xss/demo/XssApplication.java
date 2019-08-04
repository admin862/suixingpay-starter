/**  
 * All rights Reserved, Designed By Suixingpay.
 * @author: qiujiayu[qiu_jy@suixingpay.com] 
 * @date: 2017年11月10日 上午10:35:09   
 * @Copyright ©2017 Suixingpay. All rights reserved. 
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */

package com.suixingpay.takin.xss.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.suixingpay.takin.xss.demo.dto.User;

import lombok.extern.slf4j.Slf4j;

/**
 * TODO
 * 
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2017年11月10日 上午10:35:09
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2017年11月10日 上午10:35:09
 */
@Slf4j
@RestController
@SpringBootApplication
public class XssApplication {

    public static void main(String[] args) {
        SpringApplication.run(XssApplication.class, args);
    }

    @GetMapping("/")
    public String index() {
        log.debug("index");
        return "Hello wolrd!";
    }

    @GetMapping("/get")
    public User get(String name) {
        User user = new User();
        user.setId(100L);
        user.setName(name);
        System.out.println("get==:" + user);
        return user;
    }

    @PostMapping("/post")
    public User update(@RequestBody User user) {
        System.out.println("update==:" + user);
        return user;
    }

    @PostMapping("/post2")
    public User update2(User user) {
        System.out.println("update==:" + user);
        return user;
    }
}
