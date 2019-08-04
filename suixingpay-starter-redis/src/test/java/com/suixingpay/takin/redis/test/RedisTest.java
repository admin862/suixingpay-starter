/**  
 * All rights Reserved, Designed By Suixingpay.
 * @author: qiujiayu[qiu_jy@suixingpay.com] 
 * @date: 2017年8月22日 上午10:48:25   
 * @Copyright ©2017 Suixingpay. All rights reserved. 
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.takin.redis.test;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.suixingpay.takin.redis.IRedisOperater;

/**
 * TODO
 * 
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2017年8月22日 上午10:48:25
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2017年8月22日 上午10:48:25
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisTest {

    @Autowired
    private IRedisOperater redis;

    String key = "redis-test-k1";
    String val = "aaaaaaaaaaaaaaa";
    
    @Test 
    public void testHgetAll() {
        String hkey="aaaa";
        Map<Object, Object> map=new HashMap<>();
        map.put("1", "1");
        map.put("2", "2");
        redis.hmset(hkey, map);
        Map<String, Object> res= redis.hgetAll(hkey);
        res.entrySet().stream().forEach(item->{
            System.out.println("-->"+item.getValue());
        });
    }

    @Test
    public void testGet() {
        Object waiter = new Object();
        Thread thread1 = new Thread(new Runnable() {

            @Override
            public void run() {
                redis.setex(key, val, 10);
                try {
                    synchronized (waiter) {
                        waiter.notifyAll();
                    }
                } catch (Exception e) {
                }
                System.out.println("thread1 finished!");
            }
        });
        Thread thread2 = new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    synchronized (waiter) {
                        waiter.wait();
                    }
                    Object r = redis.get(key);
                    Assert.assertEquals(val, r);
                    System.out.println("thread2 finished!" + r);
                } catch (Exception e) {
                }
            }
        });
        thread2.start();
        thread1.start();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
