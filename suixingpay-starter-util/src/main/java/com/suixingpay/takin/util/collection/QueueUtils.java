/**  
 * All rights Reserved, Designed By Suixingpay.
 * @author: matieli<ma_tl@suixingpay.com> 
 * @date: 2017年3月8日 下午1:25:06   
 * @Copyright ©2017 Suixingpay. All rights reserved. 
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.takin.util.collection;

import java.util.ArrayDeque;
import java.util.Collections;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Queue工具集. 1.各种Queue，Dequeue的创建 2.LIFO的Stack的创建
 * 
 * @author: matieli[ma_tl@suixingpay.com]
 * @date: 2017年3月8日 下午1:58:15
 * @version: V1.0
 */
public abstract class QueueUtils {
    /**
     * 创建ArrayDeque (JDK无ArrayQueue) 需设置初始长度，默认为16，数组满时成倍扩容
     * 
     * @param initSize
     * @return
     */
    public static <E> ArrayDeque<E> newArrayDeque(int initSize) {
        return new ArrayDeque<E>(initSize);
    }

    /**
     * 创建LinkedDeque (LinkedList实现了Deque接口)
     */
    public static <E> LinkedList<E> newLinkedDeque() {
        return new LinkedList<E>();
    }

    /**
     * 创建无阻塞情况下，性能最优的并发队列
     */
    public static <E> ConcurrentLinkedQueue<E> newConcurrentNonBlockingQueue() {
        return new ConcurrentLinkedQueue<E>();
    }

    /**
     * 创建无阻塞情况下，性能最优的并发双端队列
     */
    public static <E> Deque<E> newConcurrentNonBlockingDeque() {
        return new ConcurrentLinkedDeque<E>();
    }

    /**
     * 创建并发阻塞情况下，长度不受限的队列. 长度不受限，即队列不会因为满而阻塞，但会因为空而阻塞.
     */
    public static <E> LinkedBlockingQueue<E> newBlockingUnlimitQueue() {
        return new LinkedBlockingQueue<E>();
    }

    /**
     * 创建并发阻塞情况下，长度不受限的双端队列. 长度不受限，即队列不会因为满而阻塞，但会因为空而阻塞.
     */
    public static <E> LinkedBlockingDeque<E> newBlockingUnlimitDeque() {
        return new LinkedBlockingDeque<E>();
    }

    /**
     * 创建并发阻塞情况下，长度受限，更节约内存，但共用一把锁的队列（无双端队列实现）.
     */
    public static <E> ArrayBlockingQueue<E> newArrayBlockingQueue(int capacity) {
        return new ArrayBlockingQueue<E>(capacity);
    }

    /**
     * 创建并发阻塞情况下，长度受限，头队尾两把锁, 但使用更多内存的队列.
     */
    public static <E> LinkedBlockingQueue<E> newLinkedBlockingQeque(int capacity) {
        return new LinkedBlockingQueue<E>(capacity);
    }

    /**
     * 创建并发阻塞情况下，长度受限，头队尾两把锁, 但使用更多内存的双端队列.
     */
    public static <E> LinkedBlockingDeque<E> newBlockingDeque(int capacity) {
        return new LinkedBlockingDeque<E>(capacity);
    }

    //////////////// Stack ///////////

    /**
     * 支持后进先出的栈，用ArrayDeque实现, 经过Collections#asLifoQueue()转换顺序 需设置初始长度，默认为16，数组满时成倍扩容
     */
    public static <E> Queue<E> newStack(int initSize) {
        return Collections.asLifoQueue(new ArrayDeque<E>(initSize));
    }

    /**
     * 支持后进先出的并发栈，用ConcurrentLinkedDeque实现，经过Collections#asLifoQueue()转换顺序 兼容了JDK6 另对于BlockingQueue接口，
     * JDK暂无Lifo倒转实现，因此只能直接使用未调转顺序的LinkedBlockingDeque
     */
    @SuppressWarnings("unchecked")
    public static <E> Queue<E> newConcurrentStack() {
        return (Queue<E>) Collections.asLifoQueue(newConcurrentNonBlockingDeque());
    }
}
