package com.suixingpay.takin.distributedlock.demo;

import java.lang.reflect.UndeclaredThrowableException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.suixingpay.takin.distributedlock.demo.service.UserService;
import com.suixingpay.takin.distributedlock.demo.to.User;
import com.suixingpay.takin.distributedlock.exception.BusinessFinishedException;
import com.suixingpay.takin.distributedlock.exception.DistributedLockConflictException;

/**
 * 
 * TODO
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2017年10月31日 下午1:12:35
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2017年10月31日 下午1:12:35
 */
@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = DistributedLockApplication.class)
public class DistributedLockTest {

    @Autowired
    private UserService userService;

    @Test
    public void testUpdateUser() {
        int threadCnt = 10;
        CountDownLatch countDown = new CountDownLatch(threadCnt);
        CountDownLatch countDown2 = new CountDownLatch(threadCnt);
        Thread[] threads = new Thread[threadCnt];
        AtomicInteger successCnt = new AtomicInteger();
        AtomicInteger failCnt = new AtomicInteger();
        for (int i = 0; i < threadCnt; i++) {
            threads[i] = new Thread(new Runnable() {

                @Override
                public void run() {
                    User user = new User();
                    user.setId(100L);
                    user.setName("name100");
                    user.setAge(20);
                    try {
                        System.out.println(Thread.currentThread().getName() + " is waiting ... ...");
                        countDown.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    try {
                        userService.updateUser(user);
                        successCnt.incrementAndGet();
                        System.out.println(Thread.currentThread().getName() + " is done ... ...");
                    } catch (Throwable e) {
                        failCnt.incrementAndGet();
                        if (e instanceof UndeclaredThrowableException) {
                            UndeclaredThrowableException e1 = (UndeclaredThrowableException) e;
                            e = e1.getUndeclaredThrowable();
                        }
                        if (e instanceof DistributedLockConflictException) {
                            System.out.println(Thread.currentThread().getName()
                                    + " is error ... ... DistributedLockConflictException");
                        } else if (e instanceof BusinessFinishedException) {
                            System.out.println(
                                    Thread.currentThread().getName() + " is error ... ... BusinessFinishedException");
                        } else {
                            System.out.println(Thread.currentThread().getName() + " is error ... ... ");
                        }
                    }
                    countDown2.countDown();
                }
            });
            threads[i].start();
        }
        for (int i = 0; i < threadCnt; i++) {
            countDown.countDown();
        }
        try {
            countDown2.await();
            Assert.assertEquals("successCnt not equals 1", successCnt.intValue(), 1);
            int t = threadCnt - 1;
            Assert.assertEquals("failCnt not equals " + t, failCnt.intValue(), t);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testUpdateUserFallback() {
        int threadCnt = 10;
        CountDownLatch countDown = new CountDownLatch(threadCnt);
        CountDownLatch countDown2 = new CountDownLatch(threadCnt);
        Thread[] threads = new Thread[threadCnt];
        AtomicInteger successCnt = new AtomicInteger();
        AtomicInteger failCnt = new AtomicInteger();
        for (int i = 0; i < threadCnt; i++) {
            threads[i] = new Thread(new Runnable() {

                @Override
                public void run() {
                    User user = new User();
                    user.setId(100L);
                    user.setName("name100");
                    user.setAge(20);
                    try {
                        System.out.println(Thread.currentThread().getName() + " is waiting ... ...");
                        countDown.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    try {
                        if (userService.updateUserWithFallback(user)) {
                            successCnt.incrementAndGet();
                            System.out.println(Thread.currentThread().getName() + " is done ... ...true");
                        } else {
                            failCnt.incrementAndGet();
                            System.out.println(Thread.currentThread().getName() + " is done ... ...false");
                        }

                    } catch (Throwable e) {
                        failCnt.incrementAndGet();
                        if (e instanceof UndeclaredThrowableException) {
                            UndeclaredThrowableException e1 = (UndeclaredThrowableException) e;
                            e = e1.getUndeclaredThrowable();
                        }
                        if (e instanceof DistributedLockConflictException) {
                            System.out.println(Thread.currentThread().getName()
                                    + " is error ... ... DistributedLockConflictException");
                        } else if (e instanceof BusinessFinishedException) {
                            System.out.println(
                                    Thread.currentThread().getName() + " is error ... ... BusinessFinishedException");
                        } else {
                            System.out.println(Thread.currentThread().getName() + " is error ... ... ");
                        }
                    }
                    countDown2.countDown();
                }
            });
            threads[i].start();
        }
        for (int i = 0; i < threadCnt; i++) {
            countDown.countDown();
        }
        try {
            countDown2.await();
            Assert.assertEquals("successCnt not equals 1", successCnt.intValue(), 1);
            int t = threadCnt - 1;
            Assert.assertEquals("failCnt not equals " + t, failCnt.intValue(), t);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testUpdateUser1() {
        int threadCnt = 10;
        CountDownLatch countDown = new CountDownLatch(threadCnt);
        CountDownLatch countDown2 = new CountDownLatch(threadCnt);
        Thread[] threads = new Thread[threadCnt];
        AtomicInteger successCnt = new AtomicInteger();
        AtomicInteger failCnt = new AtomicInteger();
        for (int i = 0; i < threadCnt; i++) {
            threads[i] = new Thread(new Runnable() {

                @Override
                public void run() {
                    User user = new User();
                    user.setId(100L);
                    user.setName("name100");
                    user.setAge(20);
                    try {
                        System.out.println(Thread.currentThread().getName() + " is waiting ... ...");
                        countDown.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    try {
                        userService.updateUser1(user);
                        successCnt.incrementAndGet();
                        System.out.println(Thread.currentThread().getName() + " is done ... ...");
                    } catch (Throwable e) {
                        failCnt.incrementAndGet();
                        if (e instanceof UndeclaredThrowableException) {
                            UndeclaredThrowableException e1 = (UndeclaredThrowableException) e;
                            e = e1.getUndeclaredThrowable();
                        }
                        if (e instanceof DistributedLockConflictException) {
                            System.out.println(Thread.currentThread().getName()
                                    + " is error ... ... DistributedLockConflictException");
                        } else if (e instanceof BusinessFinishedException) {
                            System.out.println(Thread.currentThread().getName()
                                    + " is error ... ... BusinessFinishedException--》" + e.getMessage());
                        } else {
                            System.out.println(Thread.currentThread().getName() + " is error ... ... ");
                        }
                    }
                    countDown2.countDown();
                }
            });
            threads[i].start();
        }
        for (int i = 0; i < threadCnt; i++) {
            countDown.countDown();
        }
        try {
            countDown2.await();
            Assert.assertEquals("done cnt not equals " + threadCnt, successCnt.intValue() + failCnt.intValue(),
                    threadCnt);
            // int t = threadCnt - 1;
            // Assert.assertEquals("failCnt not equals " + t,
            // failCnt.intValue(), t);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
