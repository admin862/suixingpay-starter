/**  
 * All rights Reserved, Designed By Suixingpay.
 * @author: qiujiayu[qiu_jy@suixingpay.com] 
 * @date: 2017年8月9日 上午10:11:29   
 * @Copyright ©2017 Suixingpay. All rights reserved. 
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.takin.distributedlock.demo.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.suixingpay.takin.distributedlock.IDistributedLock;

/**
 * TODO
 * 
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2017年8月9日 上午10:11:29
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2017年8月9日 上午10:11:29
 */
@RestController
public class LockController {
    @Autowired
    private IDistributedLock distributedLock;

    @GetMapping("/lock")
    public void lock() {
        String key = "test";
        distributedLock.tryLock(key, 5, 0, 10);
        System.out.println("test");
        distributedLock.release(key);
    }
}
